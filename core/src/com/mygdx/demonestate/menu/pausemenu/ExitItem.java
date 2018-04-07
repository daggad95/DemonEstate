package com.mygdx.demonestate.menu.pausemenu;

public class ExitItem implements PauseMenuItem {
    public static final String text = "Exit Game";

    public String toString() { return text; }

    public void action() {
        System.exit(0);
    }
}
