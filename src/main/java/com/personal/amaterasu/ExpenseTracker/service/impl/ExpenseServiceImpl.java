package com.personal.amaterasu.ExpenseTracker.service.impl;

import com.personal.amaterasu.ExpenseTracker.dto.ExpenseDTO;
import com.personal.amaterasu.ExpenseTracker.entity.Expense;
import com.personal.amaterasu.ExpenseTracker.repository.ExpenseRepository;
import com.personal.amaterasu.ExpenseTracker.service.ExpenseService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {
    private static final Logger LOG = LoggerFactory.getLogger(ExpenseServiceImpl.class);

    @Autowired
    private final ExpenseRepository expenseRepository;

    @Override
    public Expense updateExpense(final Long expenseId, final ExpenseDTO expenseDTO) {
        return updateExistingExpense(expenseId, expenseDTO);
    }

    @Override
    public Expense findExpenseById(final Long expenseId) {
        return getExpenseReferencedViaId(expenseId);
    }

    @Override
    public void deleteExpenseById(final Long expenseId) {
        deleteExpenseViaIdReference(expenseId);
    }

    @Override
    public List<Expense> getAllExpenses() {
        return getAndSortAllExpenses();
    }

    @Override
    public Expense createExpense(final ExpenseDTO expenseDTO) {
        return saveActualExpense(null, expenseDTO);
    }

    private void deleteExpenseViaIdReference(final Long expenseId) {
        final Optional<Expense> expenseToRemove = expenseRepository.findById(expenseId);
        if (expenseToRemove.isPresent()){
            expenseRepository.deleteById(expenseId);
        } else {
            throw new EntityNotFoundException("Expense with an ID of "+ expenseId + " is not existing. Unable delete entity.");
        }
    }

    private Expense getExpenseReferencedViaId(final Long expenseId) {
        final Optional<Expense> retrievedExpense = expenseRepository.findById(expenseId);
        if (retrievedExpense.isPresent()) {
            return retrievedExpense.get();
        }
        throw new EntityNotFoundException("Expense does not exist on the database for ID " + expenseId);
    }

    private Expense updateExistingExpense(final Long expenseId, final ExpenseDTO expenseDTO) {
        final Expense expenseToUpdate = getExpenseReferencedViaId(expenseId);
        return saveActualExpense(expenseToUpdate, expenseDTO);
    }

    private List<Expense> getAndSortAllExpenses(){
        return expenseRepository.findAll().stream()
                .filter(exp -> Objects.nonNull(exp.getDate()))
                .sorted(Comparator.comparing(Expense::getDate).reversed())
                .collect(Collectors.toList());
    }

    private Expense saveActualExpense(final Expense expenseToBuild, final ExpenseDTO expenseDTO) {
        final Expense expenseToSave = buildExpenseFromDTO(expenseToBuild, expenseDTO);
        if (Objects.nonNull(expenseToSave)) {
            return expenseRepository.save(expenseToSave);
        }
        throw new IllegalArgumentException("Failed to create expense because attributes are null");
    }

    private Expense buildExpenseFromDTO(final Expense expenseForUpdate, final ExpenseDTO expenseDTO) {
        if (Objects.isNull(expenseDTO) ||((Objects.isNull(expenseDTO.getTitle()) || Objects.isNull(expenseDTO.getAmount())))) {
            LOG.warn("Current request to create expense [{}] is not possible due to null values", expenseDTO);
            throw new IllegalArgumentException("Unable to build expense dto due to null values.");
        }

        Expense.ExpenseBuilder builder = Objects.nonNull(expenseForUpdate) ?
                expenseForUpdate.toBuilder() : Expense.builder();

        return builder
                .title(expenseDTO.getTitle())
                .description(expenseDTO.getDescription())
                .category(expenseDTO.getCategory())
                .date(expenseDTO.getDate())
                .amount(expenseDTO.getAmount())
                .build();
    }
}
