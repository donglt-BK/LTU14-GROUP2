package com.bk.olympia.base;

import java.util.ArrayList;
import java.util.List;

public interface IReadiable {
    List<Boolean> readyList = new ArrayList<>();
    void addPlayerReady(int position);
    boolean isAllReady();
    void resetReady();
    void removePlayerReady(int position);
}
