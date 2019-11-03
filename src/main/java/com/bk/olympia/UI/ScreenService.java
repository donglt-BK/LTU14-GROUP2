package com.bk.olympia.UI;

import com.bk.olympia.UI.screen.HomeScreen;
import com.bk.olympia.UI.screen.LobbyScreen;
import com.bk.olympia.UI.screen.LoginScreen;
import com.bk.olympia.UI.screen.Screen;

import java.util.ArrayList;
import java.util.List;

public class ScreenService {
    public static List<Screen> getScreens() {
        List<Screen> screens = new ArrayList<>();

        screens.add(new LoginScreen());
        screens.add(new HomeScreen());
        screens.add(new LobbyScreen());
        return screens;
    }
}
