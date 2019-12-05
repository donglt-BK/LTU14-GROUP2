package com.bk.olympia.base;

public interface IReadiable {
    void addPlayerReady(int position);
    boolean isAllReady();
    void resetReady();
    void removePlayerReady(int position);
}
