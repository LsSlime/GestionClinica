package personal.gestionclinica.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdministradorController {

    @GetMapping("/admin/menu")
    public String mostrarMenuAdministrador(){
        return "Menu_Administrador";
    }

    @GetMapping({"/", "/menu"})
    public String mostrarMenuPrincipal() {
        return "MenuPrincipal";
    }

}
