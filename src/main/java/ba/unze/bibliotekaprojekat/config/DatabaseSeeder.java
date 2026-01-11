package ba.unze.bibliotekaprojekat.config;

import ba.unze.bibliotekaprojekat.models.KorisnikModel;
import ba.unze.bibliotekaprojekat.repositories.KorisnikRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final KorisnikRepo korisnikRepo;
    private final PasswordEncoder passwordEncoder;

    public DatabaseSeeder(KorisnikRepo korisnikRepo, PasswordEncoder passwordEncoder) {
        this.korisnikRepo = korisnikRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        if (!korisnikRepo.existsByUsername("user")) {
            KorisnikModel user = new KorisnikModel();
            user.setIme("Test");
            user.setPrezime("User");
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setRole("ROLE_USER");
            korisnikRepo.save(user);
        }

        if (!korisnikRepo.existsByUsername("admin")) {
            KorisnikModel admin = new KorisnikModel();
            admin.setIme("Test");
            admin.setPrezime("Admin");
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ROLE_ADMIN");
            korisnikRepo.save(admin);
        }
    }
}
