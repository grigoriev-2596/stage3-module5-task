package com.mjc.school.service.exception;

public enum ErrorCode {

    NEWS_DOES_NOT_EXIST(1001, "News does not exist (id=%s)"),
    AUTHOR_DOES_NOT_EXIST(1002, "Author does not exist (id=%s)"),
    COMMENT_DOES_NOT_EXIST(1003, "Comment does not exist (id=%s)"),
    TAG_DOES_NOT_EXIST(1004, "Tag does not exist (id=%s)"),

    APPLYING_NEWS_PATCH_PROBLEM(1010, "Applying patch to the news problem (id=%s)"),
    APPLYING_AUTHOR_PATCH_PROBLEM(1011, "Applying patch to the author problem (id=%s)"),
    APPLYING_COMMENT_PATCH_PROBLEM(1012, "Applying patch to the comment problem (id=%s)"),
    APPLYING_TAG_PATCH_PROBLEM(1013, "Applying patch to the tag problem (id=%s)"),

    ENTITY_VALIDATION_FAILED(1020, "One or more entity fields failed validation process"),

    UNEXPECTED_SERVER_PROBLEM(1030, "Unexpected server problem"),

    NEWS_ALREADY_EXIST(1041, "Such news already exists"),
    AUTHOR_ALREADY_EXIST(1042, "Such author already exists"),
    TAG_ALREADY_EXIST(1043, "Such tag already exists");


    private final int id;
    private final String message;


    ErrorCode(int id, String message) {
        this.id = id;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }

}
