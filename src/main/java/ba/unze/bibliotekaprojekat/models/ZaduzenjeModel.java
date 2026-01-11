package ba.unze.bibliotekaprojekat.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(name = "zaduzenja")
public class ZaduzenjeModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "knjiga_id")
    @NotNull(message = "Knjiga ne može biti prazna.")
    private KnjigaModel knjiga;

    @ManyToOne
    @JoinColumn(name = "korisnik_id")
    @NotNull(message = "Korisnik ne može biti prazan.")
    private KorisnikModel korisnik;

    @Column(name = "datum_zaduzenja", nullable = false)
    @NotNull(message = "Datum zaduženja ne može biti prazan.")
    private LocalDate datumZaduzenja;

    @Column(name = "datum_vracanja")
    private LocalDate datumVracanja;

    @Column(name = "status", nullable = false)
    private String status = "IZNAJMLJENO";

    // Getteri i setteri
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public KnjigaModel getKnjiga() {
        return knjiga;
    }

    public void setKnjiga(KnjigaModel knjiga) {
        this.knjiga = knjiga;
    }

    public KorisnikModel getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(KorisnikModel korisnik) {
        this.korisnik = korisnik;
    }

    public LocalDate getDatumZaduzenja() {
        return datumZaduzenja;
    }

    public void setDatumZaduzenja(LocalDate datumZaduzenja) {
        this.datumZaduzenja = datumZaduzenja;
    }

    public LocalDate getDatumVracanja() {
        return datumVracanja;
    }

    public void setDatumVracanja(LocalDate datumVracanja) {
        this.datumVracanja = datumVracanja;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isValid() {
        return knjiga != null && korisnik != null && datumZaduzenja != null;
    }

    @Override
    public String toString() {
        return "Zaduzenje{" +
                "id=" + id +
                ", knjiga=" + (knjiga != null ? knjiga.getNaslov() : "N/A") +
                ", korisnik=" + (korisnik != null ? korisnik.getImePrezime() : "N/A") +
                ", datumZaduzenja=" + datumZaduzenja +
                '}';
    }
}
