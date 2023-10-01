package com.anma.confl.models

class View {
    def value
    def representation

    @Override
    public String toString() {
        return "View{" +
                "value=" + value +
                ", representation=" + representation +
                '}';
    }
}
