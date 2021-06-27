package ru.a_z.tools.taxes;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static ru.a_z.tools.taxes.testutils.ResourceUtils.fetchClassPathResource;

class MainTest {

    @ParameterizedTest(name = "[{index}] Correct args: {0}")
    @MethodSource("fetchCorrectArguments")
    void correctArgs(String inputPath, String exportType) {
        String[] args = new String[]{inputPath, exportType};
        assertDoesNotThrow(() -> Main.main(args));
    }

    @ParameterizedTest(name = "[{index}] Incorrect args: {0}")
    @MethodSource("fetchIncorrectArguments")
    void incorrectArgs(String[] args, String message) {
        assertThatThrownBy(() -> Main.main(args))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(message);
    }

    @Test
    @DisplayName("Incorrect exporter")
    void incorrectExporter() {
        assertThatThrownBy(() -> Main.fetchExporter("test"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Нет подходящего экспортера для типа test");
    }

    // MethodSource для correctArgs
    @SuppressWarnings("unused")
    private static Stream<Arguments> fetchCorrectArguments() {
        String inputPath = fetchClassPathResource("pdf/correct.pdf").toString();

        return Stream.of(
                arguments(inputPath, "csv"),
                arguments(inputPath, "xlsx")
        );
    }

    // MethodSource для incorrectArgs
    @SuppressWarnings("unused")
    private static Stream<Arguments> fetchIncorrectArguments() {
        return Stream.of(
                arguments(new String[]{}, "Передано некорректное количество аргументов: 0"),
                arguments(new String[]{"input.qwe", "output.csv"}, "На вход передан файл неизвестного типа: input.qwe"),
                arguments(new String[]{"input.pdf", "output.qwe"}, "Неизвестный тип файла для экспорта: output.qwe")
        );
    }

}
