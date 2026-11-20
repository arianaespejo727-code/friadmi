package com.friiadmi.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Objects;
import java.util.stream.Collectors;

/** Utilidades para mostrar errores de validación al administrador. */
public final class Mensajes {

    private Mensajes() {
    }

    public static String errores(BindingResult resultado) {
        return resultado.getAllErrors().stream()
                .map(e -> (e instanceof FieldError fe && fe.isBindingFailure())
                        ? "Valor inválido en el campo «" + fe.getField() + "»."
                        : e.getDefaultMessage())
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.joining(" "));
    }
}
