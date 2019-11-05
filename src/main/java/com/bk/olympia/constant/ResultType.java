package com.bk.olympia.constant;

public enum ResultType {
    WIN(1),
    LOSE(-1),
    DRAW(0);

    private int modifier;

    ResultType(int modifier) {
        this.modifier = modifier;
    }

    public int getModifier() {
        return modifier;
    }
}
