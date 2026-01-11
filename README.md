# BibliotekaProjekat – Web aplikacija

Ovaj repozitorij sadrži web aplikaciju za upravljanje bibliotekom razvijenu u okviru predmeta **Web programiranje**.  
Aplikacija je izrađena korištenjem **Spring Boot MVC** arhitekture i **MySQL** baze podataka.

---

## Opis aplikacije

Aplikacija omogućava upravljanje osnovnim procesima biblioteke:
- knjigama
- korisnicima
- zaduženjima knjiga

Sistem podržava prijavu korisnika i razlikuje **ADMIN** i **USER** uloge, gdje ADMIN ima puna prava upravljanja sistemom, dok USER ima pristup samo svojim podacima i zaduženjima.

Cilj projekta je praktična primjena MVC arhitekture, rada sa bazom podataka, autentifikacije korisnika i timskog razvoja web aplikacije.

---

## Funkcionalnosti

- Forma za prijavu korisnika (login)
- Upravljanje knjigama (dodavanje, prikaz, izmjena, brisanje)
- Upravljanje korisnicima (dodavanje, prikaz, izmjena, brisanje)
- Upravljanje zaduženjima knjiga
- Prikaz svih zaduženja (ADMIN)
- Prikaz ličnih zaduženja (USER)
- Provjera dostupnosti knjiga prilikom zaduživanja
- Vraćanje knjiga i ažuriranje dostupnih primjeraka

---

## Tehnologije i alati

- Java **JDK 21.0.8**
- Spring Boot **3.5.9**
- Spring MVC
- Spring Security
- Spring Data JPA (Hibernate)
- Maven
- MySQL
- Thymeleaf
- HTML / CSS
- Git & GitHub

---

## Arhitektura projekta

Projekat je organizovan prema **MVC (Model–View–Controller)** arhitekturi:

- **Model** – entiteti (Knjiga, Korisnik, Zaduženje)
- **Controller** – obrada HTTP zahtjeva i poslovna logika
- **View** – Thymeleaf HTML stranice

Za rad sa bazom koriste se **JPA repozitoriji**, dok Spring Security obezbjeđuje autentifikaciju i kontrolu pristupa aplikaciji.

---

## Pokretanje aplikacije
### Konfiguracija baze podataka

1. Pokrenuti MySQL Server.
2. U MySQL klijentu (npr. MySQL Workbench) kreirati bazu podataka
3. CREATE DATABASE biblioteka_projekat;

### Preduslovi

Za pokretanje aplikacije potrebno je da su instalirani sljedeći alati:
- **Java JDK 21.0.8**
- **MySQL Server**
- **IntelliJ IDEA** (preporučeno razvojno okruženje)
- **Maven**

### Pristup aplikaciji

Nakon uspješnog pokretanja aplikacije, sistem je dostupan putem web preglednika na sljedećoj adresi:
http://localhost:8080

Korisnik se zatim može prijaviti u aplikaciju korištenjem postojećih korisničkih podataka i nastaviti rad u skladu sa dodijeljenom ulogom (ADMIN ili USER).


## Autori / Tim

Halilović Arduana, Kos Adna, Lusija Merjem, Cogo Amer



