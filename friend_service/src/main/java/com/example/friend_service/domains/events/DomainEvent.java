package com.example.friend_service.domains.events;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

public class DomainEvent<T> {

    private T subject;
    private EventType eventType;
    private Date createdDate = Date.from(Instant.now());
    private String createdBy;

    public DomainEvent(){}

    public T getSubject() {
        return subject;
    }
    public void setSubject(T subject) {
        this.subject = subject;
    }
    public EventType getEventType() {
        return eventType;
    }
    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }
    public Date getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    public String getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
