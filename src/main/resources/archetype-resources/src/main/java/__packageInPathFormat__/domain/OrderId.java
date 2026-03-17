#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.domain;

import java.util.Objects;
import java.util.UUID;

/**
 * Value object representing a strongly-typed Order identifier.
 *
 * <p>Using a dedicated type instead of a raw {@code UUID} prevents primitive obsession
 * and makes it impossible to accidentally pass a {@code CustomerId} where an
 * {@code OrderId} is required.</p>
 */
public final class OrderId {

    private final UUID value;

    private OrderId(UUID value) {
        this.value = Objects.requireNonNull(value, "OrderId value must not be null");
    }

    /**
     * Creates a new {@code OrderId} from an existing {@link UUID}.
     *
     * @param value the UUID value; must not be {@code null}
     * @return a new {@code OrderId} instance
     */
    public static OrderId of(UUID value) {
        return new OrderId(value);
    }

    /**
     * Generates a new, random {@code OrderId}.
     *
     * @return a new {@code OrderId} backed by a random UUID
     */
    public static OrderId newOrderId() {
        return new OrderId(UUID.randomUUID());
    }

    /**
     * Returns the underlying {@link UUID}.
     *
     * @return the UUID value; never {@code null}
     */
    public UUID value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderId other)) return false;
        return Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "OrderId{" + value + "}";
    }
}
