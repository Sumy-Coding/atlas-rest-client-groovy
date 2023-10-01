package com.anma.confl.models

class Version {
    def when
    int number
    def message

    @Override
    public String toString() {
        return "Version{" +
                "when=" + when +
                ", number=" + number +
                ", message=" + message +
                '}';
    }
}
