#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.adapter.out.persistence;

import ${package}.application.port.out.OrderRepository;
import ${package}.domain.Order;
import ${package}.domain.OrderId;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Outbound adapter — implements the {@link OrderRepository} port using Spring Data JPA.
 *
 * <p>Translates between the domain {@link Order} aggregate and the {@link OrderEntity}
 * persistence model via {@link OrderMapper}.</p>
 */
@Repository
public class JpaOrderRepository implements OrderRepository {

    private final SpringDataOrderRepository springDataRepository;
    private final OrderMapper mapper;

    public JpaOrderRepository(SpringDataOrderRepository springDataRepository, OrderMapper mapper) {
        this.springDataRepository = springDataRepository;
        this.mapper = mapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Order save(Order order) {
        OrderEntity entity = mapper.toEntity(order);
        OrderEntity saved = springDataRepository.save(entity);
        return mapper.toDomain(saved);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Order> findById(OrderId orderId) {
        return springDataRepository
                .findById(orderId.value())
                .map(mapper::toDomain);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsById(OrderId orderId) {
        return springDataRepository.existsById(orderId.value());
    }
}
