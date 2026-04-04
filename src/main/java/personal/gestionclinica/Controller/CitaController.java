package personal.gestionclinica.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import personal.gestionclinica.model.Citas;
import personal.gestionclinica.model.Medico;
import personal.gestionclinica.model.Paciente;
import personal.gestionclinica.service.CitaService;
import personal.gestionclinica.service.EspecialidadService;
import personal.gestionclinica.service.MedicoService;
import personal.gestionclinica.service.PacienteService;

import java.util.Collections;

@Controller
@RequestMapping("/citas")
public class CitaController {

    private final CitaService citaService;
    private final PacienteService pacienteService;
    private final MedicoService medicoService;
    private final EspecialidadService especialidadService;

    public CitaController(CitaService citaService, PacienteService pacienteService, MedicoService medicoService, EspecialidadService especialidadService) {
        this.citaService = citaService;
        this.pacienteService = pacienteService;
        this.medicoService = medicoService;
        this.especialidadService = especialidadService;
    }

    @GetMapping("/nueva")
    public String mostrarFormulario(Model model) {
        Citas cita = new Citas();
        cita.setPaciente(new Paciente());
        cita.setMedico(new Medico());
        prepararFormulario(model, cita, null, null);
        return "Formulario_Agendar_Cita";
    }

    @GetMapping
    public String listarCitas(Model model) {
        model.addAttribute("citas", citaService.listarTodasLasCitas());
        return "Menu_citas";
    }


    @PostMapping("/guardar")
    public String guardarCita(@ModelAttribute Citas cita,
                              @RequestParam(name = "especialidadId", required = false) Integer especialidadId,
                              Model model) {
        try {
            citaService.guardarCita(cita);
            return "redirect:/citas";
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

    @GetMapping("/paciente/{id}")
    public String citasPorPaciente(@PathVariable int id, Model model) {
        model.addAttribute("citas", citaService.obtenerCitasPorPaciente(id));
        return "Menu_citas";
    }

    @PostMapping("/cancelar/{id}")
    public String cancelarCita(@PathVariable int id) {
        citaService.cancelarCita(id);
        return "redirect:/citas";
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

}
