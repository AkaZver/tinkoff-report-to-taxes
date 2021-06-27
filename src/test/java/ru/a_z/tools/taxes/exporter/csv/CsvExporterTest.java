package ru.a_z.tools.taxes.exporter.csv;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.a_z.tools.taxes.importer.pdf.PdfImporter;
import ru.a_z.tools.taxes.model.DividendPayment;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ru.a_z.tools.taxes.testutils.ResourceUtils.fetchClassPathResource;
import static ru.a_z.tools.taxes.testutils.ResourceUtils.fetchCsvContent;

class CsvExporterTest {

    private final CsvExporter exporter = new CsvExporter();

    @Test
    @DisplayName("Successful execution")
    void successfulExecution(@TempDir Path tempDir) {
        List<DividendPayment> payments = new PdfImporter().execute(fetchClassPathResource("pdf/correct.pdf"));
        Path actualOutput = tempDir.resolve("test-export.csv");
        Path expectedOutput = fetchClassPathResource("csv/correct.csv");

        assertDoesNotThrow(() -> exporter.execute(payments, actualOutput));

        List<String> actualContent = fetchCsvContent(actualOutput);
        List<String> expectedContent = fetchCsvContent(expectedOutput);

        assertThat(actualContent).containsExactlyElementsOf(expectedContent);
    }

    @Test
    @DisplayName("Failed execution")
    void failedExecution() {
        Path brokenPath = mock(Path.class);
        List<DividendPayment> payments = Collections.emptyList();

        when(brokenPath.getFileSystem())
                .thenAnswer(x -> {
                    throw new IOException("test exception");
                });

        assertThatThrownBy(() -> exporter.execute(payments, brokenPath))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Не удалось обработать CSV-файл %s", brokenPath);
    }

}
