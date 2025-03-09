package com.personal.amaterasu.ExpenseTracker.controller;

import com.personal.amaterasu.ExpenseTracker.dto.GraphDTO;
import com.personal.amaterasu.ExpenseTracker.dto.StatsDTO;
import com.personal.amaterasu.ExpenseTracker.service.GraphStatsService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/graph-stats")
@RequiredArgsConstructor
@CrossOrigin("*")
public class GraphStatsController {

    private static final Logger LOG = LoggerFactory.getLogger(GraphStatsController.class);

    @Autowired
    private GraphStatsService graphStatsService;

    @GetMapping
    @RequestMapping("/chart")
    public ResponseEntity<GraphDTO> getGraphStatsDetails() {
        return ResponseEntity.ok(graphStatsService.retrieveChartStatsData());
    }

    @GetMapping
    @RequestMapping("/income-expense-stats")
    public ResponseEntity<StatsDTO> getIncomeToExpenseStatsInfo() {
        return ResponseEntity.ok(graphStatsService.getIncomeToExpenseStats());
    }

}
