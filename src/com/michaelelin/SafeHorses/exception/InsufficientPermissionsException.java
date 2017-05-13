package com.michaelelin.SafeHorses.exception;

public class InsufficientPermissionsException extends SafeHorsesException {

    public InsufficientPermissionsException() {
        super("You don't have permission to run that command.", false);
    }

}
