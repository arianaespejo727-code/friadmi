package com.friiadmi.model;

public enum EstadoPago {
    PAGADO("Pagado"),
    PENDIENTE("Pendiente");

    private final String label;

    EstadoPago(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
