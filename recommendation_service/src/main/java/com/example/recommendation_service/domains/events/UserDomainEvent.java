package com.example.recommendation_service.domains.events;

import java.time.Instant;
import java.util.Date;

import com.example.recommendation_service.domains.enums.UserEventType;
public class UserDomainEvent<T> {

    private T subject;
    private UserEventType eventType;
    private Date createdDate = Date.from(Instant.now());
    private String createdBy;

    public UserDomainEvent(){}

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

    @Override
    public String toString() {
        return "DomainEvent {subject=" + subject + ", eventType=" + eventType + ", createdDate=" + createdDate
                + ", createdBy=" + createdBy + "}";
    }
    
}
