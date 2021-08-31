package com.anma.models

class Content {

    def id
    def title
    def type
    def status
    def ancestors
    Container container
    Version version
    Body body
    Space space

    @Override
    String toString() {
        return "id: ${id} | title: ${title} | type: ${type} | Version: ${version.number} | Body: ${body.storage.value} "
    }
}
