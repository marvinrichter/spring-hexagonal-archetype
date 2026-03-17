#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

/**
 * Value object representing a monetary amount with a currency.
 *
 * <p>Immutable by design — all arithmetic operations return new instances.</p>
 */
public final class Money {

    private final BigDecimal amount;
    private final Currency currency;

    private Money(BigDecimal amount, Currency currency) {
        Objects.requireNonNull(amount, "amount must not be null");
        Objects.requireNonNull(currency, "currency must not be null");
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Monetary amount must not be negative, was: " + amount);
        }
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
        this.currency = currency;
    }

    /**
     * Creates a {@code Money} instance.
     *
     * @param amount   the monetary amount; must not be {@code null} or negative
     * @param currency the ISO 4217 currency; must not be {@code null}
     * @return a new {@code Money} instance
     */
    public static Money of(BigDecimal amount, Currency currency) {
        return new Money(amount, currency);
    }

    /**
     * Convenience factory using a {@link String} currency code (e.g. {@code "EUR"}).
     *
     * @param amount       the monetary amount
     * @param currencyCode an ISO 4217 currency code
     * @return a new {@code Money} instance
     */
    public static Money of(BigDecimal amount, String currencyCode) {
        return new Money(amount, Currency.getInstance(currencyCode));
    }

    /**
     * Returns a new {@code Money} representing the sum of this instance and {@code other}.
     *
     * @param other the amount to add; must use the same currency
     * @return a new {@code Money} with the combined amount
     * @throws IllegalArgumentException if currencies differ
     */
    public Money add(Money other) {
        assertSameCurrency(other);
        return new Money(this.amount.add(other.amount), this.currency);
    }

    /**
     * Returns a new {@code Money} representing this instance minus {@code other}.
     *
     * @param other the amount to subtract; must use the same currency
     * @return a new {@code Money} with the resulting amount
     * @throws IllegalArgumentException if currencies differ or result is negative
     */
    public Money subtract(Money other) {
        assertSameCurrency(other);
        return new Money(this.amount.subtract(other.amount), this.currency);
    }

    /**
     * Returns {@code true} if this amount is greater than {@code other}.
     *
     * @param other the amount to compare; must use the same currency
     * @return comparison result
     */
    public boolean isGreaterThan(Money other) {
        assertSameCurrency(other);
        return this.amount.compareTo(other.amount) > 0;
    }

    public BigDecimal amount() {
        return amount;
    }

    public Currency currency() {
        return currency;
    }

    private void assertSameCurrency(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException(
                    "Cannot operate on different currencies: " + this.currency + " vs " + other.currency);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money other)) return false;
        return Objects.equals(amount, other.amount) && Objects.equals(currency, other.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

    @Override
    public String toString() {
        return amount.toPlainString() + " " + currency.getCurrencyCode();
    }
}
