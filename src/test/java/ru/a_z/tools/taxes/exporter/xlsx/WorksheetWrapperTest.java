package ru.a_z.tools.taxes.exporter.xlsx;

import org.dhatim.fastexcel.Workbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.a_z.tools.taxes.exporter.ExporterField;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class WorksheetWrapperTest {

    private WorksheetWrapper wrapper;

    @BeforeEach
    void beforeEach(@TempDir Path tempDir) throws IOException {
        try (OutputStream outputStream = Files.newOutputStream(tempDir.resolve("test-worksheet-wrapper.xlsx"))) {
            Workbook workbook = new Workbook(outputStream, "Test app", null);
            wrapper = WorksheetWrapper.with(workbook.newWorksheet("Test worksheet"));
        }
    }

    @Test
    @DisplayName("Incorrect object type")
    void incorrectObjectType() {
        Object object = new Object();

        assertThatThrownBy(() -> wrapper.value(object, Style.STRING))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Неизвестный тип объекта: java.lang.Object");
    }

    @ParameterizedTest(name = "[{index}] Correct object type {0}")
    @MethodSource("fetchCorrectObjectTypes")
    void correctObjectTypes(Object object, Style style) {
        assertDoesNotThrow(() -> wrapper.value(object, style));
        assertThat(wrapper.getWs().value(0, 0)).isNotNull();
    }

    // MethodSource для correctObjectTypes
    @SuppressWarnings("unused")
    private static Stream<Arguments> fetchCorrectObjectTypes() {
        return Stream.of(
                arguments("test", Style.STRING),
                arguments(BigDecimal.ZERO, Style.BIG_DECIMAL),
                arguments(LocalDate.now(), Style.DATE),
                arguments(ExporterField.NAME_OF_SECURITY, Style.HEADER)
        );
    }

}
