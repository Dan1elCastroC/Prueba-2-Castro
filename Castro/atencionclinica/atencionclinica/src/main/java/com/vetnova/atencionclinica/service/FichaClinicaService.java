package com.vetnova.atencionclinica.service;

import com.vetnova.atencionclinica.client.MascotaClient;
import com.vetnova.atencionclinica.exception.ResourceNotFoundException;
import com.vetnova.atencionclinica.model.FichaClinica;
import com.vetnova.atencionclinica.repository.FichaClinicaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class FichaClinicaService {

    @Autowired
    private FichaClinicaRepository repository;

    @Autowired
    private MascotaClient mascotaClient;

    @Transactional
    public FichaClinica crearFicha(FichaClinica ficha) {
        log.info("Iniciando creación de ficha clínica para la Mascota ID: {}", ficha.getIdMascota());

        // 1. INTEGRACIÓN PREPARADA: Descomentar cuando MS Mascotas de Gabriel esté listo
        /*
        MascotaDTO datosMascota = mascotaClient.obtenerDatosMascota(ficha.getIdMascota());
        if(datosMascota == null) {
            log.error("Fallo al crear ficha: La mascota con ID {} no existe.", ficha.getIdMascota());
            throw new ResourceNotFoundException("Mascota no encontrada en el sistema central.");
        }
        log.info("Datos obtenidos desde MS Mascotas: {} (Especie: {})", datosMascota.getNombre(), datosMascota.getEspecie());
        */

        log.info("Simulando validación y obtención de datos biológicos vía Feign Client (Mascota ID: {})", ficha.getIdMascota());

        // Vinculamos los diagnósticos con la ficha para respetar la integridad referencial
        if (ficha.getDiagnosticos() != null) {
            ficha.getDiagnosticos().forEach(diag -> diag.setFichaClinica(ficha));
        }

        FichaClinica nuevaFicha = repository.save(ficha);
        log.info("Ficha creada exitosamente con ID: {}", nuevaFicha.getIdFicha());
        return nuevaFicha;
    }

    public List<FichaClinica> obtenerTodas() {
        return repository.findAll();
    }
    
    // Método para buscar una ficha específica por su ID
    public FichaClinica buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("La ficha clínica con ID " + id + " no existe."));
    }
}