package com.anma.confl.models

class Space {
    String key
    String spaceKey
    String name
    String type
    SpaceDescription description
    String status
    Metadata metadata
    String label
    def expand
    boolean favourite
    int start
    int limit
    Content homepage

    @Override
    public String toString() {
        return "Space{" +
                "key='" + key + '\'' +
                ", spaceKey='" + spaceKey + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", description=" + description +
                ", status='" + status + '\'' +
                ", metadata=" + metadata +
                ", label='" + label + '\'' +
                ", expand=" + expand +
                ", favourite=" + favourite +
                ", start=" + start +
                ", limit=" + limit +
                '}';
    }
}
