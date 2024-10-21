package com.example.Meeting_Calendar_Assistant.repository;

import com.example.Meeting_Calendar_Assistant.model.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    List<Meeting> findByEmployeeId(String employeeId);
}