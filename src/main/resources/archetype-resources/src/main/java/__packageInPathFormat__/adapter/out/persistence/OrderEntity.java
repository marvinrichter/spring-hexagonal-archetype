#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.adapter.out.persistence;

import ${package}.domain.Order;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * JPA entity for persisting {@link Order} aggregates.
 *
 * <p>This class lives exclusively in the persistence adapter and must never
 * appear in the application or domain layers.</p>
 */
@Entity
@Table(name = "orders")
class OrderEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @Column(name = "total_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Order.Status status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    /** Required by JPA. */
    protected OrderEntity() {}

    OrderEntity(
            UUID id,
            String customerId,
            BigDecimal totalAmount,
            String currency,
            Order.Status status,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.customerId = customerId;
        this.totalAmount = totalAmount;
        this.currency = currency;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    UUID getId() { return id; }
    String getCustomerId() { return customerId; }
    BigDecimal getTotalAmount() { return totalAmount; }
    String getCurrency() { return currency; }
    Order.Status getStatus() { return status; }
    Instant getCreatedAt() { return createdAt; }
    Instant getUpdatedAt() { return updatedAt; }

    void setStatus(Order.Status status) { this.status = status; }
    void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
