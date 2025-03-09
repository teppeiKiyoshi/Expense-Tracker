package com.personal.amaterasu.ExpenseTracker.service.impl;

import com.personal.amaterasu.ExpenseTracker.dto.GraphDTO;
import com.personal.amaterasu.ExpenseTracker.dto.StatsDTO;
import com.personal.amaterasu.ExpenseTracker.entity.Expense;
import com.personal.amaterasu.ExpenseTracker.entity.Income;
import com.personal.amaterasu.ExpenseTracker.repository.ExpenseRepository;
import com.personal.amaterasu.ExpenseTracker.repository.IncomeRepository;
import com.personal.amaterasu.ExpenseTracker.service.GraphStatsService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

@Service
@RequiredArgsConstructor
public class GraphStatsServiceImpl implements GraphStatsService {
    private static final Logger LOG = LoggerFactory.getLogger(GraphStatsServiceImpl.class);

    @Autowired
    private final ExpenseRepository expenseRepository;
    @Autowired
    private final IncomeRepository incomeRepository;


    @Override
    public GraphDTO retrieveChartStatsData() {
        final LocalDate startDate = LocalDate.now();
        final LocalDate endDate = startDate.plusDays(120);

        GraphDTO graphDTO = new GraphDTO();
        graphDTO.setExpensesList(expenseRepository.findByDateBetween(startDate, endDate));
        graphDTO.setIncomesList(incomeRepository.findByDateBetween(startDate, endDate));
        return graphDTO;
    }

    @Override
    public StatsDTO getIncomeToExpenseStats() {
        Double totalIncome = incomeRepository.sumAllAmounts();
        Double totalExpense = expenseRepository.sumAllAmounts();

        Optional<Income> incomeOptional = incomeRepository.findFirstByOrderByDateDesc();
        Optional<Expense> expenseOptional = expenseRepository.findFirstByOrderByDateDesc();

        StatsDTO statsDTO = new StatsDTO();
        statsDTO.setExpense(totalExpense);
        statsDTO.setIncome(totalIncome);
        expenseOptional.ifPresent(statsDTO::setLatestExpense);
        incomeOptional.ifPresent(statsDTO::setLatestIncome);

        statsDTO.setBalance(totalIncome - totalExpense);

        List<Income> incomesList = incomeRepository.findAll();
        List<Expense> expensesList = expenseRepository.findAll();

        OptionalDouble minimumIncome = incomesList.stream().mapToDouble(Income::getIncomeAmount).min();
        OptionalDouble maximumIncome = incomesList.stream().mapToDouble(Income::getIncomeAmount).max();

        minimumIncome.ifPresent(statsDTO::setMinIncome);
        maximumIncome.ifPresent(statsDTO::setMaxIncome);

        OptionalDouble minimumExpense = expensesList.stream().mapToDouble(Expense::getAmount).min();
        OptionalDouble maximumExpense = expensesList.stream().mapToDouble(Expense::getAmount).max();

        minimumExpense.ifPresent(statsDTO::setMinExpense);
        maximumExpense.ifPresent(statsDTO::setMaxExpense);

        return statsDTO;
    }
}
