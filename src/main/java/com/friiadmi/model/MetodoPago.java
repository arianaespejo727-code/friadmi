package com.friiadmi.model;

public enum MetodoPago {
    YAPE("Yape"),
    PLIN("Plin");

    private final String label;

    MetodoPago(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
