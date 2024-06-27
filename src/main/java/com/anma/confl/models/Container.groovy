package com.anma.confl.models

class Container {
    def id
    def type
    def status
    def title
    Version version
    Body body
    Extensions extensions
    Links _links

    public Container() {}

    public Container(Content content) {
        this.id = content.id
        this.type = content.type
        this.status = content.status
        this.title = content.title
        this.version = content.version
        this.body = content.body
        this.extensions = content.extensions
        this._links = content._links
    }

}
