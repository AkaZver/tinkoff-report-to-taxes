package ru.a_z.tools.taxes.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Представление объекта дивидендной выплаты из PDF
 */
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DividendPayment {

    private LocalDate registryFixDate;
    private LocalDate paymentDate;
    private int paymentType;
    private String nameOfSecurity;
    private String isin;
    private String issuerCountry;
    private BigDecimal numberOfSecurities;
    private BigDecimal paymentPerPaper;
    private BigDecimal commission;
    private BigDecimal amountBeforeTax;
    private BigDecimal taxes;
    private BigDecimal totalPaymentAmount;
    private String currency;

}

