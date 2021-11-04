package com.anma.services

import com.anma.models.Content
import com.anma.models.Metadata
import com.anma.models.Plain
import com.anma.models.Space
import com.anma.models.SpaceDescription
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mashape.unirest.http.Unirest

class SpaceService {

    static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    static def getSpace(CONF_URL, TOKEN, spaceKey) {
        //def URL = "http://example.com/confluence/rest/api/space?spaceKey=TST&spaceKey=ds\n"
        def expand = "homepage"
        def response = Unirest.get("${CONF_URL}/rest/api/space?spaceKey=${spaceKey}&expand=${expand}")
                .header("Authorization", "Basic ${TOKEN}")
                .asString()

        return gson.fromJson(response.body, Space.class)

    }

    static def createSpace(CONF_URL, TOKEN, spaceKey, name) {
        /*
        https://docs.atlassian.com/ConfluenceServer/rest/7.5.0/#api/space-update
        {
            "key": "TST",
            "name": "Example space",
            "description": {
                "plain": {
                    "value": "This is an example space",
                    "representation": "plain"
                }
            },
            "metadata": {}
            }
         */

        def space = new Space()
        space.key = spaceKey
        space.name = name
        SpaceDescription description = new SpaceDescription()
        Plain plain = new Plain()
        plain.representation = "plain"
        plain.value = ""
        description.plain = plain
        space.description = description
        space.metadata = new Metadata()

        def response = Unirest.post("${CONF_URL}/rest/api/space")
                .header("Authorization", "Basic ${TOKEN}")
                .header("Content-Type", "application/json")
                .body(gson.toJson(space))
                .asString()

        return gson.fromJson(response.body, Space.class)

    }
}
