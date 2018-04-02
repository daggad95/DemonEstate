package com.mygdx.demonestate.gamepad;

import com.badlogic.gdx.controllers.PovDirection;

/**
 * Created by david on 3/21/17.
 */
public class LinuxXbox360Pad implements Xbox360Pad {
    public static final int BUTTON_X = 2;
    public static final int BUTTON_Y = 3;
    public static final int BUTTON_A = 0;
    public static final int BUTTON_B = 1;
    public static final int BUTTON_BACK = 6;
    public static final int BUTTON_START = 7;
    public static final PovDirection BUTTON_DPAD_UP = PovDirection.north;
    public static final PovDirection BUTTON_DPAD_DOWN = PovDirection.south;
    public static final PovDirection BUTTON_DPAD_RIGHT = PovDirection.east;
    public static final PovDirection BUTTON_DPAD_LEFT = PovDirection.west;
    public static final int BUTTON_LB = 4;
    public static final int BUTTON_L3 = 8;
    public static final int BUTTON_RB = 5;
    public static final int BUTTON_R3 = 9;
    public static final int AXIS_LEFT_X = 0; //-1 is left | +1 is right
    public static final int AXIS_LEFT_Y = 1; //-1 is up | +1 is down
    public static final int AXIS_LEFT_TRIGGER = 2; //value 0 to 1f
    public static final int AXIS_RIGHT_X = 3; //-1 is left | +1 is right
    public static final int AXIS_RIGHT_Y = 4; //-1 is up | +1 is down
    public static final int AXIS_RIGHT_TRIGGER = 5; //value 0 to -1f

    public int buttonX() { return BUTTON_X; }
    public int buttonY() { return BUTTON_Y; }
    public int buttonA() { return BUTTON_A; }
    public int buttonB() { return BUTTON_B; }
    public int buttonBack() { return BUTTON_BACK; }
    public int buttonStart() { return BUTTON_START; }
    public PovDirection buttonDpadUp() { return BUTTON_DPAD_UP; }
    public PovDirection buttonDpadDown() { return BUTTON_DPAD_DOWN; };
    public PovDirection buttonDpadRight() { return BUTTON_DPAD_RIGHT; };
    public PovDirection buttonDpadLeft() { return BUTTON_DPAD_RIGHT; };
    public int buttonLB() { return BUTTON_LB; };
    public int buttonRB() { return BUTTON_RB; }
    public int buttonL3() { return BUTTON_L3; }
    public int buttonR3() { return BUTTON_R3; }
    public int axisLeftX() { return AXIS_LEFT_X; }
    public int axisLeftY() { return AXIS_LEFT_Y; };
    public int axisLeftTrigger() { return AXIS_LEFT_TRIGGER; }
    public int axisRightX() { return AXIS_RIGHT_X; }
    public int axisRightY() { return AXIS_RIGHT_Y; }
    public int axisRightTrigger() { return AXIS_RIGHT_TRIGGER; }
}
