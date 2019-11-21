package com.bk.olympia.exception;

import com.bk.olympia.base.BaseRuntimeException;
import com.bk.olympia.constant.ErrorType;

public class AnswerPlacingViolationException extends BaseRuntimeException {

    public AnswerPlacingViolationException(ViolationType type, int userId) {
        super("Violated the rule of placing money into answer. " +
                (type == ViolationType.MONEY_EXCEED_MONEY_TOTAL ? "Money exceeds player's current money total. " : "Player placed all four answers. "), userId, ErrorType.ANSWER_PLACING_VIOLATION);
    }

    public enum ViolationType {
        MONEY_EXCEED_MONEY_TOTAL,
        PLACE_ALL_FOUR_ANSWER
    }
}
