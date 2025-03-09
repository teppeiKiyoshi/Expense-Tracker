package com.personal.amaterasu.ExpenseTracker.dto;

import com.personal.amaterasu.ExpenseTracker.entity.Expense;
import com.personal.amaterasu.ExpenseTracker.entity.Income;
import lombok.Data;

import java.util.List;

@Data
public class GraphDTO {

    private List<Expense> expensesList;

    private List<Income> incomesList;

}
