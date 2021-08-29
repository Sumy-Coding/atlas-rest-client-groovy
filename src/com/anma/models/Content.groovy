package com.anma.models

class Content {

    def id
    def title
    def type
    def status
    def ancestors
    Version version
    Body body

    @Override
    String toString() {
        return "id: ${id} | title: ${title} | type: ${type} | Version: ${version.number} | Body: ${body.storage.value} "
    }
}
