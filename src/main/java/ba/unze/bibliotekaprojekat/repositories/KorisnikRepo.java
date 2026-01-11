package ba.unze.bibliotekaprojekat.repositories;

import ba.unze.bibliotekaprojekat.models.KorisnikModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KorisnikRepo extends JpaRepository<KorisnikModel, Long> {

    boolean existsByUsername(String username);

    Optional<KorisnikModel> findByUsername(String username);
}
