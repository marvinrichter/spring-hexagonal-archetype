#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.application.service;

import ${package}.application.port.in.PlaceOrderUseCase;
import ${package}.application.port.out.OrderRepository;
import ${package}.domain.Money;
import ${package}.domain.Order;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Application service that implements the {@link PlaceOrderUseCase} inbound port.
 *
 * <p>This class orchestrates the domain logic and delegates persistence to the
 * {@link OrderRepository} outbound port. It must never depend on adapter classes.</p>
 *
 * <p>Micrometer metrics are recorded here to observe domain-level events without
 * polluting the domain or adapter layers.</p>
 */
@Service
@Transactional
public class PlaceOrderService implements PlaceOrderUseCase {

    private final OrderRepository orderRepository;
    private final Counter ordersPlacedCounter;

    public PlaceOrderService(OrderRepository orderRepository, MeterRegistry meterRegistry) {
        this.orderRepository = orderRepository;
        this.ordersPlacedCounter = Counter.builder("orders.placed")
                .description("Total number of orders successfully placed")
                .register(meterRegistry);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Creates a new {@link Order} aggregate via the domain factory method,
     * persists it via the outbound port, and records a metric.</p>
     */
    @Override
    public Result placeOrder(Command command) {
        Money totalAmount = Money.of(command.totalAmount(), command.currency());
        Order order = Order.placeOrder(command.customerId(), totalAmount);

        Order saved = orderRepository.save(order);

        ordersPlacedCounter.increment();

        return new Result(
                saved.id().value().toString(),
                saved.customerId(),
                saved.status().name()
        );
    }
}
