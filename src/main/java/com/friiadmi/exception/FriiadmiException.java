package com.friiadmi.exception;

/** Error de negocio de FRIIADMI. El mensaje se muestra tal cual al administrador. */
public class FriiadmiException extends RuntimeException {

    public FriiadmiException(String mensaje) {
        super(mensaje);
    }
}
