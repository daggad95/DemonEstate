package com.mygdx.demonestate.menu.pausemenu;

import com.mygdx.demonestate.menu.MenuHandler;

public class MainMenuItem implements PauseMenuItem {
    public static final String text = "Main Menu";

    public String toString() { return text; }

    public void action() {
        MenuHandler.titleScreen();
    }
}
