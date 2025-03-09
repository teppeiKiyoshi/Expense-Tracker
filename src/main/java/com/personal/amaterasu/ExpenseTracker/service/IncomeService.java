package com.personal.amaterasu.ExpenseTracker.service;

import com.personal.amaterasu.ExpenseTracker.dto.ExpenseDTO;
import com.personal.amaterasu.ExpenseTracker.dto.IncomeDTO;
import com.personal.amaterasu.ExpenseTracker.entity.Expense;
import com.personal.amaterasu.ExpenseTracker.entity.Income;

import java.util.List;

public interface IncomeService {

    List<Income> getAllIncomes();
    Income findExpenseById(final Long incomeId);
    Income createIncome(final IncomeDTO incomeDTO);
    Income updateIncome(final Long incomeId, final IncomeDTO incomeDTO);
    void deleteExpenseById(final Long expenseId);
}
