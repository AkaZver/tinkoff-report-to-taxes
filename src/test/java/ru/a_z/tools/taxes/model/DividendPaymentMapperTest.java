package ru.a_z.tools.taxes.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import technology.tabula.Cell;
import technology.tabula.RectangularTextContainer;
import technology.tabula.TextChunk;
import technology.tabula.TextElement;

import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class DividendPaymentMapperTest {

    private final DividendPaymentMapper mapper = new DividendPaymentMapper();

    @ParameterizedTest(name = "[{index}] Incorrect row with {1}")
    @MethodSource("fetchIncorrectRows")
    void incorrectRows(List<RectangularTextContainer<?>> row, Class<RuntimeException> exception) {
        assertThrows(exception, () -> mapper.map(row));
    }

    // MethodSource для incorrectRows
    @SuppressWarnings("unused")
    private static Stream<Arguments> fetchIncorrectRows() {
        List<RectangularTextContainer<?>> incorrectDate = fetchIncorrectRow(0, "32.01.2021");
        List<RectangularTextContainer<?>> incorrectInteger = fetchIncorrectRow(2, "test");
        List<RectangularTextContainer<?>> incorrectDecimal = fetchIncorrectRow(7, "0/00");

        List<RectangularTextContainer<?>> incorrectRowSize = Stream
                .generate(() -> new Cell(1, 1, 1, 1))
                .limit(13)
                .collect(Collectors.toList());

        return Stream.of(
                arguments(incorrectDate, DateTimeParseException.class),
                arguments(incorrectInteger, NumberFormatException.class),
                arguments(incorrectDecimal, NumberFormatException.class),
                arguments(incorrectRowSize, IllegalArgumentException.class)
        );
    }

    private static List<RectangularTextContainer<?>> fetchIncorrectRow(int index, String value) {
        List<RectangularTextContainer<?>> correctRow = fetchRow(
                "01.01.2021", "02.02.2021", "test", "Stock 1", "ISIN 1",
                "США", "1", "1.00", "0.00", "0.00", "0.00", "USD"
        );

        correctRow.set(index, fetchCell(value));
        return correctRow;
    }

    private static List<RectangularTextContainer<?>> fetchRow(String... strings) {
        return Arrays
                .stream(strings)
                .map(DividendPaymentMapperTest::fetchCell)
                .collect(Collectors.toList());
    }

    private static RectangularTextContainer<?> fetchCell(String string) {
        TextElement textElement = new TextElement(1, 1, 1, 1, null, 1, string, 1);
        TextChunk textChunk = new TextChunk(textElement);
        Cell cell = new Cell(1, 1, 1, 1);
        cell.setTextElements(Collections.singletonList(textChunk));

        return cell;
    }

}
