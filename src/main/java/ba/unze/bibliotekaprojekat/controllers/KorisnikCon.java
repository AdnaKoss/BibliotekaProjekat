package ba.unze.bibliotekaprojekat.controllers;

import jakarta.servlet.http.HttpServletRequest;
import ba.unze.bibliotekaprojekat.models.KorisnikModel;
import ba.unze.bibliotekaprojekat.repositories.KorisnikRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/korisnici")
public class KorisnikCon {

    @Autowired
    private KorisnikRepo korisnikRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // prikaz svih korisnika
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String prikaziKorisnike(Model model) {
        model.addAttribute("korisnici", korisnikRepo.findAll());
        return "admin/korisnik";
    }

    // forma za dodavanje novog korisnika
    @GetMapping("/novi")
    @PreAuthorize("hasRole('ADMIN')")
    public String prikaziFormuZaNovogKorisnika(HttpServletRequest request, Model model) {
        model.addAttribute("korisnik", new KorisnikModel());

        if (request.isUserInRole("ROLE_ADMIN")) {
            return "admin/korisnik-novi";
        } else if (request.isUserInRole("ROLE_USER")) {
            return "korisnik-novi";
        }
        return "korisnik-novi";
    }

    // Dodavanje novog korisnika
    @PostMapping("/novi")
    @PreAuthorize("hasRole('ADMIN')")
    public String dodajKorisnika(@ModelAttribute KorisnikModel korisnik, Model model) {

        if (korisnikRepo.existsByUsername(korisnik.getUsername())) {
            model.addAttribute("errorMessage", "Korisničko ime već postoji. Unesite drugo korisničko ime.");
            model.addAttribute("korisnik", korisnik);
            return "admin/korisnik-novi";
        }

        korisnik.setPassword(passwordEncoder.encode(korisnik.getPassword()));

        String role = korisnik.getRole();
        if (role == null || role.isBlank()) role = "ROLE_USER";
        role = role.trim().toUpperCase();
        if (!role.startsWith("ROLE_")) role = "ROLE_" + role;
        korisnik.setRole(role);

        korisnikRepo.save(korisnik);
        return "redirect:/korisnici";
    }

    // forma za izmjenu korisnika
    @GetMapping("/izmijeni/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String prikaziFormuZaIzmjenu(@PathVariable Long id, Model model) {
        korisnikRepo.findById(id).ifPresentOrElse(
                korisnik -> model.addAttribute("korisnik", korisnik),
                () -> model.addAttribute("errorMessage", "Korisnik nije pronađen.")
        );
        return "admin/korisnik-izmjena";
    }

    // snimanje izmjene korisnika
    @PostMapping("/izmijeni/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String sacuvajIzmjene(@PathVariable Long id, @ModelAttribute KorisnikModel korisnik, Model model) {

        KorisnikModel postojeci = korisnikRepo.findById(id).orElse(null);
        if (postojeci == null) {
            model.addAttribute("errorMessage", "Korisnik s ID-om " + id + " ne postoji.");
            return "admin/korisnik-izmjena";
        }

        postojeci.setIme(korisnik.getIme());
        postojeci.setPrezime(korisnik.getPrezime());
        postojeci.setUsername(korisnik.getUsername());

        // role
        String role = korisnik.getRole();
        if (role != null && !role.isBlank()) {
            role = role.trim().toUpperCase();
            if (!role.startsWith("ROLE_")) role = "ROLE_" + role;
            postojeci.setRole(role);
        }

        // password: samo ako je korisnik unio novu lozinku
        if (korisnik.getPassword() != null && !korisnik.getPassword().isBlank()) {
            postojeci.setPassword(passwordEncoder.encode(korisnik.getPassword()));
        }

        korisnikRepo.save(postojeci);
        return "redirect:/korisnici";
    }

    // brisanje korisnika
    @PostMapping("/obrisi/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String obrisiKorisnikaPost(@PathVariable Long id, Model model) {
        if (!korisnikRepo.existsById(id)) {
            model.addAttribute("errorMessage", "Korisnik s ID-om " + id + " ne postoji.");
            return "admin/korisnik";
        }
        korisnikRepo.deleteById(id);
        return "redirect:/korisnici";
    }
}
