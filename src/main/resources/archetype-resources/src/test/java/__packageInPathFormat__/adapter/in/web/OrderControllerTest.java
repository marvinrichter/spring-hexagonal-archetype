#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.adapter.in.web;

import ${package}.application.port.in.PlaceOrderUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for {@link OrderController} using Spring MVC test slice.
 *
 * <p>The application service is mocked — these tests verify HTTP wiring only.</p>
 *
 * <p>Spring Boot 4 notes:
 * <ul>
 *   <li>{@code @WebMvcTest} moved to {@code org.springframework.boot.webmvc.test.autoconfigure}</li>
 *   <li>Jackson 3 ObjectMapper is in {@code tools.jackson.databind}</li>
 *   <li>{@code @MockitoBean} is in {@code org.springframework.test.context.bean.override.mockito}</li>
 * </ul></p>
 */
@WebMvcTest(OrderController.class)
@DisplayName("OrderController")
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PlaceOrderUseCase placeOrderUseCase;

    @Test
    @DisplayName("POST /api/v1/orders returns 201 with order details when request is valid")
    void placeOrder_validRequest_returns201() throws Exception {
        // GIVEN
        String orderId = "550e8400-e29b-41d4-a716-446655440000";
        when(placeOrderUseCase.placeOrder(any()))
                .thenReturn(new PlaceOrderUseCase.Result(orderId, "customer-42", "PENDING"));

        PlaceOrderRequest request = new PlaceOrderRequest("customer-42", new BigDecimal("49.99"), "EUR");

        // WHEN / THEN
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderId").value(orderId))
                .andExpect(jsonPath("$.customerId").value("customer-42"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    @DisplayName("POST /api/v1/orders returns 400 when customerId is blank")
    void placeOrder_blankCustomerId_returns400() throws Exception {
        // GIVEN
        PlaceOrderRequest request = new PlaceOrderRequest("", new BigDecimal("49.99"), "EUR");

        // WHEN / THEN
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/v1/orders returns 400 when totalAmount is zero")
    void placeOrder_zeroAmount_returns400() throws Exception {
        // GIVEN
        PlaceOrderRequest request = new PlaceOrderRequest("customer-42", BigDecimal.ZERO, "EUR");

        // WHEN / THEN
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/v1/orders returns 400 when currency is blank")
    void placeOrder_blankCurrency_returns400() throws Exception {
        // GIVEN
        PlaceOrderRequest request = new PlaceOrderRequest("customer-42", new BigDecimal("10.00"), "");

        // WHEN / THEN
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/v1/orders returns 400 when body is missing")
    void placeOrder_missingBody_returns400() throws Exception {
        // WHEN / THEN
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
