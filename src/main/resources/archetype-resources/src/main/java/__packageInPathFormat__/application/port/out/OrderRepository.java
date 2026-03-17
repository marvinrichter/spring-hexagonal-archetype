#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.application.port.out;

import ${package}.domain.Order;
import ${package}.domain.OrderId;

import java.util.Optional;

/**
 * Outbound port — the secondary driven port for persisting and retrieving orders.
 *
 * <p>The application service depends on this interface.
 * The concrete implementation lives in the outbound adapter
 * ({@code adapter.out.persistence}).</p>
 */
public interface OrderRepository {

    /**
     * Persists a new order or updates an existing one.
     *
     * @param order the order to save; must not be {@code null}
     * @return the saved order (may be a new instance reflecting persistence-layer state)
     */
    Order save(Order order);

    /**
     * Retrieves an order by its identifier.
     *
     * @param orderId the order identifier to look up; must not be {@code null}
     * @return an {@link Optional} containing the found order, or empty if not found
     */
    Optional<Order> findById(OrderId orderId);

    /**
     * Checks whether an order with the given identifier exists.
     *
     * @param orderId the order identifier to check; must not be {@code null}
     * @return {@code true} if an order with the given id exists
     */
    boolean existsById(OrderId orderId);
}
