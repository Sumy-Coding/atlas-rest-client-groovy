package com.anma


import com.anma.models.Contents
import com.anma.services.GsonService
import com.anma.services.PageService

class GetPageData {

    static def getChildrenIds(id) {

        Contents pages = GsonService.httpToGson(PageService.getPage(id))


        return pages

    }
}
