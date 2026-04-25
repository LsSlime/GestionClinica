package personal.gestionclinica.Controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;
import personal.gestionclinica.model.Paciente;
import personal.gestionclinica.model.Usuario;
import personal.gestionclinica.service.CitaService;
import personal.gestionclinica.service.PacienteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class PacienteController {

    private final PacienteService pacienteService;
    private final CitaService citaService;

    @Autowired
    public PacienteController(PacienteService pacienteService, CitaService citaService) {
        this.pacienteService = pacienteService;
        this.citaService = citaService;
    }


    // Muestra la lista de pacientes con opción de búsqueda (Solo Admin)
    @GetMapping("/pacientes")
    public String listarPacientes(@RequestParam(name = "q", required = false)String q, HttpSession session, Model model) {
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioLogueado == null || !"ADMIN".equals(usuarioLogueado.getRol())) {
            return "redirect:/login";
        }

        model.addAttribute("q", q);
        model.addAttribute("pacientes", pacienteService.buscarPacientes(q));
        return "Menu_Crear_Paciente";
    }

    // Muestra el panel personal del paciente con sus datos y citas
    @GetMapping("/paciente/menu")
    public String mostrarMenuPaciente(HttpSession session, Model model) {
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        
        if (usuarioLogueado == null || !"PACIENTE".equals(usuarioLogueado.getRol())) {
            return "redirect:/login";
        }
        
        Paciente paciente = pacienteService.buscarPacientePorId(usuarioLogueado.getId());
        if (paciente != null) {
            model.addAttribute("paciente", paciente);
            model.addAttribute("citas", citaService.obtenerCitasPorPaciente(paciente.getId()));
        }
        
        return "Menu_Pacientes";
    }

    // Muestra el formulario para registrar un nuevo paciente
    @GetMapping("/pacientes/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("paciente", new Paciente());
        return "Formulario_Crear_Paciente";
    }

    // Registra un nuevo paciente en el sistema
    @PostMapping("/pacientes/guardar")
    public String guardarPaciente(@ModelAttribute Paciente paciente, HttpSession session, Model model) {
        try {
            pacienteService.guardarPaciente(paciente);
            
            Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
            if (usuarioLogueado != null && "ADMIN".equals(usuarioLogueado.getRol())) {
                return "redirect:/pacientes";
            }
            
            return "redirect:/?success=registro";
        } catch (RuntimeException e) {
            model.addAttribute("paciente", paciente);
            model.addAttribute("error", e.getMessage());
            return "Formulario_Crear_Paciente";
        }
    }

    // Actualiza la información de un paciente existente
    @PostMapping("/pacientes/actualizar")
    public String actualizarPaciente(@ModelAttribute Paciente paciente, HttpSession session, Model model) {
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioLogueado == null || !"ADMIN".equals(usuarioLogueado.getRol())) {
            return "redirect:/login";
        }
        try {
            pacienteService.actualizarPaciente(paciente);
            return "redirect:/pacientes";
        } catch (RuntimeException e) {
            Paciente existente = pacienteService.buscarPacientePorId(paciente.getId());
            if (existente != null) {
                paciente.setDni(existente.getDni());
                paciente.setGenero(existente.getGenero());
            }

            model.addAttribute("paciente", paciente);
            model.addAttribute("error", e.getMessage());
            return "Formulario_Crear_Paciente";
        }
    }

    // Carga el formulario con los datos del paciente para editar
    @GetMapping("/pacientes/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable int id, HttpSession session, Model model) {
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioLogueado == null || !"ADMIN".equals(usuarioLogueado.getRol())) {
            return "redirect:/login";
        }
        Paciente paciente = pacienteService.buscarPacientePorId(id);
        if (paciente == null) {
            return "redirect:/pacientes";
        }
        model.addAttribute("paciente", paciente);
        return "Formulario_Crear_Paciente";
    }

    // Elimina un paciente del sistema (Solo Admin)
    @PostMapping("/pacientes/eliminar/{id}")
    public String eliminarPaciente(@PathVariable int id, HttpSession session, Model model) {
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioLogueado == null || !"ADMIN".equals(usuarioLogueado.getRol())) {
            return "redirect:/login";
        }
        try {
            pacienteService.eliminarPaciente(id);
            return "redirect:/pacientes";
        } catch (RuntimeException e) {
            model.addAttribute("pacientes", pacienteService.listarTodosLosPacientes());
            model.addAttribute("error", e.getMessage());
            return "Menu_Crear_Paciente";
        }
    }
}
