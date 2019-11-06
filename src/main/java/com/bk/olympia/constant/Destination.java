package com.bk.olympia.constant;

public class Destination {
    public static final String LOGIN = "/queue/auth/login";
    public static final String SIGN_UP = "/queue/auth/sign_up";
    public static final String LOGOUT = "/queue/user/logout";

    public static final String GET_USER_INFO = "/queue/user/get-info";
    public static final String CHANGE_USER_INFO = "/queue/user/change-info";
    public static final String GET_RECENT_HISTORY = "/queue/user/get-recent-history";
    public static final String ADD_QUESTION = "/queue/user/add-questions";
    public static final String GET_ALL_TOPICS = "/queue/user/get-all-topics";

    public static final String FIND_LOBBY = "/queue/play/join";
    public static final String INVITE_PLAYER = "/queue/play/invite";
    public static final String LEAVE_LOBBY = "/queue/play/leave";
    public static final String START_GAME = "/queue/play/start-game";
    public static final String CREATE_ROOM = "/queue/play/create-room";

    public static final String READY = "/queue/play/ready";
    public static final String GET_TOPIC_LIST = "/queue/play/get-topic-list";
    public static final String PICK_TOPIC = "/queue/play/pick-topic";
    public static final String GET_QUESTION = "/queue/play/get_question";
    public static final String SUBMIT_ANSWER = "/queue/play/submit_answer";
    public static final String GET_ANSWER = "/queue/play/get_answer";
    public static final String GAME_OVER = "/queue/play/game_over";

    public static final String PUBLIC_CHAT = "/topic/public";
    public static final String PRIVATE_CHAT = "/topic/private/user/";
    public static final String ROOM_CHAT = "/topic/private/room/";
    public static final String LOBBY_CHAT = "/topic/private/lobby/";

    public static final String ERROR = "/queue/error";
}
