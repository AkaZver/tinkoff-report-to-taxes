package ru.a_z.tools.taxes.exporter.xlsx;

import lombok.extern.log4j.Log4j2;
import org.dhatim.fastexcel.Workbook;
import ru.a_z.tools.taxes.exporter.Exporter;
import ru.a_z.tools.taxes.exporter.ExporterField;
import ru.a_z.tools.taxes.model.DividendPayment;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Экспортер в XLSX формат Excel.
 * Используется библиотека <a href="https://github.com/dhatim/fastexcel">fastexcel</a>
 */
@Log4j2
public class XlsxExporter implements Exporter {

    @Override
    public void execute(List<DividendPayment> payments, Path output) {
        log.info(">> execute: paymentsSize={}, output={}", payments.size(), output);
        log.debug("-- execute: payments={}", payments);

        try (OutputStream outputStream = Files.newOutputStream(output)) {
            Workbook workbook = new Workbook(outputStream, "tinkoff-report-to-taxes", null);

            WorksheetWrapper wrapper = WorksheetWrapper
                    .with(workbook.newWorksheet("Main"))
                    .headers(ExporterField.values());

            for (DividendPayment payment : payments) {
                wrapper.nextRow()
                        .firstColumn()
                        .date(payment::getRegistryFixDate)
                        .date(payment::getPaymentDate)
                        .integer(payment::getPaymentType)
                        .string(payment::getNameOfSecurity)
                        .string(payment::getIsin)
                        .string(payment::getIssuerCountry)
                        .bigDecimal(payment::getNumberOfSecurities)
                        .bigDecimal(payment::getPaymentPerPaper)
                        .bigDecimal(payment::getCommission)
                        .bigDecimal(payment::getAmountBeforeTax)
                        .bigDecimal(payment::getTaxes)
                        .formula(fetchTaxPercentFormula(wrapper.getRow()))
                        .bigDecimal(payment::getTotalPaymentAmount)
                        .string(payment::getCurrency);
            }

            wrapper.finish();
            workbook.finish();
        } catch (IOException exception) {
            String message = "Не удалось обработать XLSX-файл " + output;
            throw new IllegalStateException(message, exception);
        }

        log.info("<< execute");
    }

    /**
     * Формула для расчёта процента налога по бумаге.
     * Примерный вид: ОКРУГЛ($J2/$I2; 1)
     *
     * @param row Номер текущей строки
     * @return Формула в формате Excel
     */
    private String fetchTaxPercentFormula(int row) {
        return String.format("ROUND($K%1$d/$J%1$d, 1)", row + 1);
    }

}
