package personal.gestionclinica.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import personal.gestionclinica.service.CitaService;
import personal.gestionclinica.service.EspecialidadService;
import personal.gestionclinica.service.MedicoService;

@Controller
@RequestMapping("/citas")
public class CitaController {

    private final CitaService citaService;
    private final MedicoService medicoService;
    private final EspecialidadService especialidadService;

    public CitaController(CitaService citaService, MedicoService medicoService, EspecialidadService especialidadService) {
        this.citaService = citaService;
        this.medicoService = medicoService;
        this.especialidadService = especialidadService;
    }




}
