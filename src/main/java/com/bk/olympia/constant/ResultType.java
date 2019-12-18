package com.bk.olympia.constant;

public enum ResultType {
    WIN(1, "WIN"),
    LOSE(-1, "LOSE"),
    DRAW(0, "DRAW");

    private int modifier;
    private String resultString;

    ResultType(int modifier, String resultString) {
        this.modifier = modifier;
        this.resultString = resultString;
    }

    public int getModifier() {
        return modifier;
    }

    public String getString() {
        return resultString;
    }
}
