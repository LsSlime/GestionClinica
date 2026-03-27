package personal.gestionclinica.Controller;

import personal.gestionclinica.model.Paciente;
import personal.gestionclinica.service.PacienteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PacienteController {

    private final PacienteService pacienteService;

    @Autowired
    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @GetMapping({"/", "/menu"})
    public String mostrarMenuPrincipal() {
        return "MenuPrincipal";
    }

    @GetMapping("/pacientes")
    public String listarPacientes(Model model) {
        model.addAttribute("pacientes", pacienteService.listarTodosLosPacientes());
        return "Menu_Crear_Paciente";
    }

    @GetMapping("/pacientes/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("paciente", new Paciente());
        return "Formulario_Crear_Paciente";
    }

    @PostMapping("/pacientes/guardar")
    public String guardarPaciente(@ModelAttribute Paciente paciente, Model model) {
        try {
            pacienteService.guardarPaciente(paciente);
            return "redirect:/pacientes";
        } catch (RuntimeException e) {
            model.addAttribute("paciente", paciente);
            model.addAttribute("error", e.getMessage());
            return "Formulario_Crear_Paciente";
        }
    }

    @PostMapping("/pacientes/actualizar")
    public String actualizarPaciente(@ModelAttribute Paciente paciente, Model model) {
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

    @GetMapping("/pacientes/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable int id, Model model) {
        Paciente paciente = pacienteService.buscarPacientePorId(id);
        if (paciente == null) {
            return "redirect:/pacientes";
        }
        model.addAttribute("paciente", paciente);
        return "Formulario_Crear_Paciente";
    }

    @PostMapping("/pacientes/eliminar/{id}")
    public String eliminarPaciente(@PathVariable int id, Model model) {
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
