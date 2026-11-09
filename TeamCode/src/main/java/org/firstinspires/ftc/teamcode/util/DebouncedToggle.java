package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Turns a held button into a clean on/off toggle.
 * Flips on the rising edge (press), ignoring presses within the debounce window.
 */
public class DebouncedToggle {

    private final long debounceMs;
    private final ElapsedTime timer = new ElapsedTime();

    private boolean lastPressed = false;
    private boolean state = false;

    public DebouncedToggle(long debounceMs) {
        this.debounceMs = debounceMs;
    }

    /** Call once per loop with the current button value. Returns the toggle state. */
    public boolean update(boolean pressed) {
        if (pressed && !lastPressed && timer.milliseconds() > debounceMs) {
            state = !state;
            timer.reset();
        }
        lastPressed = pressed;
        return state;
    }

    public boolean get() {
        return state;
    }
}
