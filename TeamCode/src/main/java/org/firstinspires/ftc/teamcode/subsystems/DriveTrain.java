package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/** Field-relative mecanum drive using the REV Hub IMU. */
public class DriveTrain {

    // Counteracts imperfect strafing from mecanum wheel geometry
    private static final double STRAFE_CORRECTION = 1.1;

    private DcMotor leftFront, rightFront, leftRear, rightRear;
    private IMU imu;

    private double flPower, frPower, blPower, brPower;
    private double heading; // radians

    public void init(HardwareMap hw) {
        leftFront  = hw.get(DcMotor.class, "leftFront");
        rightFront = hw.get(DcMotor.class, "rightFront");
        leftRear   = hw.get(DcMotor.class, "leftRear");
        rightRear  = hw.get(DcMotor.class, "rightRear");

        leftFront.setDirection(DcMotor.Direction.FORWARD);
        leftRear.setDirection(DcMotor.Direction.FORWARD);
        rightFront.setDirection(DcMotor.Direction.REVERSE);
        rightRear.setDirection(DcMotor.Direction.REVERSE);

        for (DcMotor m : new DcMotor[]{leftFront, rightFront, leftRear, rightRear}) {
            m.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }

        initImu(hw);
    }

    /**
     * Change these two values if your REV Hub is mounted differently.
     */
    private void initImu(HardwareMap hw) {
        imu = hw.get(IMU.class, "imu");

        RevHubOrientationOnRobot.LogoFacingDirection logo =
                RevHubOrientationOnRobot.LogoFacingDirection.RIGHT;
        RevHubOrientationOnRobot.UsbFacingDirection usb =
                RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD;

        imu.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(logo, usb)));
    }

    public void resetHeading() {
        imu.resetYaw();
    }

    /**
     * @param x      strafe input (right is positive)
     * @param y      forward input (forward is positive)
     * @param rx     rotation input
     * @param scale  speed multiplier (1.0 = full, 0.5 = half, ...)
     */
    public void driveFieldRelative(double x, double y, double rx, double scale) {
        heading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        // Field-relative -> robot-relative
        double rotX = x * Math.cos(-heading) - y * Math.sin(-heading);
        double rotY = x * Math.sin(-heading) + y * Math.cos(-heading);
        rotX *= STRAFE_CORRECTION;

        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1.0);

        flPower = (rotY + rotX + rx) / denominator * scale;
        blPower = (rotY - rotX + rx) / denominator * scale;
        frPower = (rotY - rotX - rx) / denominator * scale;
        brPower = (rotY + rotX - rx) / denominator * scale;

        leftFront.setPower(flPower);
        leftRear.setPower(blPower);
        rightFront.setPower(frPower);
        rightRear.setPower(brPower);
    }

    public void addTelemetry(Telemetry t) {
        t.addData("Heading", "%.1f°", Math.toDegrees(heading));
        t.addData("FL", "%.2f", flPower);
        t.addData("FR", "%.2f", frPower);
        t.addData("BL", "%.2f", blPower);
        t.addData("BR", "%.2f", brPower);
    }
}
