package com.anma.jira.models.update

import com.fasterxml.jackson.annotation.JsonIgnoreProperties


@JsonIgnoreProperties(ignoreUnknown = true)
public class Update{
    public ArrayList<Summary> summary;
    public ArrayList<Component> components;
    public ArrayList<Timetracking> timetracking;
    public ArrayList<Label> labels;
}

public class Actor{
    public String id;
    public String displayName;
    public String type;
    public String avatarUrl;
    public String url;
}

public class Cause{
    public String id;
    public String type;
}

public class Component{
    public String set;
}

public class Edit{
    public String originalEstimate;
    public String remainingEstimate;
}

public class ExtraData{
    public String keyvalue;
    public String goes;
}

public class Fields{
    public String summary;
    public int customfield_10010;
    public String customfield_10000;
}

public class Generator{
    public String id;
    public String type;
}

public class HistoryMetadata{
    public String type;
    public String description;
    public String descriptionKey;
    public String activityDescription;
    public String activityDescriptionKey;
    public Actor actor;
    public Generator generator;
    public Cause cause;
    public ExtraData extraData;
}

public class Label{
    public String add;
    public String remove;
}

public class Property{
    public String key;
}

public class Root{
    public Update update;
    public Fields fields;
    public HistoryMetadata historyMetadata;
    public ArrayList<Property> properties;
}

public class Summary{
    public String set;
}

public class Timetracking{
    public Edit edit;
}


