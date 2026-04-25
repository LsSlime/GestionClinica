package personal.gestionclinica.Controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import personal.gestionclinica.model.Usuario;

@Controller
public class AdministradorController {

    // Muestra el menú principal del administrador tras validar sesión
    @GetMapping("/admin/menu")
    public String mostrarMenuAdministrador(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null || !"ADMIN".equals(usuario.getRol())) {
            return "redirect:/login";
        }
        return "Menu_Administrador";
    }

    // Carga la página de inicio o menú principal del sitio
    @GetMapping({"/", "/menu"})
    public String mostrarMenuPrincipal() {
        return "MenuPrincipal";
    }

}
