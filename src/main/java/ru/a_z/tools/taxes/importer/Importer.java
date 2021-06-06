package ru.a_z.tools.taxes.importer;

import ru.a_z.tools.taxes.model.DividendPayment;

import java.nio.file.Path;
import java.util.List;

/**
 * Интерфейс для импортеров из различных форматов
 */
public interface Importer {

    List<DividendPayment> execute(Path input);

}
