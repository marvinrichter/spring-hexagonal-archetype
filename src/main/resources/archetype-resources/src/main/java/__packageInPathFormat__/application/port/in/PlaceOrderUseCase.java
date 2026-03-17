#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.application.port.in;

import ${package}.domain.Order;

import java.math.BigDecimal;

/**
 * Inbound port — the primary driving port for placing a new order.
 *
 * <p>Inbound adapters (e.g. {@code OrderController}) depend on this interface.
 * The implementation lives in the application service layer.</p>
 *
 * <p>Following the <em>Command / Result</em> pattern, the command and its result
 * are defined as nested types to keep the port self-contained.</p>
 */
public interface PlaceOrderUseCase {

    /**
     * Places a new order and returns a view of the created order.
     *
     * @param command the command carrying the placement request data
     * @return a result containing the persisted order information
     */
    Result placeOrder(Command command);

    // -----------------------------------------------------------------------
    // Nested command / result types
    // -----------------------------------------------------------------------

    /**
     * Immutable command object carrying the data required to place an order.
     *
     * @param customerId  the identifier of the customer placing the order; must not be blank
     * @param totalAmount the total monetary amount of the order; must be positive
     * @param currency    ISO 4217 currency code (e.g. {@code "EUR"})
     */
    record Command(String customerId, BigDecimal totalAmount, String currency) {

        public Command {
            if (customerId == null || customerId.isBlank()) {
                throw new IllegalArgumentException("customerId must not be blank");
            }
            if (totalAmount == null || totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("totalAmount must be positive");
            }
            if (currency == null || currency.isBlank()) {
                throw new IllegalArgumentException("currency must not be blank");
            }
        }
    }

    /**
     * Immutable result carrying the essential details of the created order.
     *
     * @param orderId    the generated order identifier
     * @param customerId the customer who placed the order
     * @param status     the initial order status (always {@code PENDING})
     */
    record Result(String orderId, String customerId, String status) {}
}
