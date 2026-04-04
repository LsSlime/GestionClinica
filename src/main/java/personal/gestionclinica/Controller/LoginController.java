package personal.gestionclinica.Controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import personal.gestionclinica.model.Usuario;
import personal.gestionclinica.service.LoginService;


//Crear un LoginController
//•
//GET /login -> muestra formulario
//•
//POST /login -> valida credenciales
//•
//GET /logout -> cierra sesión


@Controller
public class LoginController {

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }


    @GetMapping("/login")
    public String mostrarLogin(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario != null) {
            return redirigirSegunRol (usuario);
        }
        return "Login";
    }


    @PostMapping("/login")
    public String iniciarSesion(@RequestParam String email,
                                @RequestParam String dni,
                                HttpSession session,
                                Model model) {
        try {
            Usuario usuario = loginService.autenticar(email, dni);

            if (usuario == null) {
                model.addAttribute("error", "Correo o Contraseña incorrecta");
                return "Login";
            }

            session.setAttribute("usuarioLogueado", usuario);
            return redirigirSegunRol(usuario);

        }catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "Login";
        }
    }

    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/Login";
    }

    private String redirigirSegunRol(Usuario usuario) {
        if ("ADMIN".equals(usuario.getRol())) {
            return "redirect:/admin/menu";
        }
        if ("MEDICO".equals(usuario.getRol())) {
            return "redirect:/medicos/menu";
        }
        if("PACIENTE".equals(usuario.getRol())) {
             return "redirect:/paciente/menu";
        }
        return "redirect:/Login";
    }
}