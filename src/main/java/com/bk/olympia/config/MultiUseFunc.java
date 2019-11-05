package com.bk.olympia.config;

import com.bk.olympia.UI.JFrameUI;
import com.bk.olympia.exception.ScreenNotFoundException;

import static com.bk.olympia.config.Constant.LOGIN_SCREEN;

public class MultiUseFunc {
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.length() < 1;
    }
}
