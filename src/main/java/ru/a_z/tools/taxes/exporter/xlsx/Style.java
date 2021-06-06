package ru.a_z.tools.taxes.exporter.xlsx;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.dhatim.fastexcel.BorderStyle;
import org.dhatim.fastexcel.Color;
import org.dhatim.fastexcel.Worksheet;

/**
 * Стили для ячеек XLSX
 */
@Getter
@ToString
@RequiredArgsConstructor
public enum Style {

    HEADER((ws, row, col) -> ws
            .style(row, col)
            .borderStyle(BorderStyle.THIN)
            .horizontalAlignment("center")
            .verticalAlignment("center")
            .fillColor(Color.GRAY2)
            .wrapText(true)
            .bold()
            .set()
    ),

    DATE((ws, row, col) -> ws
            .style(row, col)
            .borderStyle(BorderStyle.THIN)
            .horizontalAlignment("center")
            .verticalAlignment("center")
            .format("dd.MM.yyyy")
            .set()
    ),

    INTEGER((ws, row, col) -> ws
            .style(row, col)
            .borderStyle(BorderStyle.THIN)
            .horizontalAlignment("right")
            .verticalAlignment("center")
            .set()),

    BIG_DECIMAL((ws, row, col) -> ws
            .style(row, col)
            .borderStyle(BorderStyle.THIN)
            .horizontalAlignment("right")
            .verticalAlignment("center")
            .set()),

    STRING((ws, row, col) -> ws
            .style(row, col)
            .borderStyle(BorderStyle.THIN)
            .horizontalAlignment("left")
            .verticalAlignment("center")
            .set()),

    FORMULA_WITH_NUMBER((ws, row, col) -> ws
            .style(row, col)
            .borderStyle(BorderStyle.THIN)
            .horizontalAlignment("right")
            .verticalAlignment("center")
            .format("0.00")
            .set()),

    FORMULA_WITH_PERCENT((ws, row, col) -> ws
            .style(row, col)
            .borderStyle(BorderStyle.THIN)
            .horizontalAlignment("right")
            .verticalAlignment("center")
            .format("0.00%")
            .set());

    private final StyleConsumer<Worksheet, Integer, Integer> value;

    @FunctionalInterface
    interface StyleConsumer<A, B, C> {
        void accept(A a, B b, C c);
    }

}
