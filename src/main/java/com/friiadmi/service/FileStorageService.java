package com.friiadmi.service;

import com.friiadmi.exception.FriiadmiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

/** Guarda los comprobantes de pago (capturas de Yape/Plin o PDF) en disco. */
@Service
public class FileStorageService {

    private static final Set<String> EXTENSIONES = Set.of("jpg", "jpeg", "png", "webp", "pdf");

    private final Path raiz;

    public FileStorageService(@Value("${friiadmi.uploads.dir}") String directorio) {
        this.raiz = Paths.get(directorio).toAbsolutePath().normalize();
        try {
            Files.createDirectories(raiz);
        } catch (IOException e) {
            throw new IllegalStateException("FRIIADMI no pudo crear la carpeta de comprobantes: " + raiz, e);
        }
    }

    public Path getRaiz() {
        return raiz;
    }

    /** Guarda el archivo y devuelve su nombre, o null si no se envió ninguno. */
    public String guardar(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            return null;
        }
        String original = archivo.getOriginalFilename() == null ? "" : archivo.getOriginalFilename();
        int punto = original.lastIndexOf('.');
        String extension = punto >= 0 ? original.substring(punto + 1).toLowerCase(Locale.ROOT) : "";
        if (!EXTENSIONES.contains(extension)) {
            throw new FriiadmiException("El comprobante debe ser una imagen (JPG, PNG, WEBP) o un PDF.");
        }
        String nombre = "FRIIADMI-" + UUID.randomUUID() + "." + extension;
        try (InputStream in = archivo.getInputStream()) {
            Files.copy(in, raiz.resolve(nombre), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FriiadmiException("No se pudo guardar el comprobante. Inténtalo nuevamente.");
        }
        return nombre;
    }

    public void eliminar(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            return;
        }
        try {
            Path archivo = raiz.resolve(nombre).normalize();
            if (archivo.startsWith(raiz)) {
                Files.deleteIfExists(archivo);
            }
        } catch (IOException ignorado) {
            // Si no se puede borrar el archivo, no se bloquea la operación del administrador.
        }
    }
}
