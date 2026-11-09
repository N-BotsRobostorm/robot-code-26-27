package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {

    private DcMotor motor;

    public void init(HardwareMap hw) {
        motor = hw.get(DcMotor.class, "intake");
    }

    public void run(boolean active) {
        motor.setPower(active ? 1 : 0);
    }

    public void reverse() {
        motor.setPower(-1);
    }
}
