package com.example.Meeting_Calendar_Assistant.service.serviceImpl;

import com.example.Meeting_Calendar_Assistant.model.Employee;
import com.example.Meeting_Calendar_Assistant.repository.EmployeeRepository;
import com.example.Meeting_Calendar_Assistant.service.EmployeeService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;

/**
 * Service implementation for managing employee-related operations.
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * Save an employee to the database.
     *
     * @param employee the employee to be saved
     */
    @Override
    public void saveEmployee(Employee employee) {
        logger.info("Saving employee: {}", employee);
        employeeRepository.save(employee);
        logger.info("Employee saved successfully: {}", employee);
    }
}
