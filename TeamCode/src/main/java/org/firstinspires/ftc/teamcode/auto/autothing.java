package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "autothing (Blocks to Java)", preselectTeleOp = "mainEntryPoint")
public class autothing extends LinearOpMode {

  private DcMotor leftFront;
  private DcMotor leftRear;
  private DcMotor rightFront;
  private DcMotor rightRear;

  /**
   * This sample contains the bare minimum Blocks for any regular OpMode. The 3 blue
   * Comment Blocks show where to place Initialization code (runs once, after touching the
   * DS INIT button, and before touching the DS Start arrow), Run code (runs once, after
   * touching Start), and Loop code (runs repeatedly while the OpMode is active, namely not
   * Stopped).
   */
  @Override
  public void runOpMode() {
    int stop2;
    int speed;
    int rightspeed;

    leftFront = hardwareMap.get(DcMotor.class, "leftFront");
    leftRear = hardwareMap.get(DcMotor.class, "leftRear");
    rightFront = hardwareMap.get(DcMotor.class, "rightFront");
    rightRear = hardwareMap.get(DcMotor.class, "rightRear");

    stop2 = 0;
    speed = 1;
    rightspeed = -1;
    // Put initialization blocks here.
    waitForStart();
    leftFront.setPower(speed);
    leftRear.setPower(speed);
    rightFront.setPower(rightspeed);
    rightRear.setPower(rightspeed);
    sleep(100);
    leftFront.setPower(stop2);
    leftRear.setPower(stop2);
    rightFront.setPower(stop2);
    rightRear.setPower(stop2);
  }
}
