package ru.a_z.tools.taxes.exporter;

import ru.a_z.tools.taxes.model.DividendPayment;

import java.nio.file.Path;
import java.util.List;

/**
 * Интерфейс для экспортеров в различные форматы
 */
public interface Exporter {

    void execute(List<DividendPayment> dividendPayments, Path output);

}
