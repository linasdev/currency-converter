package com.linasdev.currency_converter;

import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.ArgGroup;

@Command(
    name = "Currency Converter",
    version = "Currency Converter 1.0",
    mixinStandardHelpOptions = true)
public class App implements Runnable {
    private static final int CONVERTER_SCALE = 18;
    private static final RoundingMode CONVERTER_ROUNDING_MODE = RoundingMode.DOWN;

    private static class ConversionRequest {
        @Parameters(index = "0", description = "Identifier of the target currency.")
        String sourceCurrency;

        @Parameters(index = "1", description = "Identifier of the source currency.")
        String targetCurrency;

        @Parameters(index = "2", description = "Amount of source currency.")
        BigDecimal amount;
    }

    @Option(
        names = { "-r", "--rates" },
        description = "CSV file containing the exchange rates (default: ${DEFAULT-VALUE}).",
        defaultValue = "./exchange_rates.csv",
        paramLabel = "file") 
    private File exchangeRateDatabase;

    @Option(
        names = { "-b", "--base" },
        description = "Identifier of the base currency (default: ${DEFAULT-VALUE}).",
        defaultValue = "EUR",
        paramLabel = "currency") 
    private String baseCurrency;

    @ArgGroup(exclusive = false, multiplicity = "1..*")
    private List<ConversionRequest> conversionRequests;

    @Override
    public void run() {
        Converter converter = new Converter(baseCurrency, CONVERTER_SCALE, CONVERTER_ROUNDING_MODE);
        loadExchangeRates(converter);
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new App()).execute(args); 
        System.exit(exitCode);
    }

    private void loadExchangeRates(Converter converter) {
        try (BufferedReader reader =
                new BufferedReader(new FileReader(exchangeRateDatabase)))
        {    
            String line;
            for (int i = 1; (line = reader.readLine()) != null; i++) {
                try {
                    // Let's deviate from RFC 4180 a bit and disallow the use of
                    // commas, quotes, spaces and line breaks in a field for simplicity.
                    line = line.replaceAll("\"", "").replaceAll(" ", "");
                    String[] parts = line.split(",");

                    if (parts.length != 2) {
                        System.err.format(
                            "Invalid exchange rate database format (wrong number of delimiters on line %d).",
                            i
                        );
                        System.exit(-1);
                    }

                    String identifier = parts[0];
                    BigDecimal exchangeRate = new BigDecimal(parts[1]);

                    if (identifier.equals(baseCurrency)) { // Ignore exchange rates for base currency
                        continue;
                    }

                    converter.registerCurrency(identifier, exchangeRate);
                } catch (NumberFormatException e) {
                    if (i == 1) { // First line could be a header.
                        continue;
                    }

                    System.err.format(
                        "Invalid exchange rate database format (invalid number format on line %d).",
                        i
                    );
                    System.exit(-1);
                } catch (IllegalArgumentException e) {
                    System.err.format(
                        "Invalid exchange rate database format (reused identifier on line %d).",
                        i
                    );
                    System.exit(-1);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Exchange rate database file not found.");
            System.exit(-1);
        } catch (IOException e) {
            System.err.println("An IO exception occurred while reading the exchange rate database.");
            System.exit(-1);
        }
    }
}
