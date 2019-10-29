package com.bk.olympia.model.type;

public class Destination {
    public static final String LOGIN = "/queue/login";
    public static final String SIGN_UP = "/queue/sign_up";
    public static final String LOGOUT = "/queue/user/logout";

    public static final String GET_USER_INFO = "/queue/user/get-info";
    public static final String CHANGE_USER_INFO = "/queue/user/change-info";
    public static final String GET_RECENT_HISTORY = "/queue/user/get-recent-history";
    public static final String ADD_QUESTIONS = "/queue/user/add-questions";

    public static final String FIND_LOBBY = "/queue/play/join";
    public static final String INVITE_PLAYER = "/queue/play/invite";
    public static final String LEAVE_LOBBY = "/queue/play/leave";
    public static final String START_GAME = "/queue/play/start-game";
    public static final String CREATE_ROOM = "/queue/play/create-room";

    public static final String LOAD_COMPLETE = "/queue/play/load-complete";
    public static final String GET_TOPIC_LIST = "/queue/play/get-topic-list";

    public static final String ERROR = "/queue/error";
}
