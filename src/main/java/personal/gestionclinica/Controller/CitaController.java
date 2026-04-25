package personal.gestionclinica.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import personal.gestionclinica.model.Citas;
import personal.gestionclinica.model.Medico;
import personal.gestionclinica.model.Paciente;
import personal.gestionclinica.model.Usuario;
import personal.gestionclinica.service.CitaService;
import personal.gestionclinica.service.EspecialidadService;
import personal.gestionclinica.service.MedicoService;
import personal.gestionclinica.service.PacienteService;

import jakarta.servlet.http.HttpSession;
import java.util.Collections;

@Controller
@RequestMapping("/citas")
public class CitaController {

    private final CitaService citaService;
    private final PacienteService pacienteService;
    private final MedicoService medicoService;
    private final EspecialidadService especialidadService;

    @Autowired
    public CitaController(CitaService citaService, PacienteService pacienteService, MedicoService medicoService, EspecialidadService especialidadService) {
        this.citaService = citaService;
        this.pacienteService = pacienteService;
        this.medicoService = medicoService;
        this.especialidadService = especialidadService;
    }

    //CON EL HTTP Session se guarda la sesion para el login hasta que se haga un logout



    // Muestra el formulario para que el paciente agende una cita
    @GetMapping("/nueva")
    public String mostrarFormulario(Model model, HttpSession session) {
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        
        // Solo los pacientes y admins pueden agendar citas
        if (usuarioLogueado == null || (!"PACIENTE".equals(usuarioLogueado.getRol()) && !"ADMIN".equals(usuarioLogueado.getRol()))) {
            return "redirect:/login";
        }
        
        Citas cita = new Citas();
        if ("PACIENTE".equals(usuarioLogueado.getRol())) {
            Paciente paciente = pacienteService.buscarPacientePorId(usuarioLogueado.getId());
            cita.setPaciente(paciente != null ? paciente : new Paciente());
        } else {
            cita.setPaciente(new Paciente()); // Para admin, paciente vacío para seleccionar
        }
        cita.setMedico(new Medico());
        prepararFormulario(model, cita, null, null);
        model.addAttribute("isAdmin", "ADMIN".equals(usuarioLogueado.getRol()));
        return "Formulario_Agendar_Cita";
    }

    // Lista todas las citas existentes (Solo Admin)
    @GetMapping
    public String listarCitas(@RequestParam(name = "q", required = false) String q, HttpSession session, Model model) {
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioLogueado == null || !"ADMIN".equals(usuarioLogueado.getRol())) {
            return "redirect:/login";
        }
        model.addAttribute("q", q);
        model.addAttribute("citas", citaService.buscarCitas(q));
        return "Menu_citas";
    }


    //======== METODO PARA GUARDAR LA CITA SEGUN EL USUARIO LOGEADO Y EL ROL DE ESTE

    // Guarda la cita tras validar que el paciente agende para sí mismo
    @PostMapping("/guardar")
    public String guardarCita(@ModelAttribute Citas cita,
                              @RequestParam(name = "especialidadId", required = false) Integer especialidadId,
                              HttpSession session,
                              Model model) {
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        
        // Validar que el usuario es un paciente o admin
        if (usuarioLogueado == null || (!"PACIENTE".equals(usuarioLogueado.getRol()) && !"ADMIN".equals(usuarioLogueado.getRol()))) {
            return "redirect:/login";
        }
        
        // Validar que el paciente seleccionado sea el logueado (solo para pacientes)
        if ("PACIENTE".equals(usuarioLogueado.getRol()) && (cita.getPaciente() == null || cita.getPaciente().getId() != usuarioLogueado.getId())) {
            if (cita.getPaciente() == null) {
                cita.setPaciente(new Paciente());
            }
            if (cita.getMedico() == null) {
                cita.setMedico(new Medico());
            }
            prepararFormulario(model, cita, especialidadId, "Solo puedes agendar citas para ti mismo.");
            return "Formulario_Agendar_Cita";
        }
        
        try {
            citaService.guardarCita(cita);
            if ("PACIENTE".equals(usuarioLogueado.getRol())) {
                return "redirect:/paciente/menu";
            } else {
                return "redirect:/admin/menu";
            }
        } catch (RuntimeException e) {
            if (cita.getPaciente() == null) {
                cita.setPaciente(new Paciente());
            }
            if (cita.getMedico() == null) {
                cita.setMedico(new Medico());
            }
            prepararFormulario(model, cita, especialidadId, e.getMessage());
            return "Formulario_Agendar_Cita";
        }
    }

    //============= LISTA LAS CITAS DE UN PACIENTE ESPECÍFICO
    @GetMapping("/paciente/{id}")
    public String citasPorPaciente(@PathVariable int id, HttpSession session, Model model) {
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioLogueado == null || (!"ADMIN".equals(usuarioLogueado.getRol()) && usuarioLogueado.getId() != id)) {
            return "redirect:/login";
        }
        model.addAttribute("citas", citaService.obtenerCitasPorPaciente(id));
        return "Menu_citas";
    }


    //============= CANCELA UNA CITA (protección: no cancela citas completadas)
    @PostMapping("/cancelar/{id}")
    public String cancelarCita(@PathVariable int id, HttpSession session) {
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");

        // Protección backend: no se puede cancelar una cita completada
        Citas cita = citaService.listarTodasLasCitas().stream()
                .filter(c -> c.getId() == id)
                .findFirst().orElse(null);

        if (cita != null && "completada".equalsIgnoreCase(cita.getEstado())) {
            // Redirige según rol del usuario logueado
            if (usuarioLogueado != null && "MEDICO".equals(usuarioLogueado.getRol())) {
                return "redirect:/medicos/menu";
            }
            if (usuarioLogueado != null && "PACIENTE".equals(usuarioLogueado.getRol())) {
                return "redirect:/paciente/menu";
            }
            return "redirect:/citas";
        }
        // Solo llega aquí si la cita NO está completada → se cancela
            citaService.cancelarCita(id);

            if (usuarioLogueado != null && "MEDICO".equals(usuarioLogueado.getRol())) {
                return "redirect:/medicos/menu";
            }
            if (usuarioLogueado != null && "PACIENTE".equals(usuarioLogueado.getRol())) {
                return "redirect:/paciente/menu";
            }

        return "redirect:/citas";
    }

    //============= LISTA LAS CITAS DE UN MÉDICO ESPECÍFICO
    @GetMapping("/medico/{id}")
    public String citasPorMedico(@PathVariable int id, HttpSession session, Model model) {
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioLogueado == null || (!"ADMIN".equals(usuarioLogueado.getRol()) && usuarioLogueado.getId() != id)) {
            return "redirect:/login";
        }
        model.addAttribute("citas", citaService.obtenerCitasPorMedico(id));
        return "Menu_citas";
    }

    private void prepararFormulario(Model model, Citas cita, Integer especialidadId, String error) {
        Integer especialidadSeleccionada = especialidadId;
        if (especialidadSeleccionada == null
                && cita.getMedico() != null
                && cita.getMedico().getId() > 0) {
            Medico medico = medicoService.obtenerMedicoPorId(cita.getMedico().getId());
            if (medico != null && medico.getEspecialidad() != null) {
                especialidadSeleccionada = medico.getEspecialidad().getId();
            }
        }

        model.addAttribute("cita", cita);
        model.addAttribute("pacientes", pacienteService.listarTodosLosPacientes());
        model.addAttribute("especialidades", especialidadService.listarTodos());
        model.addAttribute("especialidadSeleccionada", especialidadSeleccionada);
        model.addAttribute(
                "medicosDisponibles",
                especialidadSeleccionada != null && especialidadSeleccionada > 0
                        ? medicoService.listarMedicosPorEspecialidad(especialidadSeleccionada)
                        : Collections.emptyList());

        if (error != null && !error.isBlank()) {
            model.addAttribute("error", error);
        }
    }

    // Muestra el formulario para que el médico complete los detalles de la consulta
    @GetMapping("/completar/{id}")
    public String mostrarFormularioCompletar(@PathVariable int id, Model model, HttpSession session) {
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioLogueado == null || !"MEDICO".equals(usuarioLogueado.getRol())) {
            return "redirect:/login";
        }

        Citas cita = citaService.listarTodasLasCitas().stream()
                .filter(c -> c.getId() == id && c.getMedico().getId() == usuarioLogueado.getId())
                .findFirst().orElse(null);

        if (cita == null || !esCitaProgramada(cita.getEstado())) {
            return "redirect:/medicos/menu";
        }

        model.addAttribute("cita", cita);
        return "Formulario_Completar_Cita";
    }

    // Procesa el completado de la cita y guarda el diagnóstico
    @PostMapping("/completar")
    public String completarCita(@ModelAttribute Citas cita, HttpSession session, Model model) {
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioLogueado == null || !"MEDICO".equals(usuarioLogueado.getRol())) {
            return "redirect:/login";
        }

        try {
            // Verificar que la cita pertenece al médico
            Citas citaExistente = citaService.listarTodasLasCitas().stream()
                    .filter(c -> c.getId() == cita.getId() && c.getMedico().getId() == usuarioLogueado.getId())
                    .findFirst().orElse(null);

            if (citaExistente == null) {
                return "redirect:/medicos/menu";
            }

            // Actualizar campos
            if (!esCitaProgramada(citaExistente.getEstado())) {
                return "redirect:/medicos/menu";
            }

            citaExistente.setDiagnostico(cita.getDiagnostico());
            citaExistente.setTratamiento(cita.getTratamiento());
            citaExistente.setObservaciones(cita.getObservaciones());
            citaExistente.setEstado("completada");

            citaService.completarCita(citaExistente);
            return "redirect:/medicos/menu";
        } catch (RuntimeException e) {
            model.addAttribute("cita", cita);
            model.addAttribute("error", e.getMessage());
            return "Formulario_Completar_Cita";
        }
    }

    private boolean esCitaProgramada(String estado) {
        return estado == null
                || "programada".equalsIgnoreCase(estado)
                || "pendiente".equalsIgnoreCase(estado);
    }
}
