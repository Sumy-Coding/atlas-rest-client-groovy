package com.anma


import com.anma.models.Contents

class GetPageData {

    static def getChildrenIds(id) {

        Contents pages = GsonService.httpToGson(PageService.getPage(id))


        return pages

    }
}
