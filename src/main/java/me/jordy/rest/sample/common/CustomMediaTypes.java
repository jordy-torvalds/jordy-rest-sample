package me.jordy.rest.sample.common;

public enum CustomMediaTypes {
    HAL_JSON_UTF8("application/hal+json;charset=UTF-8"),
    JSON_UTF8("application/json;charset=UTF-8");

    String mediaTypeStr;

    CustomMediaTypes(String mediaTypeStr) {
        this.mediaTypeStr = mediaTypeStr;
    }

    public String getMediaTypeStr() {
        return this.mediaTypeStr;
    }
}
