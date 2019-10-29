package com.bk.olympia.base;

import java.util.ArrayList;

public class ReverseArrayList<T> extends ArrayList<T> {
    @Override
    public boolean add(T o) {
        super.add(0, o);
        return true;
    }
}
