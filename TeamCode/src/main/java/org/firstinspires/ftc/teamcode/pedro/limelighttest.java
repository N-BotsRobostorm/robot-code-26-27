package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
// Import the native FTC Limelight drivers
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.limelightvision.LLResult;

// 1. Fixed spelling from @Autonomus to @Autonomous

@Autonomous(name="limelighttest", group="Concept")
public class limelighttest extends LinearOpMode {
  private Limelight3A limelight;

  @Override
  public void runOpMode() throws InterruptedException {
    limelight = hardwareMap.get(Limelight3A.class, "limelight");
    limelight.setPollRateHz(100);
    limelight.start();

    waitForStart();

    // 2. Added parenthesis to opModeIsActive() because it is a method
    while (opModeIsActive()) {
      // 3. Fixed spelling from LLRresult to LLResult
      LLResult result = limelight.getLatestResult();

      if (result != null && result.isValid()) {
        // 4. Fixed spelling from telementry to telemetry across all lines
        telemetry.addData("Target Found", "yes");
        telemetry.addData("X Offset: (tx)", result.getTx());
      } else {
        telemetry.addData("Target Found", "no");
      }
      telemetry.update();
    }
  }
}
