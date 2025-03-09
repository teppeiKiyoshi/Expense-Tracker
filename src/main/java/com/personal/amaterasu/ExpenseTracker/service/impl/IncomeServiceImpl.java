package com.personal.amaterasu.ExpenseTracker.service.impl;

import com.personal.amaterasu.ExpenseTracker.dto.ExpenseDTO;
import com.personal.amaterasu.ExpenseTracker.dto.IncomeDTO;
import com.personal.amaterasu.ExpenseTracker.entity.Expense;
import com.personal.amaterasu.ExpenseTracker.entity.Income;
import com.personal.amaterasu.ExpenseTracker.repository.IncomeRepository;
import com.personal.amaterasu.ExpenseTracker.service.IncomeService;
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
public class IncomeServiceImpl implements IncomeService {
    private static final Logger LOG = LoggerFactory.getLogger(IncomeServiceImpl.class);

    @Autowired
    private final IncomeRepository incomeRepository;

    @Override
    public List<Income> getAllIncomes() {
        return getAndSortAllExpenses();
    }

    @Override
    public Income findExpenseById(final Long expenseId) {
        return getIncomeReferencedViaId(expenseId);
    }

    @Override
    public Income createIncome(final IncomeDTO incomeDTO) {
        return saveActualIncome(null, incomeDTO);
    }

    @Override
    public Income updateIncome(final Long incomeId, final IncomeDTO incomeDTO) {
        return updateExistingExpense(incomeId, incomeDTO);
    }

    @Override
    public void deleteExpenseById(Long expenseId) {
        deleteIncomeViaIdReference(expenseId);
    }

    private void deleteIncomeViaIdReference(final Long incomeId) {
        final Optional<Income> expenseToRemove = incomeRepository.findById(incomeId);
        if (expenseToRemove.isPresent()){
            incomeRepository.deleteById(incomeId);
        } else {
            throw new EntityNotFoundException("Expense with an ID of "+ incomeId + " is not existing. Unable delete entity.");
        }
    }

    private List<Income> getAndSortAllExpenses(){
        return incomeRepository.findAll().stream()
                .filter(income -> Objects.nonNull(income.getDate()))
                .sorted(Comparator.comparing(Income::getDate).reversed())
                .collect(Collectors.toList());
    }

    private Income updateExistingExpense(final Long incomeId, final IncomeDTO incomeDTO) {
        final Income incomeToUpdate = getIncomeReferencedViaId(incomeId);
        return saveActualIncome(incomeToUpdate, incomeDTO);
    }

    private Income getIncomeReferencedViaId(final Long incomeId) {
        final Optional<Income> retrievedIncome = incomeRepository.findById(incomeId);
        if (retrievedIncome.isPresent()) {
            return retrievedIncome.get();
        }
        throw new EntityNotFoundException("Expense does not exist on the database for ID " + incomeId);
    }

    private Income saveActualIncome(final Income incomeToBuild, final IncomeDTO incomeDTO) {
        final Income incomeToSave = buildIncomeFromDTO(incomeToBuild, incomeDTO);
        if (Objects.nonNull(incomeToSave)) {
            return incomeRepository.save(incomeToSave);
        }
        throw new IllegalArgumentException("Failed to create expense because attributes are null");
    }

    private Income buildIncomeFromDTO(final Income incomeForUpdate, final IncomeDTO incomeDTO) {
        if (Objects.isNull(incomeDTO) ||((Objects.isNull(incomeDTO.getIncomeTitle()) || Objects.isNull(incomeDTO.getIncomeAmount())))) {
            LOG.warn("Current request to create expense [{}] is not possible due to null values", incomeDTO);
            throw new IllegalArgumentException("Unable to build expense dto due to null values.");
        }

        Income.IncomeBuilder builder = Objects.nonNull(incomeForUpdate) ?
                incomeForUpdate.toBuilder() : Income.builder();

        return builder
                .incomeTitle(incomeDTO.getIncomeTitle())
                .incomeAmount(incomeDTO.getIncomeAmount())
                .date(incomeDTO.getDate())
                .incomeCategory(incomeDTO.getIncomeCategory())
                .incomeDescription(incomeDTO.getIncomeDescription())
                .build();
    }


}
