package edu.cit.chan.restoradar.feature.user;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ADMIN")
public class AdminUser extends UserEntity {

    public AdminUser() {
        super();
    }
}
