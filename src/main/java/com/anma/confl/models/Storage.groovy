package com.anma.confl.models

class Storage {
    String value
    def representation

    @Override
    public String toString() {
        return "Storage{" +
                "value='" + value + '\'' +
                ", representation=" + representation +
                '}';
    }
}
