package com.anma.confl.cloud.v2;

import java.util.Date;

public class ConfluencePage {
    public String parentId;
    public String spaceId;
    public String ownerId;
    public Object lastOwnerId;
    public Date createdAt;
    public String authorId;
    public String parentType;
    public PageVersion version;
    public int position;
    public PageBody body;
    public String status;
    public String title;
    public String id;
    public PageLinks _links;

    public static class PageLinks{
        public String editui;
        public String webui;
        public String edituiv2;
        public String tinyui;
        public String base;
    }

    public static class PageBody {
        public PageBodyStorage storage;
    }

    public static class PageBodyStorage {
        public String representation;
        public String value;
    }

}




