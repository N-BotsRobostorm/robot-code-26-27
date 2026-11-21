package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class FlowerBar {
    private Servo servo;

    public void init(HardwareMap hw) { servo = hw.get(Servo.class, "flowerBar");}

    public void run(boolean active){servo.setPosition(active ? 1 : 0.05);}
}
