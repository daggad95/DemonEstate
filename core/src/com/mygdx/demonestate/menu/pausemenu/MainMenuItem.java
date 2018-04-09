package com.mygdx.demonestate.menu.pausemenu;

public class MainMenuItem implements PauseMenuItem {
    public static final String text = "Main Menu";

    public String toString() { return text; }

    public void action() {
        System.exit(0);
    }
}
