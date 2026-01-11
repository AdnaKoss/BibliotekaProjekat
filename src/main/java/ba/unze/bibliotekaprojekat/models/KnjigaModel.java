package ba.unze.bibliotekaprojekat.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;


@Entity
@Table(name = "knjige")
public class KnjigaModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "naslov_knjige", length = 100, nullable = false)
    @NotNull(message = "Naslov knjige ne može biti prazan.")
    @Size(min = 1, max = 100, message = "Naslov mora biti između 1 i 100 karaktera.")
    private String naslov;

    @Column(name = "autor_knjige", length = 100, nullable = false)
    @NotNull(message = "Autor knjige ne može biti prazan.")
    @Size(min = 1, max = 100, message = "Autor mora biti između 1 i 100 karaktera.")
    private String autor;

    @Column(name = "ukupno_primjeraka", nullable = false)
    @NotNull(message = "Broj primjeraka ne može biti prazan.")
    @Min(value = 1, message = "Mora postojati bar jedan primjerak.")
    private Integer ukupnoPrimjeraka;

    @Column(name = "dostupno_primjeraka", nullable = false)
    private Integer dostupnoPrimjeraka;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNaslov() {
        return naslov;
    }

    public void setNaslov(String naslov) {
        this.naslov = naslov;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Integer getUkupnoPrimjeraka() {
        return ukupnoPrimjeraka;
    }

    public void setUkupnoPrimjeraka(int ukupnoPrimjeraka) {
        this.ukupnoPrimjeraka = ukupnoPrimjeraka;
    }

    public  Integer getDostupnoPrimjeraka() {
        return dostupnoPrimjeraka;
    }

    public void setDostupnoPrimjeraka(int dostupnoPrimjeraka) {
        this.dostupnoPrimjeraka = dostupnoPrimjeraka;
    }
}