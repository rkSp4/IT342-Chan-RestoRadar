package edu.cit.chan.restoradar.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("USER")
public class CustomerUser extends UserEntity {
    
    public CustomerUser() {
        super();
    }
}
