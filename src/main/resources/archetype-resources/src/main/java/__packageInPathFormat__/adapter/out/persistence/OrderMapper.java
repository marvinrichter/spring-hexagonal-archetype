#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.adapter.out.persistence;

import ${package}.domain.Money;
import ${package}.domain.Order;
import ${package}.domain.OrderId;
import org.springframework.stereotype.Component;

/**
 * Maps between the {@link Order} domain aggregate and the {@link OrderEntity} JPA entity.
 *
 * <p>This anti-corruption layer ensures that persistence concerns (column types,
 * nullable defaults, JPA quirks) are isolated from the domain model.</p>
 */
@Component
class OrderMapper {

    /**
     * Maps a domain {@link Order} to its JPA {@link OrderEntity} representation.
     *
     * @param order the domain aggregate; must not be {@code null}
     * @return a new {@link OrderEntity}
     */
    OrderEntity toEntity(Order order) {
        return new OrderEntity(
                order.id().value(),
                order.customerId(),
                order.totalAmount().amount(),
                order.totalAmount().currency().getCurrencyCode(),
                order.status(),
                order.createdAt(),
                order.updatedAt()
        );
    }

    /**
     * Maps a JPA {@link OrderEntity} back to a domain {@link Order}.
     *
     * @param entity the JPA entity; must not be {@code null}
     * @return a domain aggregate reconstituted from the entity
     */
    Order toDomain(OrderEntity entity) {
        return new Order(
                OrderId.of(entity.getId()),
                entity.getCustomerId(),
                Money.of(entity.getTotalAmount(), entity.getCurrency()),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
