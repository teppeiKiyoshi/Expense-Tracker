package com.personal.amaterasu.ExpenseTracker.service;

import com.personal.amaterasu.ExpenseTracker.dto.ExpenseDTO;
import com.personal.amaterasu.ExpenseTracker.entity.Expense;

import java.util.List;

public interface ExpenseService {

    List<Expense> getAllExpenses();
    Expense createExpense(final ExpenseDTO expenseDTO);
    Expense updateExpense(final Long expenseId, final ExpenseDTO expenseDTO);
    Expense findExpenseById(final Long expenseId);
    void deleteExpenseById(final Long expenseId);

}
