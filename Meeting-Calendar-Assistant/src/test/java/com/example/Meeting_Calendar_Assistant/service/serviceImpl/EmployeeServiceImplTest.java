package com.example.Meeting_Calendar_Assistant.service.serviceImpl;

import com.example.Meeting_Calendar_Assistant.model.Employee;
import com.example.Meeting_Calendar_Assistant.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        employee = new Employee();
        employee.setId("1");
        employee.setName("John Doe"); // Setting additional fields
    }

    @Test
    void saveEmployee() {
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        employeeService.saveEmployee(employee);

        verify(employeeRepository, times(1)).save(employee);
    }
}