package com.mygdx.demonestate.gamepad;

import com.badlogic.gdx.controllers.PovDirection;

public class EvanTestPad extends LinuxXbox360Pad {

    public int buttonX() { return BUTTON_X; }
    public int buttonY() { return BUTTON_Y; }
    public int buttonA() { return BUTTON_A; }
    public int buttonB() { return BUTTON_B; }
    public int buttonBack() { return BUTTON_BACK; }
    public int buttonStart() { return BUTTON_START; }
    public PovDirection buttonDpadUp() { return BUTTON_DPAD_UP; }
    public PovDirection buttonDpadDown() { return BUTTON_DPAD_DOWN; }
    public PovDirection buttonDpadRight() { return BUTTON_DPAD_RIGHT; }
    public PovDirection buttonDpadLeft() { return BUTTON_DPAD_RIGHT; }
    public int buttonLB() { return BUTTON_RB; }
    public int buttonRB() { return BUTTON_LB; }
    public int buttonL3() { return BUTTON_L3; }
    public int buttonR3() { return BUTTON_R3; }
    public int axisLeftX() { return AXIS_LEFT_X; }
    public int axisLeftY() { return AXIS_LEFT_Y; }
    public int axisLeftTrigger() { return AXIS_RIGHT_TRIGGER; }     // changed
    public int axisRightX() { return AXIS_LEFT_TRIGGER; }           // changed
    public int axisRightY() { return AXIS_RIGHT_X; }                // changed
    public int axisRightTrigger() { return AXIS_RIGHT_Y; }          // changed

}