package com.linasdev.currency_converter;

import java.io.File;
import java.math.BigDecimal;
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
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new App()).execute(args); 
        System.exit(exitCode);
    }
}
