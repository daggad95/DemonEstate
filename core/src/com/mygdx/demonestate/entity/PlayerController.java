package com.mygdx.demonestate.entity;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.PovDirection;
import com.mygdx.demonestate.LinuxXbox360Pad;
import com.mygdx.demonestate.menu.Menu;
import com.mygdx.demonestate.menu.MenuHandler;

/**
 * Created by David on 1/9/2017.
 */
public class PlayerController extends ControllerAdapter {
    //threshold joystick must pass to trigger event
    private static final float JOYSTICK_THRESHOLD = 0.25f;

    private Player player;
    private Controller controller;

    public PlayerController(Player player, Controller controller) {
        this.player = player;
        this.controller = controller;

        controller.addListener(this);
    }

    public boolean axisMoved(Controller controller, int axisCode, float value) {
        float xLeft = controller.getAxis(LinuxXbox360Pad.AXIS_LEFT_X);
        float yLeft = -controller.getAxis(LinuxXbox360Pad.AXIS_LEFT_Y);
        float xRight = controller.getAxis(LinuxXbox360Pad.AXIS_RIGHT_X);
        float yRight = -controller.getAxis(LinuxXbox360Pad.AXIS_RIGHT_Y);

        if (Math.abs(xLeft) > JOYSTICK_THRESHOLD && Math.abs(yLeft) > JOYSTICK_THRESHOLD) {
            player.setMovementVector(xLeft, yLeft);
        } else if (Math.abs(xLeft) > JOYSTICK_THRESHOLD) {
            player.setMovementVector(xLeft, 0);
        } else if (Math.abs(yLeft) > JOYSTICK_THRESHOLD) {
            player.setMovementVector(0, yLeft);
        } else {
            player.setMovementVector(0, 0);
        }

       if (Math.abs(xRight) > JOYSTICK_THRESHOLD || Math.abs(yRight) > JOYSTICK_THRESHOLD) {
            player.setAttackDir(xRight, yRight);
        }

        if (controller.getAxis(LinuxXbox360Pad.AXIS_RIGHT_TRIGGER) > 0.5f) {
            player.setAttacking(true);
        } else {
            player.setAttacking(false);
        }

        return true;
    }

    public boolean buttonDown (Controller controller, int buttonCode) {
        Menu menu = MenuHandler.getMenu();

        if (buttonCode == LinuxXbox360Pad.BUTTON_START) {
            menu.toggleActive();
        }
        if (buttonCode == LinuxXbox360Pad.BUTTON_A) {
            if (menu.isActive()) {
                menu.selectItem(player);
            }
        }

        return true;
    }

    public boolean povMoved (Controller controller, int povCode, PovDirection value) {
        if (value == LinuxXbox360Pad.BUTTON_DPAD_DOWN) {
            MenuHandler.getMenu().changeSelection(1);
        }

        if (value == LinuxXbox360Pad.BUTTON_DPAD_UP) {
            MenuHandler.getMenu().changeSelection(-1);
        }

        return true;
    }
}
