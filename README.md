# Currency Converter

## Introduction
A fast and accurate currency converter with an intuitive CLI.

## Features
- Uses exchange rates from an external CSV file
    - `./rates.csv`
        ```
        identifier,exchangeRate
        EUR,1
        USD,0.809552722
        GBP,1.126695
        BTC,6977.089657
        ETH,685.2944747
        FKE,0.025
        ```
    -   ```
        $ ./currency-converter --rates ./rates.csv BTC EUR 1
        6977.089657000000000000
        ```
- Can output results in a human readable format
    ```
    $ ./currency-converter --verbose BTC EUR 500
    500EUR -> 0.071663118087977868BTC
    ```

- Accepts multiple conversion requests
    ```
    $ ./currency-converter --verbose BTC EUR 500 USD EUR 100 ETH FKE 1000
    500EUR -> 0.071663118087977868BTC
    100EUR -> 123.525000018466987502USD
    1000FKE -> 0.036480667688068257ETH
    ```

## Exchange Rate Database File Format
- The exchange rate database file should be a simple [CSV](https://tools.ietf.org/html/rfc4180) file with 2 columns.
- The field **values** should not have any commas, quotes, spaces or line breaks.

## Built With
* [Gradle](https://gradle.org/) - A build tool with a focus on build automation and support for multi-language development
* [JUnit](https://junit.org/) - A programmer-friendly testing framework for Java and the JVM
* [picocli](https://picocli.info/) - A mighty tiny command line interface
* [System Rules](https://stefanbirkner.github.io/system-rules/) - A collection of JUnit rules for testing code that uses java.lang.System

## License
This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
