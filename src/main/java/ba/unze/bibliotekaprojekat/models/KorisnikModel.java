package ba.unze.bibliotekaprojekat.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Table(name = "korisnici")
public class KorisnikModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Ime ne može biti prazno.")
    @Size(min = 2, max = 50, message = "Ime mora biti između 2 i 50 karaktera.")
    private String ime;

    @NotNull(message = "Prezime ne može biti prazno.")
    @Size(min = 2, max = 50, message = "Prezime mora biti između 2 i 50 karaktera.")
    private String prezime;

    @NotNull(message = "Korisničko ime ne može biti prazno.")
    @Column(name = "korisnicko_ime", unique = true, nullable = false, length = 50)
    private String username;

    @Column(name = "lozinka", nullable = false)
    @NotNull(message = "Lozinka ne može biti prazna.")
    private String password;

    @Column(name = "uloga", nullable = false, length = 20)
    @NotNull(message = "Uloga je obavezna.")
    private String role; // ROLE_USER ili ROLE_ADMIN

    // Getteri i setteri
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role =role;
    }

    public String getImePrezime() {
        return ime + " " + prezime;
    }
}