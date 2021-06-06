package ru.a_z.tools.taxes.model;

import lombok.extern.log4j.Log4j2;
import technology.tabula.RectangularTextContainer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Маппер из PDF-представления строки таблицы в объект {@link DividendPayment}
 */
@Log4j2
public class DividendPaymentMapper {

    private static final int NORMAL_ROW_SIZE = 12;
    private static final DateTimeFormatter PDF_DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public DividendPayment map(List<RectangularTextContainer<?>> row) {
        log.trace(">> map: row={}", row);

        if (row.size() != NORMAL_ROW_SIZE) {
            String message = String.format("Строка таблицы должна иметь %d полей, но содержит %d: %s",
                    NORMAL_ROW_SIZE, row.size(), fetchFullRowText(row));

            throw new IllegalArgumentException(message);
        }

        DividendPayment payment = DividendPayment.builder()
                .registryFixDate(fetchLocalDate(row.get(0)))
                .paymentDate(fetchLocalDate(row.get(1)))
                .paymentType(fetchInt(row.get(2)))
                .nameOfSecurity(fetchString(row.get(3)))
                .isin(fetchString(row.get(4)))
                .issuerCountry(fetchString(row.get(5)))
                .numberOfSecurities(fetchBigDecimal(row.get(6)))
                .paymentPerPaper(fetchBigDecimal(row.get(7)))
                .commission(fetchBigDecimal(row.get(8)))
                .taxes(fetchBigDecimal(row.get(9)))
                .totalPaymentAmount(fetchBigDecimal(row.get(10)))
                .currency(fetchString(row.get(11)))
                .build();

        log.trace("<< map: payment={}", payment);
        return payment;
    }

    private String fetchString(RectangularTextContainer<?> cell) {
        return cell.getText(false);
    }

    private LocalDate fetchLocalDate(RectangularTextContainer<?> cell) {
        return LocalDate.parse(fetchString(cell), PDF_DATE_FORMAT);
    }

    private int fetchInt(RectangularTextContainer<?> cell) {
        return Integer.parseInt(fetchString(cell));
    }

    private BigDecimal fetchBigDecimal(RectangularTextContainer<?> cell) {
        return new BigDecimal(fetchString(cell).replace(',', '.'));
    }

    private String fetchFullRowText(List<RectangularTextContainer<?>> row) {
        return row.stream().map(this::fetchString).collect(Collectors.joining(";"));
    }

}
