package ru.a_z.tools.taxes.exporter.csv;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import ru.a_z.tools.taxes.exporter.Exporter;
import ru.a_z.tools.taxes.exporter.ExporterField;
import ru.a_z.tools.taxes.model.DividendPayment;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

/**
 * Экспортер в формат CSV.
 * Используется библиотека <a href="https://github.com/apache/commons-csv">commons-csv</a>
 */
@Log4j2
public class CsvExporter implements Exporter {

    private static final BigDecimal HUNDRED = new BigDecimal(100);
    private static final Charset CP1251 = Charset.forName("CP1251");

    @Override
    public void execute(List<DividendPayment> payments, Path output) {
        log.info(">> execute: paymentsSize={}, output={}", payments.size(), output);
        log.debug("-- execute: payments={}", payments);

        String[] header = Arrays.stream(ExporterField.values())
                .map(ExporterField::getValue)
                .toArray(String[]::new);

        CSVFormat csvFormat = CSVFormat.Builder
                .create(CSVFormat.EXCEL)
                .setQuoteMode(QuoteMode.ALL)
                .setHeader(header)
                .setDelimiter(';')
                .build();

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(output, CP1251);
             CSVPrinter csvPrinter = new CSVPrinter(bufferedWriter, csvFormat)) {
            for (DividendPayment payment : payments) {
                csvPrinter.printRecord(fetchCsvRecord(payment));
            }
        } catch (IOException exception) {
            String message = "Не удалось обработать CSV-файл " + output;
            throw new IllegalStateException(message, exception);
        }

        log.info("<< execute");
    }

    private Object[] fetchCsvRecord(DividendPayment payment) {
        log.debug(">> fetchCsvRecord: payment={}", payment);

        BigDecimal paymentPerPaper = payment.getPaymentPerPaper();
        BigDecimal numberOfSecurities = payment.getNumberOfSecurities();
        BigDecimal taxes = payment.getTaxes();

        BigDecimal amountBeforeTax = payment.getAmountBeforeTax();
        BigDecimal taxPercent = fetchTaxPercent(taxes, amountBeforeTax);
        String formattedTaxPercent = formatNumber(taxPercent.multiply(HUNDRED)) + "%";

        Object[] csvRecord = {
                payment.getRegistryFixDate(),
                payment.getPaymentDate(),
                payment.getPaymentType(),
                payment.getNameOfSecurity(),
                payment.getIsin(),
                payment.getIssuerCountry(),
                formatNumber(numberOfSecurities),
                formatNumber(paymentPerPaper),
                formatNumber(payment.getCommission()),
                formatNumber(amountBeforeTax),
                formatNumber(taxes),
                formattedTaxPercent,
                formatNumber(payment.getTotalPaymentAmount()),
                payment.getCurrency()
        };

        log.debug("<< fetchCsvRecord: csvRecord={}", csvRecord);
        return csvRecord;
    }

    private BigDecimal fetchTaxPercent(BigDecimal taxes, BigDecimal amountBeforeTax) {
        return taxes.divide(amountBeforeTax, RoundingMode.HALF_UP);
    }

    private String formatNumber(Number number) {
        return number.toString().replace('.', ',');
    }

}
