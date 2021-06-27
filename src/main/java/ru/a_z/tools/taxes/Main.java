package ru.a_z.tools.taxes;

import lombok.extern.log4j.Log4j2;
import ru.a_z.tools.taxes.exporter.Exporter;
import ru.a_z.tools.taxes.exporter.csv.CsvExporter;
import ru.a_z.tools.taxes.exporter.xlsx.XlsxExporter;
import ru.a_z.tools.taxes.importer.Importer;
import ru.a_z.tools.taxes.importer.pdf.PdfImporter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Основной файл для запуска процесса преобразования форматов
 */
@Log4j2
public class Main {

    private static final String PDF_TYPE = "pdf";
    private static final String CSV_TYPE = "csv";
    private static final String XLSX_TYPE = "xlsx";

    public static void main(String[] args) {
        log.info(">> main: args={}", Arrays.toString(args));

        if (args.length != 2) {
            String message = "Передано некорректное количество аргументов: " + args.length;
            throw new IllegalArgumentException(message);
        }

        String inputPath = args[0];
        String exportType = args[1];

        if (!inputPath.endsWith("." + PDF_TYPE)) {
            String message = "На вход передан файл неизвестного типа: " + inputPath;
            throw new IllegalArgumentException(message);
        } else if (!CSV_TYPE.equals(exportType) && !XLSX_TYPE.equals(exportType)) {
            String message = "Неизвестный тип файла для экспорта: " + exportType;
            throw new IllegalArgumentException(message);
        }

        Path input = Paths.get(inputPath);
        String outputPath = inputPath.substring(0, inputPath.lastIndexOf(PDF_TYPE)) + exportType;
        Path output = Paths.get(outputPath);

        Importer importer = new PdfImporter();
        Exporter exporter = fetchExporter(exportType);

        exporter.execute(importer.execute(input), output);

        log.info("<< main");
    }

    protected static Exporter fetchExporter(String exportType) {
        switch (exportType) {
            case CSV_TYPE:
                return new CsvExporter();
            case XLSX_TYPE:
                return new XlsxExporter();
            default:
                String message = "Нет подходящего экспортера для типа " + exportType;
                throw new IllegalStateException(message);
        }
    }

}
