package com.example.Meeting_Calendar_Assistant.service.serviceImpl;

import com.example.Meeting_Calendar_Assistant.exception.MeetingConflictException;
import com.example.Meeting_Calendar_Assistant.model.Employee;
import com.example.Meeting_Calendar_Assistant.model.Meeting;
import com.example.Meeting_Calendar_Assistant.repository.MeetingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MeetingServiceImplTest {

    @Mock
    private MeetingRepository meetingRepository;

    @InjectMocks
    private MeetingServiceImpl meetingService;

    private Meeting meeting;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        meeting = new Meeting();
        meeting.setEmployee(new Employee()); // Set the employee
        meeting.setStartTime(LocalDateTime.now());
        meeting.setEndTime(LocalDateTime.now().plusHours(1));
        // Set other meeting fields as needed
    }

    @Test
    void bookMeeting_noConflict() {
        when(meetingRepository.findByEmployeeId(meeting.getEmployee().getId())).thenReturn(Collections.emptyList());

        assertDoesNotThrow(() -> meetingService.bookMeeting(meeting));
        verify(meetingRepository, times(1)).save(meeting);
    }

    @Test
    void bookMeeting_conflict() {
        Meeting conflictingMeeting = new Meeting();
        conflictingMeeting.setEmployee(meeting.getEmployee());
        conflictingMeeting.setStartTime(meeting.getStartTime().minusMinutes(30));
        conflictingMeeting.setEndTime(meeting.getEndTime().plusMinutes(30));

        when(meetingRepository.findByEmployeeId(meeting.getEmployee().getId())).thenReturn(List.of(conflictingMeeting));

        assertThrows(MeetingConflictException.class, () -> meetingService.bookMeeting(meeting));
        verify(meetingRepository, never()).save(meeting);
    }

    @Test
    void findMeetings() {
        List<Meeting> meetingsList = new ArrayList<>();
        meetingsList.add(meeting);

        when(meetingRepository.findByEmployeeId(meeting.getEmployee().getId())).thenReturn(meetingsList);

        List<Meeting> result = meetingService.findMeetings(meeting.getEmployee().getId());
        assertEquals(1, result.size());
        assertEquals(meeting, result.get(0));
    }

    @Test
    void findFreeSlots() {
        // Prepare meetings for two employees
        Meeting meeting1 = new Meeting();
        meeting1.setStartTime(LocalDateTime.now().plusHours(1));
        meeting1.setEndTime(LocalDateTime.now().plusHours(2));

        Meeting meeting2 = new Meeting();
        meeting2.setStartTime(LocalDateTime.now().plusHours(3));
        meeting2.setEndTime(LocalDateTime.now().plusHours(4));

        List<Meeting> employee1Meetings = List.of(meeting1);
        List<Meeting> employee2Meetings = List.of(meeting2);

        List<LocalDateTime> freeSlots = meetingService.findFreeSlots(employee1Meetings, employee2Meetings, 30);

        // Check that the free slots are correct
        LocalDateTime expectedFreeSlot = LocalDateTime.now().plusHours(2);
        assertTrue(freeSlots.contains(expectedFreeSlot));
    }

    @Test
    void checkConflicts_noConflicts() {
        List<Meeting> participantMeetings = new ArrayList<>();
        when(meetingRepository.findByEmployeeId(anyString())).thenReturn(participantMeetings);

        List<String> conflicts = meetingService.checkConflicts(meeting, List.of("participant1", "participant2"));
        assertTrue(conflicts.isEmpty());
    }

    @Test
    void checkConflicts_withConflicts() {
        Meeting conflictingMeeting = new Meeting();
        conflictingMeeting.setStartTime(meeting.getStartTime());
        conflictingMeeting.setEndTime(meeting.getEndTime());

        List<Meeting> participantMeetings = List.of(conflictingMeeting);
        when(meetingRepository.findByEmployeeId(anyString())).thenReturn(participantMeetings);

        List<String> conflicts = meetingService.checkConflicts(meeting, List.of("participant1"));
        assertEquals(1, conflicts.size());
        assertEquals("participant1", conflicts.get(0));
    }
}