package com.friiadmi.controller;

import com.friiadmi.dto.EquipoDTO;
import com.friiadmi.exception.FriiadmiException;
import com.friiadmi.service.EquipoService;
import com.friiadmi.service.JugadorService;
import com.friiadmi.util.Mensajes;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/equipos")
public class EquipoController {

    private final EquipoService equipoService;
    private final JugadorService jugadorService;

    public EquipoController(EquipoService equipoService, JugadorService jugadorService) {
        this.equipoService = equipoService;
        this.jugadorService = jugadorService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("equipos", equipoService.listar());
        model.addAttribute("jugadores", jugadorService.listar());
        return "equipos";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("equipoForm") EquipoDTO dto,
                          BindingResult resultado,
                          RedirectAttributes ra) {
        if (resultado.hasErrors()) {
            ra.addFlashAttribute("error", Mensajes.errores(resultado));
            return "redirect:/equipos";
        }
        try {
            boolean esNuevo = dto.getId() == null;
            equipoService.guardar(dto);
            ra.addFlashAttribute("exito", esNuevo ? "Equipo creado correctamente." : "Equipo actualizado correctamente.");
        } catch (FriiadmiException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/equipos";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        try {
            equipoService.eliminar(id);
            ra.addFlashAttribute("exito", "Equipo eliminado. Sus jugadores quedaron sin equipo.");
        } catch (FriiadmiException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/equipos";
    }
}
