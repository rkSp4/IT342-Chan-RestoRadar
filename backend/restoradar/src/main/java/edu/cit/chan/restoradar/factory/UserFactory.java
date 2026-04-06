package edu.cit.chan.restoradar.factory;

import edu.cit.chan.restoradar.entity.*;
import org.springframework.stereotype.Component;

@Component
public class UserFactory {

    public UserEntity createUser(Role role) {
        if (role == null) {
            return new CustomerUser(); // Default
        }

        switch (role) {
            case OWNER:
                return new OwnerUser();
            case ADMIN:
                return new AdminUser();
            case USER:
            default:
                return new CustomerUser();
        }
    }
}
