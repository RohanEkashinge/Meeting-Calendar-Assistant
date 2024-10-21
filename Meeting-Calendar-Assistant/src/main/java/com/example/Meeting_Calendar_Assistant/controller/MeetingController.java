package com.example.Meeting_Calendar_Assistant.controller;

import com.example.Meeting_Calendar_Assistant.exception.MeetingConflictException;
import com.example.Meeting_Calendar_Assistant.model.Meeting;
import com.example.Meeting_Calendar_Assistant.service.MeetingService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for managing meetings.
 */
@RestController
@RequestMapping("/api/meetings")
public class MeetingController {
    private static final Logger logger = LoggerFactory.getLogger(MeetingController.class);

    @Autowired
    private MeetingService meetingService;

    /**
     * Book a meeting with the calendar owner.
     *
     * @param meeting the meeting to be booked
     * @return a response indicating success or failure
     */
    @PostMapping
    public ResponseEntity<String> bookMeeting(@RequestBody Meeting meeting) {
        try {
            logger.info("Booking meeting: {}", meeting);
            meetingService.bookMeeting(meeting);
            logger.info("Meeting booked successfully: {}", meeting);
            return ResponseEntity.ok("Meeting booked successfully.");
        } catch (MeetingConflictException e) {
            logger.warn("Conflict while booking meeting: {}", e.getMessage());
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    /**
     * Find free slots for a meeting.
     *
     * @param employeeIds list of employee IDs
     * @param duration duration of the meeting in minutes
     * @return list of free slots
     */
    @GetMapping("/free-slots")
    public ResponseEntity<List<LocalDateTime>> getFreeSlots(@RequestParam List<String> employeeIds, @RequestParam int duration) {
        logger.info("Finding free slots for employees: {} with duration: {} minutes", employeeIds, duration);
        List<Meeting> allMeetings = new ArrayList<>();

        // Retrieve meetings for each employee
        for (String employeeId : employeeIds) {
            logger.debug("Retrieving meetings for employee ID: {}", employeeId);
            allMeetings.addAll(meetingService.findMeetings(employeeId));
        }

        // Assuming you want to split the meetings based on two employees for free slots
        List<Meeting> employee1Meetings = new ArrayList<>();
        List<Meeting> employee2Meetings = new ArrayList<>();

        // You can customize this logic to split meetings based on your application needs
        if (employeeIds.size() > 0) {
            employee1Meetings = allMeetings.stream().filter(m -> m.getEmployee().getId().equals(employeeIds.get(0))).toList();
        }

        if (employeeIds.size() > 1) {
            employee2Meetings = allMeetings.stream().filter(m -> m.getEmployee().getId().equals(employeeIds.get(1))).toList();
        }

        // Now call the findFreeSlots method with the two lists of meetings
        List<LocalDateTime> freeSlots = meetingService.findFreeSlots(employee1Meetings, employee2Meetings, duration);

        logger.info("Free slots found: {}", freeSlots);
        return ResponseEntity.ok(freeSlots);
    }


    /**
     * Check for meeting conflicts among participants.
     *
     * @param meeting the meeting to check
     * @param participantIds list of participant IDs
     * @return list of participants with conflicts
     */
    @PostMapping("/check-conflicts")
    public ResponseEntity<List<String>> checkConflicts(@RequestBody Meeting meeting, @RequestParam List<String> participantIds) {
        logger.info("Checking for conflicts for meeting: {} with participants: {}", meeting, participantIds);
        List<String> conflicts = meetingService.checkConflicts(meeting, participantIds);
        logger.info("Conflicts found: {}", conflicts);
        return ResponseEntity.ok(conflicts);
    }
}
