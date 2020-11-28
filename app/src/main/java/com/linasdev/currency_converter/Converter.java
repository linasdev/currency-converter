package com.linasdev.currency_converter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class Converter {
    private String baseCurrency;
    private Map<String, BigDecimal> exchangeRates;

    /**
     * Constructs a new converter.
     * 
     * @param baseCurrency a unique identifier for the base currency, not null
     */
    public Converter(String baseCurrency) {
        this.baseCurrency = baseCurrency;
        exchangeRates = new HashMap<>();

        exchangeRates.put(baseCurrency, BigDecimal.ONE);
    }

    /**
     * Registers a new currency.
     * 
     * @param identifier a unique identifier for the currency, not null
     * @param exchangeRate price of the currency in relation to the base currency, not null
     * @throws IllegalArgumentException if identifier is already used.
     */
    public void registerCurrency(String identifier, BigDecimal exchangeRate) {
        if (exchangeRates.containsKey(identifier)) {
            throw new IllegalArgumentException("Currency identifier already used.");
        }

        exchangeRates.put(identifier, exchangeRate);
    }

    /**
     * Calculates amount of target currency equivalent to the specified amount of source currency.
     * 
     * @param targetCurrency unique identifier of target currency, not null
     * @param sourceCurrency unique identifier of source currency, not null
     * @param sourceAmount amount of source currency, not null
     * @return amount of target currency
     * @throws IllegalArgumentException if either of the currencies is not registered.
     */
    public BigDecimal convert(
        String targetCurrency,
        String sourceCurrency,
        BigDecimal sourceAmount)
    {
        if (!exchangeRates.containsKey(targetCurrency)) {
            throw new IllegalArgumentException("Target currency is not registered.");
        }

        if (!exchangeRates.containsKey(sourceCurrency)) {
            throw new IllegalArgumentException("Source currency is not registered.");
        }

        BigDecimal targetRate = exchangeRates.get(targetCurrency);
        BigDecimal sourceRate = exchangeRates.get(sourceCurrency);

        BigDecimal baseAmount = sourceAmount.multiply(sourceRate);
        // TODO: use proper math context;
        BigDecimal targetAmount = baseAmount.divide(targetRate, RoundingMode.DOWN);

        return targetAmount;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }
}