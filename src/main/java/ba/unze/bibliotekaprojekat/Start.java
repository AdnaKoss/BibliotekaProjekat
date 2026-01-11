package ba.unze.bibliotekaprojekat;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class Start {

    @GetMapping
    public String home() {
        return "home"; // src/main/resources/templates/home.html
    }

    @GetMapping("/admin_home")
    public String adminHome() {
        return "admin/home-page"; // templates/admin/home-page.html
    }

    @GetMapping("/user_home")
    public String userHome() {
        return "user/homepage"; // templates/user/homepage.html
    }
}
