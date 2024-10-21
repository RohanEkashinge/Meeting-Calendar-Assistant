package com.example.Meeting_Calendar_Assistant.exception;

/**
 * Custom exception to handle meeting conflicts.
 */
public class MeetingConflictException extends RuntimeException {
    public MeetingConflictException(String message) {
        super(message);
    }
}
