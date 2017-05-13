package com.michaelelin.SafeHorses.exception;

public class SafeHorsesException extends Exception {

    private String message;
    private boolean showUsage;

    public SafeHorsesException(String message, boolean showUsage) {
        this.message = message;
        this.showUsage = showUsage;
    }

    public String getMessage() {
        return message;
    }

    public boolean hasMessage() {
        return message != null;
    }

    public boolean isShowUsage() {
        return showUsage;
    }

}
