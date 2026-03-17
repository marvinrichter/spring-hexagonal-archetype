#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Spring Data JPA repository for {@link OrderEntity}.
 *
 * <p>Package-private — only {@link JpaOrderRepository} should use this interface.
 * External code depends on the {@code OrderRepository} port, not this Spring Data adapter.</p>
 */
interface SpringDataOrderRepository extends JpaRepository<OrderEntity, UUID> {
}
