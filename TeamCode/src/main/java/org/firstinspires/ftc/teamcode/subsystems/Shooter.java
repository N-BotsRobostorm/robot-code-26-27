package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Shooter {

    private static final double DEFAULT_POWER = 0.565;
    private static final double MIN_POWER = 0.0;
    private static final double MAX_POWER = 1.0;

    private DcMotor motor;
    private double power = DEFAULT_POWER;

    public void init(HardwareMap hw) {
        motor = hw.get(DcMotor.class, "shooter");
    }

    public void run(boolean active) {
        motor.setPower(active ? power : 0);
    }

    /** Nudge the shooter power up (positive) or down (negative), clamped to [0, 1]. */
    public void adjustPower(double delta) {
        power = Math.max(MIN_POWER, Math.min(MAX_POWER, power + delta));
    }

    public double getPowerPercent() {
        return power * 100;
    }
}
