#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.integration;

import ${package}.application.port.in.PlaceOrderUseCase;
import ${package}.application.port.out.OrderRepository;
import ${package}.domain.Order;
import ${package}.domain.OrderId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for the full Order vertical slice.
 *
 * <p>Spins up a real PostgreSQL container via Testcontainers and verifies
 * the complete REST → use case → domain → JPA round-trip.</p>
 *
 * <p>Spring Boot 4 notes:
 * <ul>
 *   <li>{@code @AutoConfigureMockMvc} moved to {@code org.springframework.boot.webmvc.test.autoconfigure}</li>
 *   <li>{@code PostgreSQLContainer} in testcontainers 2.x is no longer generic — use raw type</li>
 *   <li>Jackson 3 ObjectMapper is in {@code tools.jackson.databind}</li>
 * </ul></p>
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
@DisplayName("Order Integration Tests")
class OrderIntegrationTest {

    @Container
    @ServiceConnection
    @SuppressWarnings("resource")
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:16-alpine");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PlaceOrderUseCase placeOrderUseCase;

    // -----------------------------------------------------------------------
    // REST round-trip tests
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("POST /api/v1/orders persists order and returns 201 with PENDING status")
    void placeOrder_via_http_creates_pending_order() throws Exception {
        // GIVEN
        Map<String, Object> body = Map.of(
                "customerId", "integration-customer-1",
                "totalAmount", 99.99,
                "currency", "EUR"
        );

        // WHEN / THEN
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderId").isNotEmpty())
                .andExpect(jsonPath("$.customerId").value("integration-customer-1"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    @DisplayName("Placed order is retrievable from the repository by its generated ID")
    void placeOrder_via_useCase_is_persisted_and_retrievable() {
        // GIVEN
        PlaceOrderUseCase.Command command = new PlaceOrderUseCase.Command(
                "integration-customer-2",
                new BigDecimal("199.00"),
                "USD"
        );

        // WHEN
        PlaceOrderUseCase.Result result = placeOrderUseCase.placeOrder(command);

        // THEN — result fields
        assertThat(result.orderId()).isNotBlank();
        assertThat(result.customerId()).isEqualTo("integration-customer-2");
        assertThat(result.status()).isEqualTo("PENDING");

        // THEN — persisted correctly
        OrderId orderId = OrderId.of(UUID.fromString(result.orderId()));
        Optional<Order> found = orderRepository.findById(orderId);
        assertThat(found).isPresent();

        Order order = found.get();
        assertThat(order.customerId()).isEqualTo("integration-customer-2");
        assertThat(order.totalAmount().amount()).isEqualByComparingTo("199.00");
        assertThat(order.totalAmount().currency().getCurrencyCode()).isEqualTo("USD");
        assertThat(order.status()).isEqualTo(Order.Status.PENDING);
    }

    @Test
    @DisplayName("Multiple orders can be placed independently — no state bleed")
    void multiple_orders_are_independent() {
        // WHEN
        PlaceOrderUseCase.Result first = placeOrderUseCase.placeOrder(
                new PlaceOrderUseCase.Command("customer-A", new BigDecimal("10.00"), "EUR"));
        PlaceOrderUseCase.Result second = placeOrderUseCase.placeOrder(
                new PlaceOrderUseCase.Command("customer-B", new BigDecimal("20.00"), "EUR"));

        // THEN
        assertThat(first.orderId()).isNotEqualTo(second.orderId());
        assertThat(first.customerId()).isEqualTo("customer-A");
        assertThat(second.customerId()).isEqualTo("customer-B");
    }

    @Test
    @DisplayName("Order repository reports non-existent order as absent")
    void findById_unknownId_returnsEmpty() {
        // GIVEN
        OrderId unknownId = OrderId.of(UUID.randomUUID());

        // WHEN
        Optional<Order> result = orderRepository.findById(unknownId);

        // THEN
        assertThat(result).isEmpty();
    }
}
