package com.mygdx.demonestate.gamepad;

import com.badlogic.gdx.controllers.PovDirection;

// This code was taken from http://www.java-gaming.org/index.php?topic=29223.0
// With thanks that is!

public interface Xbox360Pad {
    public int buttonX();
    public int buttonY();
    public int buttonA();
    public int buttonB();
    public int buttonBack();
    public int buttonStart();
    public PovDirection buttonDpadUp();
    public PovDirection buttonDpadDown();
    public PovDirection buttonDpadRight();
    public PovDirection buttonDpadLeft();
    public int buttonLB();
    public int buttonRB();
    public int buttonL3();
    public int buttonR3();
    public int axisLeftX();
    public int axisLeftY();
    public int axisLeftTrigger();
    public int axisRightX();
    public int axisRightY();
    public int axisRightTrigger();
}