package com.friiadmi.controller;

import com.friiadmi.dto.PartidaDTO;
import com.friiadmi.dto.ResultadoDTO;
import com.friiadmi.exception.FriiadmiException;
import com.friiadmi.service.EquipoService;
import com.friiadmi.service.PartidaService;
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

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Controller
@RequestMapping("/partidas")
public class PartidaController {

    private static final List<String> MAPAS = List.of("Bermuda", "Purgatorio", "Kalahari", "Alpes", "NeXTerra", "Solara");

    private final PartidaService partidaService;
    private final EquipoService equipoService;

    public PartidaController(PartidaService partidaService, EquipoService equipoService) {
        this.partidaService = partidaService;
        this.equipoService = equipoService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("partidas", partidaService.listar());
        model.addAttribute("equipos", equipoService.listar());
        model.addAttribute("posiciones", equipoService.tablaPosiciones());
        model.addAttribute("mapas", MAPAS);
        model.addAttribute("hoy", LocalDate.now(ZoneId.of("America/Lima")));
        return "partidas";
    }

    @PostMapping("/guardar")
    public String crear(@Valid @ModelAttribute("partidaForm") PartidaDTO dto,
                        BindingResult resultado,
                        RedirectAttributes ra) {
        if (resultado.hasErrors()) {
            ra.addFlashAttribute("error", Mensajes.errores(resultado));
            return "redirect:/partidas";
        }
        partidaService.crear(dto);
        ra.addFlashAttribute("exito", "Partida creada. Ya puedes registrar los puntos de cada equipo.");
        return "redirect:/partidas";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        try {
            partidaService.eliminar(id);
            ra.addFlashAttribute("exito", "Partida eliminada y puntos recalculados.");
        } catch (FriiadmiException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/partidas";
    }

    @PostMapping("/resultados/guardar")
    public String registrarResultado(@Valid @ModelAttribute("resultadoForm") ResultadoDTO dto,
                                     BindingResult resultado,
                                     RedirectAttributes ra) {
        if (resultado.hasErrors()) {
            ra.addFlashAttribute("error", Mensajes.errores(resultado));
            return "redirect:/partidas";
        }
        try {
            partidaService.registrarResultado(dto);
            ra.addFlashAttribute("exito", "Resultado registrado. La tabla de posiciones se actualizó.");
        } catch (FriiadmiException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/partidas";
    }

    @PostMapping("/resultados/{id}/eliminar")
    public String eliminarResultado(@PathVariable Long id, RedirectAttributes ra) {
        try {
            partidaService.eliminarResultado(id);
            ra.addFlashAttribute("exito", "Resultado eliminado y puntos recalculados.");
        } catch (FriiadmiException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/partidas";
    }
}
