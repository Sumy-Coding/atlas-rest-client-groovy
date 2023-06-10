package com.anma.confl.models

class Label {
    String prefix
    String name
    long id


    @Override
    public String toString() {
        return "Label{" +
                "prefix='" + prefix + '\'' +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
