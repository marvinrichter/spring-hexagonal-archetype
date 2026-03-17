#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.domain;

import java.time.Instant;
import java.util.Objects;

/**
 * Aggregate root representing a customer order.
 *
 * <p>This class is intentionally free of any framework annotations or imports.
 * All business invariants live here — no leakage into adapters.</p>
 *
 * <p>An {@code Order} moves through the following lifecycle:</p>
 * <pre>
 *   PENDING → CONFIRMED → SHIPPED → DELIVERED
 *                       ↘ CANCELLED
 * </pre>
 */
public final class Order {

    /** Lifecycle states of an order. */
    public enum Status {
        PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED
    }

    private final OrderId id;
    private final String customerId;
    private final Money totalAmount;
    private Status status;
    private final Instant createdAt;
    private Instant updatedAt;

    /**
     * Reconstitutes an existing {@code Order} from persistence.
     * Use {@link ${symbol_pound}placeOrder(String, Money)} to create a brand-new order.
     */
    public Order(
            OrderId id,
            String customerId,
            Money totalAmount,
            Status status,
            Instant createdAt,
            Instant updatedAt) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.customerId = Objects.requireNonNull(customerId, "customerId must not be null");
        if (customerId.isBlank()) {
            throw new IllegalArgumentException("customerId must not be blank");
        }
        this.totalAmount = Objects.requireNonNull(totalAmount, "totalAmount must not be null");
        this.status = Objects.requireNonNull(status, "status must not be null");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt must not be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt must not be null");
    }

    /**
     * Factory method — creates a new order in {@link Status${symbol_pound}PENDING} state.
     *
     * @param customerId  non-blank customer identifier
     * @param totalAmount the order total; must not be {@code null}
     * @return a new {@code Order} ready for confirmation
     */
    public static Order placeOrder(String customerId, Money totalAmount) {
        Instant now = Instant.now();
        return new Order(OrderId.newOrderId(), customerId, totalAmount, Status.PENDING, now, now);
    }

    /**
     * Confirms this order, transitioning it from {@code PENDING} to {@code CONFIRMED}.
     *
     * @throws IllegalStateException if the order is not in {@code PENDING} state
     */
    public void confirm() {
        requireStatus(Status.PENDING, "confirm");
        this.status = Status.CONFIRMED;
        this.updatedAt = Instant.now();
    }

    /**
     * Cancels this order.
     *
     * @throws IllegalStateException if the order has already been shipped or delivered
     */
    public void cancel() {
        if (status == Status.SHIPPED || status == Status.DELIVERED) {
            throw new IllegalStateException(
                    "Cannot cancel order in status " + status + ". Order id: " + id);
        }
        this.status = Status.CANCELLED;
        this.updatedAt = Instant.now();
    }

    // -----------------------------------------------------------------------
    // Accessors
    // -----------------------------------------------------------------------

    public OrderId id() { return id; }

    public String customerId() { return customerId; }

    public Money totalAmount() { return totalAmount; }

    public Status status() { return status; }

    public Instant createdAt() { return createdAt; }

    public Instant updatedAt() { return updatedAt; }

    // -----------------------------------------------------------------------
    // Internal helpers
    // -----------------------------------------------------------------------

    private void requireStatus(Status required, String operation) {
        if (this.status != required) {
            throw new IllegalStateException(
                    "Cannot " + operation + " order in status " + status
                    + ". Required: " + required + ". Order id: " + id);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order other)) return false;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Order{id=" + id + ", customer=" + customerId + ", status=" + status + ", total=" + totalAmount + "}";
    }
}
