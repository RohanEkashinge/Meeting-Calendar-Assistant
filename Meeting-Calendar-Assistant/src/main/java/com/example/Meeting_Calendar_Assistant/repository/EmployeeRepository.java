package com.example.Meeting_Calendar_Assistant.repository;

import com.example.Meeting_Calendar_Assistant.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {
}
