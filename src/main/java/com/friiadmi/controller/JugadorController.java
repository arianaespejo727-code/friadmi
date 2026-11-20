package com.friiadmi.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.friiadmi.dto.JugadorDTO;
import com.friiadmi.exception.FriiadmiException;
import com.friiadmi.model.EstadoPago;
import com.friiadmi.model.Jugador;
import com.friiadmi.model.MetodoPago;
import com.friiadmi.service.ConfiguracionService;
import com.friiadmi.service.EquipoService;
import com.friiadmi.service.JugadorService;
import com.friiadmi.util.Mensajes;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/jugadores")
public class JugadorController {

    private final JugadorService jugadorService;
    private final EquipoService equipoService;
    private final ConfiguracionService configuracionService;

    public JugadorController(JugadorService jugadorService,
                             EquipoService equipoService,
                             ConfiguracionService configuracionService) {
        this.jugadorService = jugadorService;
        this.equipoService = equipoService;
        this.configuracionService = configuracionService;
    }

    @GetMapping
    public String listar(Model model) {
        List<Jugador> jugadores = jugadorService.listar();
        long pagados = jugadores.stream().filter(j -> j.getEstadoPago() == EstadoPago.PAGADO).count();
        model.addAttribute("jugadores", jugadores);
        model.addAttribute("equipos", equipoService.listar());
        model.addAttribute("config", configuracionService.obtener());
        model.addAttribute("estados", EstadoPago.values());
        model.addAttribute("metodos", MetodoPago.values());
        model.addAttribute("totalPagados", pagados);
        model.addAttribute("totalPendientes", jugadores.size() - pagados);
        return "jugadores";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("jugadorForm") JugadorDTO dto,
                          BindingResult resultado,
                          RedirectAttributes ra) {
        if (resultado.hasErrors()) {
            ra.addFlashAttribute("error", Mensajes.errores(resultado));
            return "redirect:/jugadores";
        }
        try {
            boolean esNuevo = dto.getId() == null;
            jugadorService.guardar(dto);
            ra.addFlashAttribute("exito", esNuevo ? "Jugador registrado correctamente." : "Jugador actualizado correctamente.");
        } catch (FriiadmiException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/jugadores";
    }

    @PostMapping("/{id}/pagar")
    public String marcarPagado(@PathVariable Long id, RedirectAttributes ra) {
        try {
            jugadorService.marcarPagado(id);
            ra.addFlashAttribute("exito", "Pago registrado como pagado.");
        } catch (FriiadmiException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/jugadores";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        System.out.println(">>> INTENTANDO ELIMINAR JUGADOR ID: " + id);
        try {
            jugadorService.eliminar(id);
            ra.addFlashAttribute("exito", "Jugador eliminado.");
        } catch (FriiadmiException e) {
            ra.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            ra.addFlashAttribute("error", "No se pudo eliminar: " + e.getMessage());
        }
        return "redirect:/jugadores";
    }
}