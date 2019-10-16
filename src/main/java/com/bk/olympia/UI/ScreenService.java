package com.bk.olympia.UI;

import com.bk.olympia.UI.screen.HomeScreen;
import com.bk.olympia.UI.screen.LoginScreen;
import com.bk.olympia.UI.screen.Screen;

import java.util.ArrayList;
import java.util.List;

public class ScreenService {
    public static List<Screen> generateScreens(int windowWidth, int windowHeight) {
        List<Screen> screens = new ArrayList<>();

        screens.add(new LoginScreen(windowWidth, windowHeight));
        screens.add(new HomeScreen(windowWidth, windowHeight));
        return screens;
    }
}
