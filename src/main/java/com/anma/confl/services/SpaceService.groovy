package com.anma.confl.services


import com.anma.confl.models.Metadata
import com.anma.confl.models.Plain
import com.anma.confl.models.Space
import com.anma.confl.models.SpaceDescription
import com.anma.confl.models.space.CreateSpace
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kong.unirest.Unirest

class SpaceService {

    static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    static Space getSpace(CONF_URL, TOKEN, spaceKey) {
        //def URL = "http://example.com/confluence/rest/api/space?spaceKey=TST&spaceKey=ds\n"
        def expand = "homepage"
        def response = Unirest.get("${CONF_URL}/rest/api/space/${spaceKey}?expand=${expand}")
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

        def space = new CreateSpace()
        space.key = spaceKey
        space.name = name
        SpaceDescription description = new SpaceDescription()
        Plain plain = new Plain()
        plain.representation = "plain"
        plain.value = ""
        description.plain = plain
        space.description = description
        space.metadata = new Metadata()

//        println(gson.toJson(space))

        def response = Unirest.post("${CONF_URL}/rest/api/space")
                .header("Authorization", "Basic ${TOKEN}")
                .header("Content-Type", "application/json")
                .body(gson.toJson(space))
                .asString()

        return gson.fromJson(response.body, Space.class)

    }
}
