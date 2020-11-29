package com.linasdev.currency_converter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class Converter {
    private String baseCurrency;
    private Map<String, BigDecimal> exchangeRates;
    private int scale;
    private RoundingMode roundingMode;

    /**
     * Constructs a new converter.
     * 
     * @param baseCurrency a unique identifier for the base currency, not null
     * @param scale scale of all calculation results, not null
     * @param roundingMode rounding to use for all calculations, not null
     */
    public Converter(String baseCurrency, int scale, RoundingMode roundingMode) {
        this.baseCurrency = baseCurrency;
        this.scale = scale;
        this.roundingMode = roundingMode;
        this.exchangeRates = new HashMap<>();

        exchangeRates.put(baseCurrency, BigDecimal.ONE);
    }

    /**
     * Registers a new currency.
     * 
     * @param identifier a unique identifier for the currency, not null
     * @param exchangeRate price of the currency in relation to the base currency, not null, >0
     * @throws IllegalArgumentException if identifier is already used.
     */
    public void registerCurrency(String identifier, BigDecimal exchangeRate) {
        if (exchangeRates.containsKey(identifier))
            throw new IllegalArgumentException("Currency identifier already used.");

        exchangeRates.put(identifier, exchangeRate);
    }

    /**
     * Checks if a currency is registered.
     * 
     * @param identifier a unique identifier for the currency, not null
     * @return true if identifier is already used, false otherwise
     */
    public boolean isCurrencyRegistered(String identifier) {
        return exchangeRates.containsKey(identifier);
    }

    /**
     * Calculates amount of target currency equivalent to the specified amount of source currency.
     * 
     * @param targetCurrency unique identifier of the target currency, not null
     * @param sourceCurrency unique identifier of the source currency, not null
     * @param amount amount of source currency, not null
     * @return amount of target currency
     * @throws IllegalArgumentException if either of the currencies is not registered.
     */
    public BigDecimal convert(
        String targetCurrency,
        String sourceCurrency,
        BigDecimal amount)
    {
        if (!exchangeRates.containsKey(targetCurrency))
            throw new IllegalArgumentException("Target currency is not registered.");

        if (!exchangeRates.containsKey(sourceCurrency))
            throw new IllegalArgumentException("Source currency is not registered.");

        BigDecimal targetRate = exchangeRates.get(targetCurrency);
        BigDecimal sourceRate = exchangeRates.get(sourceCurrency);

        BigDecimal baseAmount = amount.multiply(sourceRate);
        BigDecimal targetAmount = baseAmount.divide(targetRate, scale, roundingMode);

        return targetAmount;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }
}
