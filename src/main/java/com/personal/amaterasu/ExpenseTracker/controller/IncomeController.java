package com.personal.amaterasu.ExpenseTracker.controller;

import com.personal.amaterasu.ExpenseTracker.dto.IncomeDTO;
import com.personal.amaterasu.ExpenseTracker.entity.Income;
import com.personal.amaterasu.ExpenseTracker.service.IncomeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/income-tracker")
@RequiredArgsConstructor
@CrossOrigin("*")
public class IncomeController {

    private static final Logger LOG = LoggerFactory.getLogger(IncomeController.class);

    @Autowired
    private IncomeService incomeService;

    @PostMapping
    @RequestMapping("/create-income")
    public ResponseEntity<?> createIncome(@RequestBody IncomeDTO incomeDTO) {
        ResponseEntity<?> responseResult = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        if (incomeService != null) {
            final Income createdIncome = incomeService.createIncome(incomeDTO);
            if (Objects.nonNull(createdIncome)) {
                responseResult = ResponseEntity.status(HttpStatus.CREATED).body(createdIncome);
            }
        } else {
            LOG.warn("INCOME SERVICE IS NULL FOR CREATE EXPENSE REQUEST.");
        }
        return responseResult;
    }

    @GetMapping
    @RequestMapping("/get-incomes")
    public ResponseEntity<?> getAllIncomes() {
        ResponseEntity<?> responseResult = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        if (incomeService != null) {
            responseResult = ResponseEntity.ok(incomeService.getAllIncomes());
        }
        return responseResult;
    }

    @GetMapping
    @RequestMapping("/get-income/{id}")
    public ResponseEntity<?> getIncomeById(@PathVariable Long id) {
        ResponseEntity<?> responseResult = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        if (incomeService != null && Objects.nonNull(id)) {
            Income retrievedIncomeFromDB;
            try {
                retrievedIncomeFromDB = incomeService.findExpenseById(id);
                if (Objects.nonNull(retrievedIncomeFromDB)) {
                    responseResult = ResponseEntity.status(HttpStatus.OK).body(retrievedIncomeFromDB);
                }
            } catch (EntityNotFoundException enfe) {
                LOG.warn("Income with an ID of [{}] was not found from the database.", id);
                responseResult = ResponseEntity.status(HttpStatus.NOT_FOUND).body(enfe.getMessage());
            } catch (Exception e) {
                LOG.warn("Something went wrong. Please try again later.");
            }
        }
        return responseResult;
    }

    @PutMapping
    @RequestMapping("/update-income/{id}")
    public ResponseEntity<?> updateIncome(@PathVariable Long id, @RequestBody IncomeDTO incomeDTO) {
        ResponseEntity<?> responseResult = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        if (Objects.nonNull(incomeService) && Objects.nonNull(id)) {
            Income retrievedIncomeFromDB;
            try {
                retrievedIncomeFromDB = incomeService.updateIncome(id, incomeDTO);
                if (Objects.nonNull(retrievedIncomeFromDB)) {
                    responseResult = ResponseEntity.status(HttpStatus.OK).body(retrievedIncomeFromDB);
                }
            } catch (EntityNotFoundException enfe) {
                LOG.warn("Income with an ID of [{}] was not found from the database.", id);
                responseResult = ResponseEntity.status(HttpStatus.NOT_FOUND).body(enfe.getMessage());
            } catch (Exception e) {
                LOG.warn("Something went wrong. Please try again later.");
            }
        }
        return responseResult;
    }

    @DeleteMapping
    @RequestMapping("/delete-income/{id}")
    public ResponseEntity<?> updateExpense(@PathVariable Long id) {
        ResponseEntity<?> responseResult = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        if (incomeService != null && Objects.nonNull(id)) {
            try {
                incomeService.deleteExpenseById(id);
                responseResult = ResponseEntity.status(HttpStatus.OK).body("Entity successfully removed from the database.");
            } catch (EntityNotFoundException enfe) {
                LOG.warn("Income with an ID of [{}] was not found from the database.", id);
                responseResult = ResponseEntity.status(HttpStatus.NOT_FOUND).body(enfe.getMessage());
            } catch (Exception e) {
                LOG.warn("Something went wrong. Please try again later.");
            }
        }
        return responseResult;
    }

}
