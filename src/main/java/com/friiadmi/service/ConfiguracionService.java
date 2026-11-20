package com.friiadmi.service;

import com.friiadmi.dto.ConfiguracionDTO;
import com.friiadmi.model.Configuracion;
import com.friiadmi.repository.ConfiguracionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConfiguracionService {

    private final ConfiguracionRepository configuracionRepository;

    public ConfiguracionService(ConfiguracionRepository configuracionRepository) {
        this.configuracionRepository = configuracionRepository;
    }

    /** Devuelve la configuración del torneo; si no existe la crea con los valores por defecto. */
    @Transactional
    public Configuracion obtener() {
        return configuracionRepository.findById(Configuracion.ID_UNICO)
                .orElseGet(() -> configuracionRepository.save(new Configuracion()));
    }

    @Transactional
    public Configuracion actualizar(ConfiguracionDTO dto) {
        Configuracion config = obtener();
        config.setNombreTorneo(dto.getNombreTorneo().trim());
        config.setComisionPorcentaje(dto.getComisionPorcentaje());
        config.setCuotaInscripcion(dto.getCuotaInscripcion());
        return configuracionRepository.save(config);
    }
}
