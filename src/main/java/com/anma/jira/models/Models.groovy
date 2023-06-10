package com.anma.jira.models


import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
/* ObjectMapper om = new ObjectMapper();
Root root = om.readValue(myJsonString, Root.class); */
public class Aggregateprogress{
    public int progress;
    public int total;
}

public class AvatarUrls{
    @JsonProperty("48x48")
    public String _48x48;
    @JsonProperty("24x24")
    public String _24x24;
    @JsonProperty("16x16")
    public String _16x16;
    @JsonProperty("32x32")
    public String _32x32;
}

public class Comment{
    public ArrayList<Object> comments;
    public int maxResults;
    public int total;
    public int startAt;
}

public class Creator{
    public String self;
    public String name;
    public String key;
    public String emailAddress;
    public AvatarUrls avatarUrls;
    public String displayName;
    public boolean active;
    public String timeZone;
}

public class Fields{
    public Issuetype issuetype;
    public Object timespent;
    public Project project;
    public ArrayList<Object> fixVersions;
    public Object customfield_10111;
    public Object aggregatetimespent;
    public Object resolution;
    public Object customfield_10107;
    public Object customfield_10108;
    public String customfield_10109;
    public Object resolutiondate;
    public int workratio;
    public Date lastViewed;
    public Watches watches;
    public Date created;
    public Priority priority;
    public Object customfield_10100;
    public Object customfield_10101;
    public Object customfield_10102;
    public ArrayList<Object> labels;
    public Object customfield_10103;
    public Object timeestimate;
    public Object aggregatetimeoriginalestimate;
    public ArrayList<Object> versions;
    public ArrayList<Object> issuelinks;
    public Object assignee;
    public Date updated;
    public Status status;
    public ArrayList<Object> components;
    public Object timeoriginalestimate;
    public String description;
    public Timetracking timetracking;
    public Object archiveddate;
    public ArrayList<Object> attachment;
    public Object aggregatetimeestimate;
    public String summary;
    public Creator creator;
    public ArrayList<Object> subtasks;
    public Reporter reporter;
    public String customfield_10000;
    public Aggregateprogress aggregateprogress;
    public Object environment;
    public Object duedate;
    public Progress progress;
    public Comment comment;
    public Votes votes;
    public Worklog worklog;
    public Object archivedby;
}

public class Issuetype{
    public String self;
    public String id;
    public String description;
    public String iconUrl;
    public String name;
    public boolean subtask;
    public int avatarId;
}

public class Priority{
    public String self;
    public String iconUrl;
    public String name;
    public String id;
}

public class Progress{
    public int progress;
    public int total;
}

public class Project{
    public String self;
    public String id;
    public String key;
    public String name;
    public String projectTypeKey;
    public AvatarUrls avatarUrls;
}

public class Reporter{
    public String self;
    public String name;
    public String key;
    public String emailAddress;
    public AvatarUrls avatarUrls;
    public String displayName;
    public boolean active;
    public String timeZone;
}

public class Issue {
    public String expand;
    public String id;
    public String self;
    public String key;
    public Fields fields;

    @Override
    public String toString() {
        return "Issue{" +
                "expand='" + expand + '\'' +
                ", id='" + id + '\'' +
                ", self='" + self + '\'' +
                ", key='" + key + '\'' +
                ", fields=" + fields +
                '}';
    }
}

public class Status{
    public String self;
    public String description;
    public String iconUrl;
    public String name;
    public String id;
    public StatusCategory statusCategory;
}

public class StatusCategory{
    public String self;
    public int id;
    public String key;
    public String colorName;
    public String name;
}

public class Timetracking{
}

public class Votes{
    public String self;
    public int votes;
    public boolean hasVoted;
}

public class Watches{
    public String self;
    public int watchCount;
    public boolean isWatching;
}

public class Worklog{
    public int startAt;
    public int maxResults;
    public int total;
    public ArrayList<Object> worklogs;
}

