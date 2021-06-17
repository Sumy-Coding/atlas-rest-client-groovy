package com.anma

import com.anma.models.Content
import com.anma.models.Contents

class GetPageData {

    static def getChildrenIds(id) {
        Contents pages = GsonService.httpToGson(GetPageHttp.getPage(id))
        return pages


    }
}
