package com.example.Meeting_Calendar_Assistant.service;

import com.example.Meeting_Calendar_Assistant.model.Meeting;

import java.time.LocalDateTime;
import java.util.List;

public interface MeetingService {
    void bookMeeting(Meeting meeting);
    List<Meeting> findMeetings(String employeeId);
    List<LocalDateTime> findFreeSlots(List<Meeting> employee1Meetings, List<Meeting> employee2Meetings, int duration);
    List<String> checkConflicts(Meeting meeting, List<String> participantIds);
}
