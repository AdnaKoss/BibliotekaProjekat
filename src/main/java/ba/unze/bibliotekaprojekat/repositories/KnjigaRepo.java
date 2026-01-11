package ba.unze.bibliotekaprojekat.repositories;

import ba.unze.bibliotekaprojekat.models.KnjigaModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KnjigaRepo extends JpaRepository<KnjigaModel, Long> {
    KnjigaModel findByNaslovAndAutor(String naslov, String autor);
}
