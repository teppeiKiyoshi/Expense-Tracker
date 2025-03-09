package com.personal.amaterasu.ExpenseTracker.controller;

import com.personal.amaterasu.ExpenseTracker.dto.ExpenseDTO;
import com.personal.amaterasu.ExpenseTracker.entity.Expense;
import com.personal.amaterasu.ExpenseTracker.service.ExpenseService;
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
public class ExpenseController {

    private static final Logger LOG = LoggerFactory.getLogger(ExpenseController.class);
    @Autowired
    private ExpenseService expenseService;

    @PostMapping
    @RequestMapping("/create-expense")
    public ResponseEntity<?> createExpense(@RequestBody ExpenseDTO expenseDTO) {
        ResponseEntity<?> responseResult = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        if (expenseService != null) {
            final Expense createdExpense = expenseService.createExpense(expenseDTO);
            if (createdExpense != null) {
                responseResult = ResponseEntity.status(HttpStatus.CREATED).body(createdExpense);
            }
        } else {
            LOG.warn("EXPENSE SERVICE IS NULL FOR CREATE EXPENSE REQUEST.");
        }
        return responseResult;
    }

    @GetMapping
    @RequestMapping("/get-expenses")
    public ResponseEntity<?> getAllExpenses() {
        ResponseEntity<?> responseResult = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        if (expenseService != null) {
            responseResult = ResponseEntity.ok(expenseService.getAllExpenses());
        }
        return responseResult;
    }

    @GetMapping
    @RequestMapping("/get-expense/{id}")
    public ResponseEntity<?> getExpenseById(@PathVariable Long id) {
        ResponseEntity<?> responseResult = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        if (expenseService != null && Objects.nonNull(id)) {
            Expense retrievedExpenseFromDB;
            try {
                retrievedExpenseFromDB = expenseService.findExpenseById(id);
                if (Objects.nonNull(retrievedExpenseFromDB)) {
                    responseResult = ResponseEntity.status(HttpStatus.OK).body(retrievedExpenseFromDB);
                }
            } catch (EntityNotFoundException enfe) {
                LOG.warn("Expense with an ID of [{}] was not found from the database.", id);
                responseResult = ResponseEntity.status(HttpStatus.NOT_FOUND).body(enfe.getMessage());
            } catch (Exception e) {
                LOG.warn("Something went wrong. Please try again later.");
            }
        }
        return responseResult;
    }

    @PutMapping
    @RequestMapping("/update-expense/{id}")
    public ResponseEntity<?> updateExpense(@PathVariable Long id, @RequestBody ExpenseDTO expenseDTO) {
        ResponseEntity<?> responseResult = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        if (expenseService != null && Objects.nonNull(id)) {
            Expense retrievedExpenseFromDB;
            try {
                retrievedExpenseFromDB = expenseService.updateExpense(id, expenseDTO);
                if (Objects.nonNull(retrievedExpenseFromDB)) {
                    responseResult = ResponseEntity.status(HttpStatus.OK).body(retrievedExpenseFromDB);
                }
            } catch (EntityNotFoundException enfe) {
                LOG.warn("Expense with an ID of [{}] was not found from the database.", id);
                responseResult = ResponseEntity.status(HttpStatus.NOT_FOUND).body(enfe.getMessage());
            } catch (Exception e) {
                LOG.warn("Something went wrong. Please try again later.");
            }
        }
        return responseResult;
    }

    @DeleteMapping
    @RequestMapping("/delete-expense/{id}")
    public ResponseEntity<?> updateExpense(@PathVariable Long id) {
        ResponseEntity<?> responseResult = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        if (expenseService != null && Objects.nonNull(id)) {
            try {
                expenseService.deleteExpenseById(id);
                responseResult = ResponseEntity.status(HttpStatus.OK).body("Entity successfully removed from the database.");
            } catch (EntityNotFoundException enfe) {
                LOG.warn("Expense with an ID of [{}] was not found from the database.", id);
                responseResult = ResponseEntity.status(HttpStatus.NOT_FOUND).body(enfe.getMessage());
            } catch (Exception e) {
                LOG.warn("Something went wrong. Please try again later.");
            }
        }
        return responseResult;
    }

}
