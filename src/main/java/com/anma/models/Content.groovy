package com.anma.models

class Content {

    def id
    def title
    def type
    def status
    Ancestor[] ancestors
    Container container
    Version version
    Body body
    Space space

    @Override
    String toString() {
        return "id: ${id} | title: ${title} | type: ${type} | Version: ${version} | Body: ${body} "
    }
}
