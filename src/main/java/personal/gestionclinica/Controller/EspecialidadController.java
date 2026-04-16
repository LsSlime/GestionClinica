package personal.gestionclinica.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import personal.gestionclinica.model.Especialidad;
import personal.gestionclinica.service.EspecialidadService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;

@Controller
@RequestMapping("/especialidades")
public class EspecialidadController {

    private final EspecialidadService especialidadService;

    @Autowired
    public EspecialidadController(EspecialidadService especialidadService) {
        this.especialidadService = especialidadService;
    }

    @GetMapping
    public String listarEspecialidades(Model model) {
        model.addAttribute("especialidades", especialidadService.listarTodos());
        return "Menu_Crear_Especialidad";
    }

    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("especialidad", new Especialidad());
        return "Formulario_Crear_Especialidad";
    }

    @PostMapping("/guardar")
    public String guardarEspecialidad(@ModelAttribute Especialidad especialidad, 
                                    @RequestParam(value = "archivo", required = false) MultipartFile archivo,
                                    Model model) {
        try {
            if (archivo != null && !archivo.isEmpty()) {
                especialidadService.guardarEspecialidadConImagen(especialidad, archivo);
            } else {
                especialidadService.guardarEspecialidad(especialidad);
            }
            return "redirect:/especialidades";
        } catch (IOException e) {
            model.addAttribute("especialidad", especialidad);
            model.addAttribute("error", "Error al guardar la imagen: " + e.getMessage());
            return "Formulario_Crear_Especialidad";
        } catch (RuntimeException e) {
            model.addAttribute("especialidad", especialidad);
            model.addAttribute("error", e.getMessage());
            return "Formulario_Crear_Especialidad";
        }
    }
}
