package ru.a_z.tools.taxes.exporter.xlsx;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.dhatim.fastexcel.Worksheet;
import ru.a_z.tools.taxes.exporter.ExporterField;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

/**
 * Обёртка над страницей XLSX для более удобной работы с {@link Worksheet}
 */
@Getter
@Log4j2
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class WorksheetWrapper {

    private final Worksheet ws;
    private int row = 0;
    private int col = 0;

    public static WorksheetWrapper with(Worksheet ws) {
        return new WorksheetWrapper(ws);
    }

    protected WorksheetWrapper value(Object object, Style style) {
        log.trace(">> value: object={}, style={}", object, style);
        style.getValue().accept(ws, row, col);

        if (object instanceof String) {
            ws.value(row, col, (String) object);
        } else if (object instanceof Number) {
            ws.value(row, col, (Number) object);
        } else if (object instanceof LocalDate) {
            ws.value(row, col, (LocalDate) object);
        } else if (object instanceof ExporterField) {
            ws.value(row, col, ((ExporterField) object).getValue());
        } else {
            throw new IllegalArgumentException("Неизвестный тип объекта: " + object.getClass().getName());
        }

        ++col;
        log.trace("<< value: this={}", this);
        return this;
    }

    public WorksheetWrapper formula(String text) {
        log.trace(">> formula: text={}", text);

        Style.FORMULA.getValue().accept(ws, row, col);
        ws.formula(row, col, text);
        ++col;

        log.trace("<< formula: this={}", this);
        return this;
    }

    public WorksheetWrapper headers(ExporterField[] exporterFields) {
        for (ExporterField exporterField : exporterFields) {
            value(exporterField, Style.HEADER);
        }

        return this;
    }

    public WorksheetWrapper date(Supplier<LocalDate> accessor) {
        return value(accessor.get(), Style.DATE);
    }

    public WorksheetWrapper integer(IntSupplier accessor) {
        return value(accessor.getAsInt(), Style.INTEGER);
    }

    public WorksheetWrapper string(Supplier<String> accessor) {
        return value(accessor.get(), Style.STRING);
    }

    public WorksheetWrapper bigDecimal(Supplier<BigDecimal> accessor) {
        return value(accessor.get(), Style.BIG_DECIMAL);
    }

    public WorksheetWrapper firstColumn() {
        col = 0;
        return this;
    }

    public WorksheetWrapper nextRow() {
        ++row;
        return this;
    }

    public void finish() {
        ws.setFitToPage(true);
    }

}
