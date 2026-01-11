package ba.unze.bibliotekaprojekat.repositories;

import ba.unze.bibliotekaprojekat.models.ZaduzenjeModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ZaduzenjeRepo extends JpaRepository<ZaduzenjeModel, Long> {

    List<ZaduzenjeModel> findByKorisnikId(Long korisnikId);

    boolean existsByKnjigaId(Long knjigaId);
}
