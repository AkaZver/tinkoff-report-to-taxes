package ru.a_z.tools.taxes.importer.pdf;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static ru.a_z.tools.taxes.testutils.ResourceUtils.fetchClassPathResource;

class PdfImporterTest {

    private final PdfImporter importer = new PdfImporter();

    @Test
    @DisplayName("Successful execution")
    void successfulExecution() {
        Path path = fetchClassPathResource("pdf/correct.pdf");
        assertThat(importer.execute(path)).hasSize(52);
    }

    @ParameterizedTest(name = "[{index}] Failed execution with {0}")
    @MethodSource("fetchArgumentsForFailedExecution")
    void failedExecution(Path path, String message) {
        assertThatThrownBy(() -> importer.execute(path))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(message);
    }

    // MethodSource для failedExecution
    @SuppressWarnings("unused")
    private static Stream<Arguments> fetchArgumentsForFailedExecution() {
        Path nonExistent = Paths.get("non-existent");
        Path absoluteEmpty = fetchClassPathResource("pdf/incorrect-absolute-empty.pdf");
        Path empty = fetchClassPathResource("pdf/incorrect-empty.pdf");

        String nonExistentError = "Не удалось обработать файл " + nonExistent;
        String absoluteEmptyError = "Не удалось обработать файл " + absoluteEmpty;
        String emptyError = String.format("Файл %s не содержит записей о выплатах", empty);

        return Stream.of(
                arguments(nonExistent, nonExistentError),
                arguments(absoluteEmpty, absoluteEmptyError),
                arguments(empty, emptyError)
        );
    }

}
