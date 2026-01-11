package ba.unze.bibliotekaprojekat.controllers;

import jakarta.validation.Valid;
import ba.unze.bibliotekaprojekat.models.KnjigaModel;
import ba.unze.bibliotekaprojekat.models.KorisnikModel;
import ba.unze.bibliotekaprojekat.models.ZaduzenjeModel;
import ba.unze.bibliotekaprojekat.repositories.KnjigaRepo;
import ba.unze.bibliotekaprojekat.repositories.KorisnikRepo;
import ba.unze.bibliotekaprojekat.repositories.ZaduzenjeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/zaduzenja")
public class ZaduzenjeCon {

    @Autowired
    private ZaduzenjeRepo zaduzenjeRepository;

    @Autowired
    private KorisnikRepo korisnikRepository;

    @Autowired
    private KnjigaRepo knjigaRepository;

    // ADMIN: prikaz svih zaduzenja
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String prikaziSvaZaduzenja(Model model) {
        model.addAttribute("zaduzenja", zaduzenjeRepository.findAll());
        return "admin/zaduzenje";
    }

    // ADMIN: forma za dodavanje zaduzenja
    @GetMapping("/nova")
    @PreAuthorize("hasRole('ADMIN')")
    public String prikaziFormuZaNovoZaduzenje(Model model) {
        model.addAttribute("zaduzenje", new ZaduzenjeModel());
        model.addAttribute("knjige", knjigaRepository.findAll());
        model.addAttribute("korisnici", korisnikRepository.findAll());
        return "admin/zaduzenja-nova";
    }

    // USER: prikaz korisnikovih zaduzenja
    @GetMapping("/mojazaduzenja")
    @PreAuthorize("hasRole('USER')")
    public String prikaziZaduzenja(Model model, Authentication authentication) {

        String username = authentication.getName();

        KorisnikModel korisnik = korisnikRepository.findByUsername(username).orElse(null);

        if (korisnik == null) {
            model.addAttribute("zaduzenja", List.of());
            model.addAttribute("poruka", "Korisnik nije pronađen u bazi.");
            return "user/korisnik-zaduzenja";
        }

        List<ZaduzenjeModel> zaduzenja = zaduzenjeRepository.findByKorisnikId(korisnik.getId());
        model.addAttribute("zaduzenja", zaduzenja);

        return "user/korisnik-zaduzenja";
    }

    // ADMIN: forma za izmjenu zaduzenja
    @GetMapping("/izmijeni/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String prikaziFormuZaUredjivanje(@PathVariable Long id, Model model) {

        ZaduzenjeModel zaduzenje = zaduzenjeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Zaduženje nije pronađeno"));

        model.addAttribute("zaduzenje", zaduzenje);
        model.addAttribute("korisnici", korisnikRepository.findAll());
        model.addAttribute("knjige", knjigaRepository.findAll());

        return "admin/zaduzenje-izmjena";
    }

    // ADMIN: dodavanje novog zaduzenja
    @PostMapping("/nova")
    @PreAuthorize("hasRole('ADMIN')")
    public String kreirajNovoZaduzenje(
            @ModelAttribute @Valid ZaduzenjeModel zaduzenje,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("knjige", knjigaRepository.findAll());
            model.addAttribute("korisnici", korisnikRepository.findAll());
            return "admin/zaduzenja-nova";
        }

        // Učitaj prave entitete po ID-u (zbog *{knjiga.id} i *{korisnik.id})
        Long knjigaId = zaduzenje.getKnjiga().getId();
        Long korisnikId = zaduzenje.getKorisnik().getId();

        KnjigaModel knjiga = knjigaRepository.findById(knjigaId)
                .orElseThrow(() -> new RuntimeException("Knjiga nije pronađena"));

        KorisnikModel korisnik = korisnikRepository.findById(korisnikId)
                .orElseThrow(() -> new RuntimeException("Korisnik nije pronađen"));

        // provjera dostupnosti knjige
        if (knjiga.getDostupnoPrimjeraka() <= 0) {
            throw new RuntimeException("Nema dostupnih primjeraka knjige: " + knjiga.getNaslov());
        }

        // umanji dostupno
        knjiga.setDostupnoPrimjeraka(knjiga.getDostupnoPrimjeraka() - 1);
        knjigaRepository.save(knjiga);

        // setuj veze
        zaduzenje.setKnjiga(knjiga);
        zaduzenje.setKorisnik(korisnik);

        // datum zaduzenja ako nije unesen
        if (zaduzenje.getDatumZaduzenja() == null) {
            zaduzenje.setDatumZaduzenja(LocalDate.now());
        }

        // automatski datum vraćanja
        zaduzenje.setDatumVracanja(zaduzenje.getDatumZaduzenja().plusDays(15));

        // status default je IZNAJMLJENO u modelu
        zaduzenjeRepository.save(zaduzenje);

        return "redirect:/zaduzenja";
    }

    // ADMIN: izmjena zaduzenja (OVO TI JE FALILO)
    @PostMapping("/izmijeni/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String sacuvajIzmjene(
            @PathVariable Long id,
            @ModelAttribute @Valid ZaduzenjeModel zaduzenje,
            BindingResult result,
            Model model
    ) {
        if (result.hasErrors()) {
            model.addAttribute("korisnici", korisnikRepository.findAll());
            model.addAttribute("knjige", knjigaRepository.findAll());
            return "admin/zaduzenje-izmjena";
        }

        ZaduzenjeModel postojece = zaduzenjeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Zaduženje nije pronađeno"));

        // učitaj prave entitete iz baze
        Long knjigaId = zaduzenje.getKnjiga().getId();
        Long korisnikId = zaduzenje.getKorisnik().getId();

        KnjigaModel knjiga = knjigaRepository.findById(knjigaId)
                .orElseThrow(() -> new RuntimeException("Knjiga nije pronađena"));

        KorisnikModel korisnik = korisnikRepository.findById(korisnikId)
                .orElseThrow(() -> new RuntimeException("Korisnik nije pronađen"));

        // primijeni izmjene
        postojece.setKnjiga(knjiga);
        postojece.setKorisnik(korisnik);
        postojece.setDatumZaduzenja(zaduzenje.getDatumZaduzenja());
        postojece.setDatumVracanja(zaduzenje.getDatumVracanja());

        // status zadrži kakav jeste
        // postojece.setStatus(postojece.getStatus());

        zaduzenjeRepository.save(postojece);

        return "redirect:/zaduzenja";
    }

    // ADMIN: vraćanje knjige i brisanje zaduzenja
    @PostMapping("/vrati/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String vratiIZbrisiZaduzenje(@PathVariable Long id) {

        ZaduzenjeModel zaduzenje = zaduzenjeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Zaduženje nije pronađeno"));

        KnjigaModel knjiga = zaduzenje.getKnjiga();
        knjiga.setDostupnoPrimjeraka(knjiga.getDostupnoPrimjeraka() + 1);
        knjigaRepository.save(knjiga);

        zaduzenjeRepository.delete(zaduzenje);

        return "redirect:/zaduzenja";
    }
}
