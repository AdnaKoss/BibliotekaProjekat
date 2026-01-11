package ba.unze.bibliotekaprojekat.controllers;

import ba.unze.bibliotekaprojekat.models.KnjigaModel;
import ba.unze.bibliotekaprojekat.repositories.KnjigaRepo;
import ba.unze.bibliotekaprojekat.repositories.ZaduzenjeRepo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/knjige")
public class KnjigaCon {

    @Autowired
    private KnjigaRepo knjigaRepository;

    @Autowired
    private ZaduzenjeRepo zaduzenjeRepository;

    // ✅ Prikaz knjiga + poruke error/success
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String prikaziKnjige(Model model,
                                @RequestParam(value = "error", required = false) String error,
                                @RequestParam(value = "success", required = false) String success) {

        model.addAttribute("knjige", knjigaRepository.findAll());

        if (error != null && !error.isBlank()) {
            model.addAttribute("errorMessage", error);
        }
        if (success != null && !success.isBlank()) {
            model.addAttribute("successMessage", success);
        }

        return "admin/knjiga";
    }

    // ✅ forma za dodavanje nove knjige
    @GetMapping("/nova-knjiga")
    @PreAuthorize("hasRole('ADMIN')")
    public String prikaziFormuDodajKnjigu(Model model) {
        model.addAttribute("knjiga", new KnjigaModel());
        return "admin/knjiga-nova";
    }

    // ✅ dodavanje nove knjige (ako već postoji naslov+autor: povećaj primjerke)
    @PostMapping("/nova-knjiga")
    @PreAuthorize("hasRole('ADMIN')")
    public String dodajKnjigu(@Valid @ModelAttribute("knjiga") KnjigaModel novaKnjiga,
                              BindingResult result,
                              Model model) {

        if (result.hasErrors()) {
            return "admin/knjiga-nova";
        }

        // Ako nije unesen dostupno, postavi da = ukupno
        if (novaKnjiga.getDostupnoPrimjeraka() == null) {
            novaKnjiga.setDostupnoPrimjeraka(novaKnjiga.getUkupnoPrimjeraka());
        }

        KnjigaModel postojecaKnjiga = knjigaRepository.findByNaslovAndAutor(
                novaKnjiga.getNaslov(), novaKnjiga.getAutor()
        );

        if (postojecaKnjiga != null) {
            postojecaKnjiga.setUkupnoPrimjeraka(
                    postojecaKnjiga.getUkupnoPrimjeraka() + novaKnjiga.getUkupnoPrimjeraka()
            );
            postojecaKnjiga.setDostupnoPrimjeraka(
                    postojecaKnjiga.getDostupnoPrimjeraka() + novaKnjiga.getDostupnoPrimjeraka()
            );
            knjigaRepository.save(postojecaKnjiga);
        } else {
            knjigaRepository.save(novaKnjiga);
        }

        return "redirect:/knjige?success=Knjiga%20je%20uspje%C5%A1no%20sa%C4%8Duvana.";
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public String handleMethodNotSupported(Exception ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    // ✅ forma za uredjivanje knjige
    @GetMapping("/edit/{knjigaId}")
    @PreAuthorize("hasRole('ADMIN')")
    public String prikaziFormuEditujKnjigu(@PathVariable Long knjigaId, Model model) {

        KnjigaModel knjiga = knjigaRepository.findById(knjigaId).orElse(null);
        if (knjiga == null) {
            return "redirect:/knjige?error=Knjiga%20nije%20prona%C4%91ena.";
        }

        model.addAttribute("knjiga", knjiga);
        return "admin/knjiga-izmjena";
    }

    // ✅ snimanje izmjene knjige
    @PostMapping("/edit/{knjigaId}")
    @PreAuthorize("hasRole('ADMIN')")
    public String spremiIzmjeneKnjige(@PathVariable Long knjigaId,
                                      @ModelAttribute KnjigaModel knjiga,
                                      Model model) {

        KnjigaModel existingKnjiga = knjigaRepository.findById(knjigaId).orElse(null);
        if (existingKnjiga == null) {
            return "redirect:/knjige?error=Knjiga%20nije%20prona%C4%91ena.";
        }

        existingKnjiga.setNaslov(knjiga.getNaslov());
        existingKnjiga.setAutor(knjiga.getAutor());

        if (knjiga.getUkupnoPrimjeraka() != null) {
            existingKnjiga.setUkupnoPrimjeraka(knjiga.getUkupnoPrimjeraka());
        }

        if (knjiga.getDostupnoPrimjeraka() != null) {
            if (knjiga.getDostupnoPrimjeraka() > existingKnjiga.getUkupnoPrimjeraka()) {
                model.addAttribute("errorMessage",
                        "Broj dostupnih primjeraka ne može biti veći od ukupnog broja.");
                model.addAttribute("knjiga", existingKnjiga);
                return "admin/knjiga-izmjena";
            }
            existingKnjiga.setDostupnoPrimjeraka(knjiga.getDostupnoPrimjeraka());
        } else {
            existingKnjiga.setDostupnoPrimjeraka(existingKnjiga.getUkupnoPrimjeraka());
        }

        knjigaRepository.save(existingKnjiga);
        return "redirect:/knjige?success=Izmjene%20su%20sa%C4%8Duvane.";
    }

    // ✅ brisanje bez bijele stranice (ako ima zaduženja -> poruka)
    @PostMapping("/obrisi/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String obrisiKnjiguPost(@PathVariable Long id) {

        if (!knjigaRepository.existsById(id)) {
            return "redirect:/knjige?error=Knjiga%20ne%20postoji.";
        }

        // 1) Lijepa provjera prije brisanja
        if (zaduzenjeRepository.existsByKnjigaId(id)) {
            return "redirect:/knjige?error=Nemogu%C4%87e%20izbrisati%20knjigu%20jer%20ima%20zadu%C5%BEenja.";
        }

        // 2) Fallback - čak i da provjera omaši, nema bijele stranice
        try {
            knjigaRepository.deleteById(id);
        } catch (Exception ex) {
            return "redirect:/knjige?error=Nemogu%C4%87e%20izbrisati%20knjigu%20jer%20je%20povezana%20sa%20zadu%C5%BEenjima.";
        }

        return "redirect:/knjige?success=Knjiga%20je%20obrisana.";
    }
}
