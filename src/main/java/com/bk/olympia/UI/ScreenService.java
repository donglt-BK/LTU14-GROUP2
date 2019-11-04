package com.bk.olympia.UI;

import com.bk.olympia.UI.screen.*;

import java.util.ArrayList;
import java.util.List;

public class ScreenService {
    public static List<Screen> getScreens() {
        List<Screen> screens = new ArrayList<>();

        screens.add(new LoginScreen());
        screens.add(new HomeScreen());
        screens.add(new LobbyScreen());
        screens.add(new SignUpScreen());
        return screens;
    }
}
