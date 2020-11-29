package com.linasdev.currency_converter;

import org.junit.rules.TemporaryFolder;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.contrib.java.lang.system.SystemErrRule;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;

public class CliTest {
    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Rule
    public final SystemErrRule systemErrRule = new SystemErrRule().muteForSuccessfulTests();

	@Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().muteForSuccessfulTests().enableLog();
    
    @Rule
    public final TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void shouldFailWhenNoParametersAreGiven() {
        // See https://picocli.info/#_exception_exit_codes
        exit.expectSystemExitWithStatus(2);

        Cli.main(new String[]{});
    }

    @Test
    public void shouldFailWhenExchangeRateDatabaseFileNotFound() {
        exit.expectSystemExitWithStatus(-1);

        File nonExistentFile = new File(folder.getRoot(), "exchange_rates.csv");

        Cli.main(new String[]{
            "-r",
            nonExistentFile.getPath(),
            "EUR",
            "USD",
            "0"
        });
    }

    @Test
    public void shouldFailWhenExchangeRateDatabaseHasInvalidFormat() throws IOException {
        exit.expectSystemExitWithStatus(-1);

        File exchangeRateDatabase = folder.newFile("exchange_rates.csv");

        try (PrintWriter writer = new PrintWriter(exchangeRateDatabase)) {
            writer.println("identifier,exchange_rate");
            writer.println("EUR,1");
            writer.println("USD,0,809552722"); // Comma instead of a period
        }

        Cli.main(new String[]{
            "-r",
            exchangeRateDatabase.getPath(),
            "EUR",
            "USD",
            "0"
        });
    }

    @Test
    public void shouldFailWhenExchangeRateIsInvalid() throws IOException {
        exit.expectSystemExitWithStatus(-1);

        File exchangeRateDatabase = folder.newFile("exchange_rates.csv");

        try (PrintWriter writer = new PrintWriter(exchangeRateDatabase)) {
            writer.println("identifier,exchange_rate");
            writer.println("EUR,1");
            writer.println("USD,-0.809552722"); // Negative exchange rate
        }

        Cli.main(new String[]{
            "-r",
            exchangeRateDatabase.getPath(),
            "EUR",
            "USD",
            "0"
        });
    }

    @Test
    public void shouldCorrectlyConvertMultipleCurrencies() throws IOException {
        File exchangeRateDatabase = folder.newFile("exchange_rates.csv");

        try (PrintWriter writer = new PrintWriter(exchangeRateDatabase)) {
            writer.println("identifier,exchange_rate");
            writer.println("EUR,1");
            writer.println("USD,0.809552722");
            writer.println("BTC,6977.089657");
        }

        Cli.main(new String[]{
            "-r",
            exchangeRateDatabase.getPath(),

            "USD",
            "EUR",
            "0.1234567899",

            "USD",
            "BTC",
            "0.1234567899"
        });

        assertEquals(
            "0.152499999746773749\n1064.006170925717749979\n",
            systemOutRule.getLogWithNormalizedLineSeparator()    
        );
    }
}
