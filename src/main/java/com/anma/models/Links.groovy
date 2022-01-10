package com.anma.models

class Links {

    String webui
    String download
    String collection
    String base
    String context
    String self

    @Override
    public String toString() {
        return "Links{" +
                "webui='" + webui + '\'' +
                ", download='" + download + '\'' +
                ", collection='" + collection + '\'' +
                ", base='" + base + '\'' +
                ", context='" + context + '\'' +
                ", self='" + self + '\'' +
                '}';
    }
}
