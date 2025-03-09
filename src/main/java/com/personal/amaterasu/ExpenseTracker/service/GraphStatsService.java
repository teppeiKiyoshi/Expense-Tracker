package com.personal.amaterasu.ExpenseTracker.service;

import com.personal.amaterasu.ExpenseTracker.dto.GraphDTO;
import com.personal.amaterasu.ExpenseTracker.dto.StatsDTO;

public interface GraphStatsService {

    GraphDTO retrieveChartStatsData();
    StatsDTO getIncomeToExpenseStats();

}
