package com.iucyh.novelservice.episode.constant;

public abstract class EpisodeConstants {

    public static final int EPISODE_TITLE_LENGTH_MIN = 2;
    public static final int EPISODE_TITLE_LENGTH_MAX = 50;

    public static final int EPISODE_DESC_LENGTH_MAX = 35;

    public static final int COMMON_EPISODE_CONTENT_LENGTH_MIN = 450;
    public static final int COMMON_EPISODE_CONTENT_LENGTH_MAX = 20000;

    public static final int PROLOGUE_EPISODE_CONTENT_LENGTH_MIN = 100;
    public static final int PROLOGUE_EPISODE_CONTENT_LENGTH_MAX = 20000;

    public static final String EPISODE_CONTENT_SAFE_LIST_KEY = "episode.content";
}
