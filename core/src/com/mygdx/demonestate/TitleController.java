package com.mygdx.demonestate;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.PovDirection;
import com.mygdx.demonestate.entity.EntityHandler;
import com.mygdx.demonestate.entity.MonsterType;
import com.mygdx.demonestate.entity.Player;
import com.mygdx.demonestate.gamepad.EvanTestPad;
import com.mygdx.demonestate.gamepad.Xbox360Pad;
import com.mygdx.demonestate.menu.MenuHandler;

public class TitleController extends ControllerAdapter {
    private static final int NUM_PLAYER_CHOICES = 4;
    private Controller controller;
    private Xbox360Pad gamepad;
    private TitleScreen titleScreen;
    private int id;
    private int currentChoice;

    public TitleController(TitleScreen titleScreen, Controller controller, int id) {
        this.titleScreen = titleScreen;
        this.controller = controller;
        this.id = id;
        this.gamepad = new EvanTestPad();
        this.currentChoice = 0;

        controller.addListener(this);
    }

    public boolean buttonDown (Controller controller, int buttonCode) {
        if (buttonCode == gamepad.buttonA()) {
            if (titleScreen.getState() == TitleScreen.INITIAL_STATE)
                titleScreen.toggleTable();
            else if (titleScreen.getState() == TitleScreen.CHARACTER_SELECT) {
                titleScreen.toggleCharSelect(id);
            }
        }
        if (buttonCode == gamepad.buttonStart()) {
            if (titleScreen.getState() == TitleScreen.CHARACTER_SELECT)
                titleScreen.startGame();
        }
        if (buttonCode == gamepad.buttonB()) {
            if (titleScreen.getState() == TitleScreen.CHARACTER_SELECT) {
                titleScreen.toggleTable();
            }
            else if (titleScreen.getState() == TitleScreen.INITIAL_STATE) {
                System.exit(0);
            }
        }
        return true;
    }

    public boolean povMoved (Controller controller, int povCode, PovDirection value) {
        if (value == gamepad.buttonDpadDown()) {
            titleScreen.changeCharSelection(id, -1);
        }
        if (value == gamepad.buttonDpadUp()) {
            currentChoice = (currentChoice + 1) % NUM_PLAYER_CHOICES ;
            titleScreen.changeCharSelection(id, 1);
        }
        return true;
    }


    public Controller getController() {
        return controller;
    }
}
