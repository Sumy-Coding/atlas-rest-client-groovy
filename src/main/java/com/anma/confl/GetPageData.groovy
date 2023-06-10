package com.anma.confl


import com.anma.confl.models.Contents
import com.anma.confl.services.GsonService
import com.anma.confl.services.PageService

class GetPageData {

    static def getChildrenIds(id) {

        Contents pages = GsonService.httpToGson(PageService.getPage(id))


        return pages

    }
}
