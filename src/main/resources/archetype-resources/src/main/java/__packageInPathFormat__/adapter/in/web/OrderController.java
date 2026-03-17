#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.adapter.in.web;

import ${package}.application.port.in.PlaceOrderUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Inbound adapter exposing the order use cases over HTTP.
 *
 * <p>This class depends only on application-layer ports — never on domain
 * internals or outbound adapters.</p>
 */
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final PlaceOrderUseCase placeOrderUseCase;

    public OrderController(PlaceOrderUseCase placeOrderUseCase) {
        this.placeOrderUseCase = placeOrderUseCase;
    }

    /**
     * Places a new order.
     *
     * <pre>
     * POST /api/v1/orders
     * Content-Type: application/json
     *
     * {
     *   "customerId": "customer-42",
     *   "totalAmount": 49.99,
     *   "currency": "EUR"
     * }
     * </pre>
     *
     * @param request the validated order placement request
     * @return {@code 201 Created} with the order response body
     */
    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(@Valid @RequestBody PlaceOrderRequest request) {
        PlaceOrderUseCase.Command command = new PlaceOrderUseCase.Command(
                request.customerId(),
                request.totalAmount(),
                request.currency()
        );

        PlaceOrderUseCase.Result result = placeOrderUseCase.placeOrder(command);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(OrderResponse.from(result));
    }
}
