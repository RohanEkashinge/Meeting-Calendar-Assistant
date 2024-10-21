package com.example.Meeting_Calendar_Assistant.controller;

import com.example.Meeting_Calendar_Assistant.model.Employee;
import com.example.Meeting_Calendar_Assistant.service.EmployeeService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;

/**
 * REST controller for managing Employee entities.
 */
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    /**
     * Creates a new employee.
     *
     * @param employee The employee data to be created.
     * @return ResponseEntity with a success message.
     */
    @PostMapping
    public ResponseEntity<String> createEmployee(@RequestBody Employee employee) {
        logger.info("Received request to create employee: {}", employee.getName());
        employeeService.saveEmployee(employee);
        logger.info("Employee created successfully: {}", employee.getName());
        return ResponseEntity.ok("Employee created successfully.");
    }
}
