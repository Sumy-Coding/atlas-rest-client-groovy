package com.anma.confl.models

class Content {
    def id
    String title
    String type
    def status
    Ancestor[] ancestors
    Container container
    Version version
    Body body
    Space space
    Links _links

    @Override
    String toString() {
        return "id: ${id} | title: ${title} | type: ${type} | Version: ${version} | Body: ${body} "
    }
}
