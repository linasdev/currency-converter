package com.linasdev.currency_converter;

import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ConverterTest {
    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenSourceCurrencyIsNotRegistered() {
        Converter converter = new Converter("EUR", 18, RoundingMode.DOWN);

        converter.convert("EUR", "USD", BigDecimal.ZERO);
    }

    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenTargetCurrencyIsNotRegistered() {
        Converter converter = new Converter("EUR", 18, RoundingMode.DOWN);

        converter.convert("USD", "EUR", BigDecimal.ZERO);
    }
    
    @Test
    public void conversionToBaseCurrencyShouldBeCorrect() {
        // TODO: implement test;
    }
    
    @Test
    public void conversionFromBaseCurrencyShouldBeCorrect() {
        // TODO: implement test;
    }
    
    @Test
    public void conversionOfArbitraryCurrenciesShouldBeCorrect() {
        // TODO: implement test;
    }
}
