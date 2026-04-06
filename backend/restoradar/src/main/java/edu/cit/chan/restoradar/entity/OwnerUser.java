package edu.cit.chan.restoradar.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("OWNER")
public class OwnerUser extends UserEntity {

    public OwnerUser() {
        super();
    }
}
