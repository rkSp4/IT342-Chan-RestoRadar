package edu.cit.chan.restoradar.feature.user;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("OWNER")
public class OwnerUser extends UserEntity {

    public OwnerUser() {
        super();
    }
}
