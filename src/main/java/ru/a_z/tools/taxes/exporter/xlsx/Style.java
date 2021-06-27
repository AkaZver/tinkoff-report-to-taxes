package ru.a_z.tools.taxes.exporter.xlsx;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import org.dhatim.fastexcel.BorderStyle;
import org.dhatim.fastexcel.Color;
import org.dhatim.fastexcel.Worksheet;

import static ru.a_z.tools.taxes.exporter.xlsx.Style.Constant.*;

/**
 * Стили для ячеек XLSX
 */
@Getter
@RequiredArgsConstructor
public enum Style {

    HEADER((ws, row, col) -> ws
            .style(row, col)
            .borderStyle(BorderStyle.THIN)
            .horizontalAlignment(ALIGN_CENTER)
            .verticalAlignment(ALIGN_CENTER)
            .fillColor(Color.GRAY2)
            .wrapText(true)
            .bold()
            .set()
    ),

    DATE((ws, row, col) -> ws
            .style(row, col)
            .borderStyle(BorderStyle.THIN)
            .horizontalAlignment(ALIGN_CENTER)
            .verticalAlignment(ALIGN_CENTER)
            .format("dd.MM.yyyy")
            .set()
    ),

    INTEGER((ws, row, col) -> ws
            .style(row, col)
            .borderStyle(BorderStyle.THIN)
            .horizontalAlignment(ALIGN_RIGHT)
            .verticalAlignment(ALIGN_CENTER)
            .set()),

    BIG_DECIMAL((ws, row, col) -> ws
            .style(row, col)
            .borderStyle(BorderStyle.THIN)
            .horizontalAlignment(ALIGN_RIGHT)
            .verticalAlignment(ALIGN_CENTER)
            .set()),

    STRING((ws, row, col) -> ws
            .style(row, col)
            .borderStyle(BorderStyle.THIN)
            .horizontalAlignment(ALIGN_LEFT)
            .verticalAlignment(ALIGN_CENTER)
            .set()),

    FORMULA_WITH_NUMBER((ws, row, col) -> ws
            .style(row, col)
            .borderStyle(BorderStyle.THIN)
            .horizontalAlignment(ALIGN_RIGHT)
            .verticalAlignment(ALIGN_CENTER)
            .format("0.00")
            .set()),

    FORMULA_WITH_PERCENT((ws, row, col) -> ws
            .style(row, col)
            .borderStyle(BorderStyle.THIN)
            .horizontalAlignment(ALIGN_RIGHT)
            .verticalAlignment(ALIGN_CENTER)
            .format("0.00%")
            .set());

    private final StyleConsumer<Worksheet, Integer, Integer> value;

    @FunctionalInterface
    interface StyleConsumer<A, B, C> {
        void accept(A a, B b, C c);
    }

    @UtilityClass
    protected static class Constant {
        protected static final String ALIGN_CENTER = "center";
        protected static final String ALIGN_RIGHT = "right";
        protected static final String ALIGN_LEFT = "left";
    }

}
