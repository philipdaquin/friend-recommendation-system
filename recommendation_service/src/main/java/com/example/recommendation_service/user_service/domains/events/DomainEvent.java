package com.example.recommendation_service.user_service.domains.events;

import java.time.Instant;

public class DomainEvent<T> {

    private T subject;
    private UserEventType eventType;
    private Instant createdDate = Instant.now();
    private String createdBy;

    public DomainEvent(){}

    public T getSubject() {
        return subject;
    }
    public void setSubject(T subject) {
        this.subject = subject;
    }
    public UserEventType getEventType() {
        return eventType;
    }
    public void setEventType(UserEventType eventType) {
        this.eventType = eventType;
    }
    public Instant getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }
    public String getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
