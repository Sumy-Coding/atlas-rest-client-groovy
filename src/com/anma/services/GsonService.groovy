package com.anma.services

import com.anma.models.Content
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonObject

import java.net.http.HttpResponse

class GsonService {

    static def httpToGson(HttpResponse<String> response, Class aClass) {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

//        JsonObject jsonObject = gson.fromJson(response.body(), JsonObject.class);
//        JsonElement id = jsonObject.get("id");
//        String title = jsonObject.get("title").getAsString();
//
//        JsonObject body = jsonObject.get("body").getAsJsonObject();
//        JsonObject bodyView = body.get("view").getAsJsonObject();
//        JsonElement bodyValue = bodyView.get("value");
//        long pageVersion = jsonObject.get("version").getAsJsonObject().get("number").getAsLong();

        return gson.fromJson(response.body(), aClass.class)

    }
}
