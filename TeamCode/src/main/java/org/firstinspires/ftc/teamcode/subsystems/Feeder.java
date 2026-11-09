package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;

/** Continuous servo that feeds game pieces into the shooter ("uptoyou"). */
public class Feeder {

    private CRServo servo;

    public void init(HardwareMap hw) {
        servo = hw.get(CRServo.class, "uptoyou");
    }

    public void run(boolean active) {
        servo.setPower(active ? 1 : 0);
    }

    public void reverse() {
        servo.setPower(-1);
    }
}
