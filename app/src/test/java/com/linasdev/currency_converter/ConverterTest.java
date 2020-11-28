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
        Converter converter = new Converter("EUR", 18, RoundingMode.DOWN);
        converter.registerCurrency("USD", BigDecimal.valueOf(0.809552722));

        BigDecimal expected = new BigDecimal("0.099944780312927107");
        BigDecimal result = converter.convert("EUR", "USD", BigDecimal.valueOf(0.1234567899));

        assertEquals(expected, result);
    }
    
    @Test
    public void conversionFromBaseCurrencyShouldBeCorrect() {
        Converter converter = new Converter("EUR", 18, RoundingMode.DOWN);
        converter.registerCurrency("USD", BigDecimal.valueOf(0.809552722));
        
        BigDecimal expected = new BigDecimal("0.152499999746773749");
        BigDecimal result = converter.convert("USD", "EUR", BigDecimal.valueOf(0.1234567899));

        assertEquals(expected, result);
    }
    
    @Test
    public void conversionOfArbitraryCurrenciesShouldBeCorrect() {
        Converter converter = new Converter("EUR", 18, RoundingMode.DOWN);
        converter.registerCurrency("USD", BigDecimal.valueOf(0.809552722));
        converter.registerCurrency("BTC", BigDecimal.valueOf(6977.089657));

        BigDecimal expected = new BigDecimal("1064.006170925717749979");
        BigDecimal result = converter.convert("USD", "BTC", BigDecimal.valueOf(0.1234567899));

        assertEquals(expected, result);
    }
}
