package personal.gestionclinica.Controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import personal.gestionclinica.model.Especialidad;
import personal.gestionclinica.model.Medico;
import personal.gestionclinica.model.Usuario;
import personal.gestionclinica.service.CitaService;
import personal.gestionclinica.service.EspecialidadService;
import personal.gestionclinica.service.MedicoService;

import java.util.Collections;

@Controller
public class MedicoController {

    private final MedicoService medicoService;
    private final EspecialidadService especialidadService;
    private final CitaService citaService;

    @Autowired
    public MedicoController(MedicoService medicoService, EspecialidadService especialidadService, CitaService citaService) {
        this.medicoService = medicoService;
        this.especialidadService = especialidadService;
        this.citaService = citaService;
    }

    @GetMapping("/medicos")
    public String listarMedicos(@RequestParam(name = "q", required = false) String q, HttpSession session, Model model) {
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioLogueado == null || !"ADMIN".equals(usuarioLogueado.getRol())) {
            return "redirect:/login";
        }
        model.addAttribute("q", q);
        model.addAttribute("medicos", medicoService.buscarMedicos(q));
        return "Menu_Crear_Medico";
    }

    @GetMapping("/medicos/menu")
    public String mostrarMenuMedico(HttpSession session, Model model) {
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        
        if (usuarioLogueado == null || !"MEDICO".equals(usuarioLogueado.getRol())) {
            return "redirect:/login";
        }
        
        Medico medico = medicoService.obtenerMedicoPorId(usuarioLogueado.getId());
        if (medico != null) {
            model.addAttribute("medico", medico);
            model.addAttribute("citas", citaService.obtenerCitasPorMedico(medico.getId()));
        }
        
        return "Menu_Medicos";
    }

    @GetMapping("/medicos/nuevo")
    public String mostrarFormulario(HttpSession session, Model model) {
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioLogueado == null || !"ADMIN".equals(usuarioLogueado.getRol())) {
            return "redirect:/login";
        }
        Medico medico = new Medico();
        medico.setEspecialidad(new Especialidad());
        model.addAttribute("medico", medico);
        cargarEspecialidades(model);
        return "Formulario_Crear_Medico";
    }

    @PostMapping("/medicos/guardar")
    public String guardarMedico(@ModelAttribute Medico medico, HttpSession session, Model model) {
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioLogueado == null || !"ADMIN".equals(usuarioLogueado.getRol())) {
            return "redirect:/login";
        }
        try {
            medico.setEspecialidad(obtenerEspecialidadValida(obtenerEspecialidadId(medico)));
            medicoService.guardarMedico(medico);
            return "redirect:/medicos";
        } catch (RuntimeException e) {
            prepararEspecialidadParaFormulario(medico);
            model.addAttribute("medico", medico);
            model.addAttribute("error", e.getMessage());
            cargarEspecialidades(model);
            return "Formulario_Crear_Medico";
        }
    }

    @PostMapping("/medicos/actualizar")
    public String actualizarMedico(@ModelAttribute Medico medico, HttpSession session, Model model) {
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioLogueado == null || !"ADMIN".equals(usuarioLogueado.getRol())) {
            return "redirect:/login";
        }
        try {
            Medico existente = medicoService.obtenerMedicoPorId(medico.getId());
            if (existente == null) {
                return "redirect:/medicos";
            }

            medico.setDni(existente.getDni());
            medico.setGenero(existente.getGenero());
            medico.setEspecialidad(obtenerEspecialidadValida(obtenerEspecialidadId(medico)));
            medicoService.actualizarMedico(medico);
            return "redirect:/medicos";
        } catch (RuntimeException e) {
            Medico existente = medicoService.obtenerMedicoPorId(medico.getId());
            if (existente != null) {
                medico.setDni(existente.getDni());
                medico.setGenero(existente.getGenero());
            }

            prepararEspecialidadParaFormulario(medico);
            model.addAttribute("medico", medico);
            model.addAttribute("error", e.getMessage());
            cargarEspecialidades(model);
            return "Formulario_Crear_Medico";
        }
    }

    @GetMapping("/medicos/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable int id, HttpSession session, Model model) {
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioLogueado == null || !"ADMIN".equals(usuarioLogueado.getRol())) {
            return "redirect:/login";
        }
        Medico medico = medicoService.obtenerMedicoPorId(id);
        if (medico == null) {
            return "redirect:/medicos";
        }
        prepararEspecialidadParaFormulario(medico);
        model.addAttribute("medico", medico);
        cargarEspecialidades(model);
        return "Formulario_Crear_Medico";
    }

    @PostMapping("/medicos/eliminar/{id}")
    public String eliminarMedico(@PathVariable int id, HttpSession session, Model model) {
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioLogueado == null || !"ADMIN".equals(usuarioLogueado.getRol())) {
            return "redirect:/login";
        }
        try {
            medicoService.eliminarMedico(id);
            return "redirect:/medicos";
        } catch (RuntimeException e) {
            model.addAttribute("medicos", medicoService.listarTodosLosMedicos());
            model.addAttribute("error", e.getMessage());
            return "Menu_Crear_Medico";
        }
    }

    @GetMapping("/medicos/por-especialidad/{id}")
    @ResponseBody
    public java.util.List<Medico> listarMedicosPorEspecialidad(@PathVariable int id) {
        return medicoService.listarMedicosPorEspecialidad(id);
    }

    private void cargarEspecialidades(Model model) {
        try {
            model.addAttribute("especialidades", especialidadService.listarTodos());
        } catch (RuntimeException e) {
            model.addAttribute("especialidades", Collections.emptyList());
            if (!model.containsAttribute("error")) {
                model.addAttribute("error", "No se pudieron cargar las especialidades. Crea una nueva desde /especialidades/nuevo.");
            }
        }
    }

    private Especialidad obtenerEspecialidadValida(int especialidadId) {
        Especialidad especialidad = especialidadService.obtenerPorId(especialidadId);
        if (especialidad == null) {
            throw new IllegalArgumentException("La especialidad seleccionada no existe.");
        }
        return especialidad;
    }

    private int obtenerEspecialidadId(Medico medico) {
        if (medico.getEspecialidad() == null || medico.getEspecialidad().getId() == null || medico.getEspecialidad().getId() <= 0) {
            throw new IllegalArgumentException("Debes seleccionar una especialidad.");
        }
        return medico.getEspecialidad().getId();
    }

    private void prepararEspecialidadParaFormulario(Medico medico) {
        if (medico.getEspecialidad() == null) {
            medico.setEspecialidad(new Especialidad());
        }
    }
}
