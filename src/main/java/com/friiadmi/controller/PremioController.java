package com.friiadmi.controller;

import com.friiadmi.dto.ConfiguracionDTO;
import com.friiadmi.service.ConfiguracionService;
import com.friiadmi.service.PremioService;
import com.friiadmi.util.Mensajes;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/premios")
public class PremioController {

    private final PremioService premioService;
    private final ConfiguracionService configuracionService;

    public PremioController(PremioService premioService, ConfiguracionService configuracionService) {
        this.premioService = premioService;
        this.configuracionService = configuracionService;
    }

    @GetMapping
    public String ver(Model model) {
        model.addAttribute("premio", premioService.calcular());
        model.addAttribute("config", configuracionService.obtener());
        return "premios";
    }

    @PostMapping("/configuracion")
    public String guardarConfiguracion(@Valid @ModelAttribute("configForm") ConfiguracionDTO dto,
                                       BindingResult resultado,
                                       RedirectAttributes ra) {
        if (resultado.hasErrors()) {
            ra.addFlashAttribute("error", Mensajes.errores(resultado));
            return "redirect:/premios";
        }
        configuracionService.actualizar(dto);
        ra.addFlashAttribute("exito", "Configuración guardada. El premio se recalculó con la nueva comisión.");
        return "redirect:/premios";
    }
}
