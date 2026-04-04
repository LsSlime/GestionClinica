package personal.gestionclinica.Controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import personal.gestionclinica.model.Usuario;

public class LoginController {

    //Crear un LoginController
    //•
    //GET /login -> muestra formulario
    //•
    //POST /login -> valida credenciales
    //•
    //GET /logout -> cierra sesión


//    @PostMapping("/login")
//    public String iniciarSesion(@RequestParam String email,
//                                @RequestParam String dni,
//                                HttpSession session,
//                                Model model) {
//        Usuario usuario = authService.autenticar(email, dni);
//
//        if (usuario == null) {
//            model.addAttribute("error", "Credenciales incorrectas.");
//            return "login";
//        }
//
//        session.setAttribute("usuarioLogueado", usuario);
//
//        if ("ADMIN".equals(usuario.getRol())) {
//            return "redirect:/menu";
//        }
//        if ("MEDICO".equals(usuario.getRol())) {
//            return "redirect:/medico/menu";
//        }
//        return "redirect:/paciente/menu";
//    }


}
