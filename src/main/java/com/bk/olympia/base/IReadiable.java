package com.bk.olympia.base;

public interface IReadiable {
    void addPlayerReady(int position);

    boolean isAllReady();

    void resetReady();
}
