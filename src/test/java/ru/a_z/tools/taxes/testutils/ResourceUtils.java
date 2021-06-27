package ru.a_z.tools.taxes.testutils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.dhatim.fastexcel.reader.*;

import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
public class ResourceUtils {

    @SneakyThrows
    public static Path fetchClassPathResource(String name) {
        URI uri = Objects
                .requireNonNull(ResourceUtils.class.getResource("/" + name))
                .toURI();

        return Paths.get(uri);
    }

    @SneakyThrows
    public static List<String> fetchCsvContent(Path path) {
        return Files.readAllLines(path, Charset.forName("CP1251"));
    }

    @SneakyThrows
    public static List<String> fetchXlsxContent(Path path) {
        try (ReadableWorkbook readableWorkbook = new ReadableWorkbook(path.toFile())) {
            return readableWorkbook
                    .getSheets()
                    .flatMap(ResourceUtils::fetchRows)
                    .flatMap(Row::stream)
                    .map(ResourceUtils::fetchCellText)
                    .collect(Collectors.toList());
        }
    }

    @SneakyThrows
    private static Stream<Row> fetchRows(Sheet sheet) {
        return sheet.openStream();
    }

    private static String fetchCellText(Cell cell) {
        return CellType.FORMULA == cell.getType()
                ? cell.getFormula()
                : cell.getText();
    }

}
