#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.adapter.in.web;

import ${package}.application.port.in.PlaceOrderUseCase;

/**
 * HTTP response DTO returned after placing an order.
 *
 * <p>Maps directly from the use case result to avoid leaking domain types
 * into the HTTP response shape.</p>
 *
 * @param orderId    the generated order identifier (UUID string)
 * @param customerId the customer who placed the order
 * @param status     the initial order status (always {@code PENDING})
 */
public record OrderResponse(String orderId, String customerId, String status) {

    /**
     * Convenience factory that maps from the application-layer result.
     *
     * @param result the use case result; must not be {@code null}
     * @return a new {@code OrderResponse}
     */
    public static OrderResponse from(PlaceOrderUseCase.Result result) {
        return new OrderResponse(result.orderId(), result.customerId(), result.status());
    }
}
