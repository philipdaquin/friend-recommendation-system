package com.example.recommendation_service.user_service.ranked;

import com.example.recommendation_service.user_service.domains.User;

public class RankedUser {
    private User user;
    private Integer weight;

    
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public Integer getWeight() {
        return weight;
    }
    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    
}
