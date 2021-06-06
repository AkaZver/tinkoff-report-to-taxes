package ru.a_z.tools.taxes.exporter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Набор полей для экспортеров
 */
@Getter
@RequiredArgsConstructor
public enum ExporterField {

    REGISTRY_FIX_DATE("Дата фиксации реестра"),
    PAYMENT_DATE("Дата выплаты"),
    PAYMENT_TYPE("Тип выплаты"),
    NAME_OF_SECURITY("Наименование ценной бумаги"),
    ISIN("ISIN"),
    ISSUER_COUNTRY("Страна эмитента"),
    NUMBER_OF_SECURITIES("Количество ценных бумаг"),
    PAYMENT_PER_PAPER("Выплата на одну бумагу"),
    PAYMENTS_WITH_TAXES("Сумма выплат с налогом"),
    COMMISSION("Комиссия внешних платежных агентов"),
    TAXES("Сумма налога, удержанного агентом"),
    TAX_PERCENT("Процент налога"),
    TOTAL_PAYMENT_AMOUNT("Итоговая сумма выплаты"),
    CURRENCY("Валюта");

    private final String value;

}
