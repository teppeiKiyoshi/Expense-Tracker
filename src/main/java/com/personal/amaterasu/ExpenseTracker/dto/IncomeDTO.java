package com.personal.amaterasu.ExpenseTracker.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class IncomeDTO {

    private long incomeId;
    private String incomeTitle;
    private Integer incomeAmount;
    private LocalDate date;
    private String incomeCategory;
    private String incomeDescription;

    public IncomeDTO getIncomeDto() {
        IncomeDTO incomeDTO = new IncomeDTO();

        incomeDTO.setIncomeId(incomeId);
        incomeDTO.setIncomeAmount(incomeAmount);
        incomeDTO.setDate(date);
        incomeDTO.setIncomeCategory(incomeCategory);
        incomeDTO.setIncomeDescription(incomeDescription);

        return incomeDTO;
    }
}
