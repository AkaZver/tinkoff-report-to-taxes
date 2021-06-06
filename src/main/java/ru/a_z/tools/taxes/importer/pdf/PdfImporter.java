package ru.a_z.tools.taxes.importer.pdf;

import lombok.extern.log4j.Log4j2;
import org.apache.pdfbox.pdmodel.PDDocument;
import ru.a_z.tools.taxes.importer.Importer;
import ru.a_z.tools.taxes.model.DividendPayment;
import ru.a_z.tools.taxes.model.DividendPaymentMapper;
import technology.tabula.ObjectExtractor;
import technology.tabula.Page;
import technology.tabula.RectangularTextContainer;
import technology.tabula.extractors.ExtractionAlgorithm;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Импортер из отчёта Тинькофф в формате PDF.
 * Используется библиотека <a href="https://github.com/tabulapdf/tabula-java">tabula-java</a>
 */
@Log4j2
public class PdfImporter implements Importer {

    private static final String PDF_DATE_REGEX = "^\\d{2}\\.\\d{2}\\.\\d{4}$";

    public List<DividendPayment> execute(Path input) {
        log.info(">> execute: input={}", input);
        List<DividendPayment> payments;

        try (PDDocument document = PDDocument.load(input.toFile())) {
            Predicate<List<RectangularTextContainer<?>>> isRowCorrect = cells -> cells.get(0).getText().matches(PDF_DATE_REGEX);
            DividendPaymentMapper mapper = new DividendPaymentMapper();
            ExtractionAlgorithm algorithm = new SpreadsheetExtractionAlgorithm();
            Iterable<Page> pages = () -> new ObjectExtractor(document).extract();

            payments = StreamSupport.stream(pages.spliterator(), false)
                    .flatMap(page -> algorithm.extract(page).stream())
                    .flatMap(table -> table.getRows().stream())
                    .map(this::fetchCheckedRow)
                    .filter(isRowCorrect)
                    .map(mapper::map)
                    .collect(Collectors.toList());
        } catch (IOException exception) {
            String message = "Не удалось обработать файл " + input;
            throw new RuntimeException(message, exception);
        }

        log.debug("-- execute: payments={}", payments);
        log.info("<< execute: paymentsSize={}", payments.size());
        return payments;
    }

    @SuppressWarnings("rawtypes")
    private List<RectangularTextContainer<?>> fetchCheckedRow(List<RectangularTextContainer> cells) {
        return cells.stream().map(x -> (RectangularTextContainer<?>) x).collect(Collectors.toList());
    }

}
