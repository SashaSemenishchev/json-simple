package com.github.cliftonlabs.json_simple;

public interface Accessor {
    JsonArray asArray();

    JsonObject asJsonObject();

    boolean isArray();

    boolean isJsonObject();

    boolean isNull();
}
