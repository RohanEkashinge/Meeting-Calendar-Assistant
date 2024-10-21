package com.example.Meeting_Calendar_Assistant.service.serviceImpl;

import com.example.Meeting_Calendar_Assistant.exception.MeetingConflictException;
import com.example.Meeting_Calendar_Assistant.model.Meeting;
import com.example.Meeting_Calendar_Assistant.repository.MeetingRepository;
import com.example.Meeting_Calendar_Assistant.service.MeetingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service implementation for managing meeting-related operations.
 */
@Service
public class MeetingServiceImpl implements MeetingService {

    private static final Logger logger = LoggerFactory.getLogger(MeetingServiceImpl.class);

    @Autowired
    private MeetingRepository meetingRepository;

    /**
     * Book a new meeting if there are no scheduling conflicts.
     *
     * @param meeting the meeting to be booked
     * @throws MeetingConflictException if there is a scheduling conflict
     */
    @Override
    public void bookMeeting(Meeting meeting) {
        logger.info("Booking meeting: {}", meeting);
        List<Meeting> existingMeetings = meetingRepository.findByEmployeeId(meeting.getEmployee().getId());
        if (hasConflict(existingMeetings, meeting)) {
            throw new MeetingConflictException("Meeting conflicts with existing schedules.");
        }
        meetingRepository.save(meeting);
        logger.info("Meeting booked successfully: {}", meeting);
    }

    /**
     * Find all meetings for a specific employee.
     *
     * @param employeeId the ID of the employee
     * @return a list of meetings for the employee
     */
    @Override
    public List<Meeting> findMeetings(String employeeId) {
        logger.info("Finding meetings for employee ID: {}", employeeId);
        return meetingRepository.findByEmployeeId(employeeId);
    }

    /**
     * Find free time slots between two employees based on their existing meetings.
     *
     * @param employee1Meetings meetings of the first employee
     * @param employee2Meetings meetings of the second employee
     * @param duration the duration of the desired free slot in minutes
     * @return a list of free time slots
     */
    @Override
    public List<LocalDateTime> findFreeSlots(List<Meeting> employee1Meetings, List<Meeting> employee2Meetings, int duration) {
        logger.info("Finding free slots for meetings.");
        List<LocalDateTime> freeSlots = new ArrayList<>();

        // Merge meetings from both employees
        List<Meeting> allMeetings = new ArrayList<>();
        allMeetings.addAll(employee1Meetings);
        allMeetings.addAll(employee2Meetings);

        // Sort meetings by start time
        allMeetings.sort((m1, m2) -> m1.getStartTime().compareTo(m2.getStartTime()));

        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);

        LocalDateTime lastEndTime = startOfDay;

        for (Meeting meeting : allMeetings) {
            if (lastEndTime.isBefore(meeting.getStartTime())) {
                // If there's a gap between meetings
                while (lastEndTime.plusMinutes(duration).isBefore(meeting.getStartTime())) {
                    freeSlots.add(lastEndTime);
                    lastEndTime = lastEndTime.plusMinutes(duration);
                }
            }
            // Move the lastEndTime to the end of the current meeting
            lastEndTime = meeting.getEndTime().isAfter(lastEndTime) ? meeting.getEndTime() : lastEndTime;
        }

        // Check for free time after the last meeting until the end of the day
        while (lastEndTime.plusMinutes(duration).isBefore(endOfDay)) {
            freeSlots.add(lastEndTime);
            lastEndTime = lastEndTime.plusMinutes(duration);
        }
        logger.info("Found free slots: {}", freeSlots);
        return freeSlots;
    }

    /**
     * Check for conflicts among participants for a specific meeting.
     *
     * @param meeting the meeting to check for conflicts
     * @param participantIds list of participant IDs
     * @return list of participant IDs that have conflicts
     */
    @Override
    public List<String> checkConflicts(Meeting meeting, List<String> participantIds) {
        logger.info("Checking for conflicts for meeting: {} with participants: {}", meeting, participantIds);
        List<String> conflictingParticipants = new ArrayList<>();
        for (String participantId : participantIds) {
            List<Meeting> participantMeetings = findMeetings(participantId);
            if (hasConflict(participantMeetings, meeting)) {
                conflictingParticipants.add(participantId);
                logger.warn("Conflict found for participant: {}", participantId);
            }
        }
        return conflictingParticipants;
    }

    /**
     * Check if a new meeting conflicts with existing meetings.
     *
     * @param meetings existing meetings to check against
     * @param newMeeting the new meeting to check for conflicts
     * @return true if there is a conflict, false otherwise
     */
    private boolean hasConflict(List<Meeting> meetings, Meeting newMeeting) {
        return meetings.stream().anyMatch(existingMeeting ->
                (newMeeting.getStartTime().isBefore(existingMeeting.getEndTime()) &&
                        newMeeting.getEndTime().isAfter(existingMeeting.getStartTime()))
        );
    }
}
