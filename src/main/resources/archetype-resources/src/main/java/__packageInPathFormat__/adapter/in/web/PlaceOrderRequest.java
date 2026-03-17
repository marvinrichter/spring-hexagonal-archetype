#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.adapter.in.web;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * HTTP request DTO for placing a new order.
 *
 * <p>Validated by Bean Validation before reaching the use case.</p>
 *
 * @param customerId  identifier of the customer placing the order
 * @param totalAmount total monetary amount; must be positive
 * @param currency    ISO 4217 currency code (e.g. {@code "EUR"})
 */
public record PlaceOrderRequest(

        @NotBlank(message = "customerId must not be blank")
        String customerId,

        @NotNull(message = "totalAmount must not be null")
        @DecimalMin(value = "0.01", message = "totalAmount must be greater than zero")
        BigDecimal totalAmount,

        @NotBlank(message = "currency must not be blank")
        String currency
) {}
