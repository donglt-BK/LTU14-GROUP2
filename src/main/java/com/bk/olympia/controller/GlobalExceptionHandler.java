package com.bk.olympia.controller;

import com.bk.olympia.exception.*;
import com.bk.olympia.model.message.ErrorMessage;
import com.bk.olympia.model.message.Message;
import com.bk.olympia.model.type.Destination;
import com.bk.olympia.model.type.ErrorType;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @SendToUser(Destination.ERROR)
    @ExceptionHandler
    public final Message handleException(Exception e) {
        if (e instanceof WrongUsernameOrPasswordException) {
            return handleWrongUsernameOrPasswordException((WrongUsernameOrPasswordException) e);
        } else if (e instanceof InsufficientBalanceException) {
            return handleInsufficientBalanceException((InsufficientBalanceException) e);
        } else if (e instanceof InvalidActionException) {
            return handleInvalidActionException((InvalidActionException) e);
        } else if (e instanceof UserIdNotFoundException) {
            return handleUserIdNotFoundException((UserIdNotFoundException) e);
        } else if (e instanceof TargetInsufficientBalanceException) {
            return handleTargetInsufficientBalanceException((TargetInsufficientBalanceException) e);
        } else {
            return null;
        }
    }

    private Message handleWrongUsernameOrPasswordException(WrongUsernameOrPasswordException e) {
        return new ErrorMessage(ErrorType.AUTHENTICATION, e.getUserId());
    }

    private Message handleInsufficientBalanceException(InsufficientBalanceException e) {
        return new ErrorMessage(ErrorType.INSUFFICIENT_BALANCE, e.getUserId());
    }

    private Message handleInvalidActionException(InvalidActionException e) {
        return new ErrorMessage(ErrorType.INVALID_ACTION, e.getUserId());
    }

    private Message handleUserIdNotFoundException(UserIdNotFoundException e) {
        return new ErrorMessage(ErrorType.WRONG_ID, e.getUserId());
    }

    private Message handleTargetInsufficientBalanceException(TargetInsufficientBalanceException e) {
        return new ErrorMessage(ErrorType.TARGET_INSUFFICIENT_BALANCE, e.getUserId());
    }
}
