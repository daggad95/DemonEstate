package com.mygdx.demonestate.entity;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.demonestate.gamepad.LinuxXbox360Pad;
import com.mygdx.demonestate.gamepad.Xbox360Pad;
import com.mygdx.demonestate.menu.MenuHandler;

/**
 * Created by David on 1/9/2017.
 */
public class PlayerController extends ControllerAdapter {
    //threshold joystick must pass to trigger event
    private static final float JOYSTICK_THRESHOLD = 0.25f;

    private Player player;
    private Controller controller;
    private Xbox360Pad gamepad;

    public PlayerController(Player player, Controller controller) {
        this.player = player;
        this.controller = controller;
        this.gamepad = new LinuxXbox360Pad();

        controller.addListener(this);
    }

    public boolean axisMoved(Controller controller, int axisCode, float value) {
        float xLeft = controller.getAxis(gamepad.axisLeftX());
        float yLeft = -controller.getAxis(gamepad.axisLeftY());
        float xRight = controller.getAxis(gamepad.axisRightX());
        float yRight = -controller.getAxis(gamepad.axisRightY());

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

        if (controller.getAxis(gamepad.axisRightTrigger()) > 0.5f) {
            player.setAttacking(true);
        } else {
            player.setAttacking(false);
        }

        return true;
    }

    public boolean buttonDown (Controller controller, int buttonCode) {
        if (buttonCode == gamepad.buttonStart()) {
            MenuHandler.getMenu(player.getId()).toggleActive(player);
        }
        if (buttonCode == gamepad.buttonA()) {
            if (MenuHandler.getMenu(player.getId()).isActive()) {
                MenuHandler.getMenu(player.getId()).selectItem(player);
            }
            else {
                EntityHandler.addMonster(new Vector2(player.getPos()).add(3, 3));
            }
        }
        if (buttonCode == gamepad.buttonB()) {
            if (MenuHandler.getMenu(player.getId()).isActive()) {
                MenuHandler.getMenu(player.getId()).goBack(player);
            }
        }
        if (buttonCode == gamepad.buttonBack()) {
            System.exit(0);
        }

        return true;
    }

    public boolean povMoved (Controller controller, int povCode, PovDirection value) {
        if (value == gamepad.buttonDpadDown()) {
            if (MenuHandler.getMenu(player.getId()).isActive()) {
                MenuHandler.getMenu(player.getId()).changeSelection(1);
            }
            else {
                player.switchWeapon(player.SIDEARM);
            }
        }
        if (value == gamepad.buttonDpadUp()) {
            if (MenuHandler.getMenu(player.getId()).isActive()) {
                MenuHandler.getMenu(player.getId()).changeSelection(-1);
            }
            else {
                player.switchWeapon(player.MAIN_GUN);
            }
        }

        return true;
    }
}
