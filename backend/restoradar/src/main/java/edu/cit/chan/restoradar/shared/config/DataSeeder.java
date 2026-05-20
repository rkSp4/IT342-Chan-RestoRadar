package edu.cit.chan.restoradar.shared.config;

import edu.cit.chan.restoradar.feature.user.UserEntity;
import edu.cit.chan.restoradar.feature.user.UserFactory;
import edu.cit.chan.restoradar.feature.user.UserRepository;
import edu.cit.chan.restoradar.shared.entity.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Runs once on every startup.
 * Creates a default ADMIN account if none exists yet.
 *
 * Default credentials (change immediately after first login):
 *   Email:    admin@restoradar.com
 *   Password: Admin@1234
 */
@Component
public class DataSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private static final String ADMIN_EMAIL    = "admin@restoradar.com";
    private static final String ADMIN_NAME     = "Admin";
    private static final String ADMIN_PASSWORD = "Admin@1234";

    private final UserRepository  userRepository;
    private final UserFactory     userFactory;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository,
                      UserFactory userFactory,
                      PasswordEncoder passwordEncoder) {
        this.userRepository  = userRepository;
        this.userFactory     = userFactory;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        seedAdmin();
    }

    private void seedAdmin() {
        if (userRepository.existsByEmail(ADMIN_EMAIL)) {
            log.info("Admin user already exists — skipping seed.");
            return;
        }

        // Use the same UserFactory that AuthService uses so the
        // correct subclass (AdminUser) is instantiated and the
        // discriminator column is set to "ADMIN" automatically.
        UserEntity admin = userFactory.createUser(Role.ADMIN);
        admin.setFullName(ADMIN_NAME);
        admin.setEmail(ADMIN_EMAIL);
        admin.setPasswordHash(passwordEncoder.encode(ADMIN_PASSWORD));

        userRepository.save(admin);

        log.info("==========================================================");
        log.info("  Default admin account created:");
        log.info("  Email   : {}", ADMIN_EMAIL);
        log.info("  Password: {}", ADMIN_PASSWORD);
        log.info("  !! Change this password immediately after first login !!");
        log.info("==========================================================");
    }
}