package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name = "Field Relative Driving", group = "TeleOp")
public class fieldRelativeDrving extends LinearOpMode {

    // Drive motors
    private DcMotor leftFront;
    private DcMotor rightFront;
    private DcMotor leftRear;
    private DcMotor rightRear;
    private DcMotor intake;
    private DcMotor shooter;
    private CRServo uptoyou;

    //toggles and speed
    boolean shooterActive = false;
    boolean intakeActive = false;
        
    //buttonstates
    boolean lastIntakeBind = false;
    boolean lastreverseBind = false;

    //debouncetimes

    int buttonDebounceTime = 200;
    private ElapsedTime intakeBindDebounce = new ElapsedTime();
    private ElapsedTime reverseBindDebounce = new ElapsedTime();
    
    
    // REV Hub IMU
    private IMU imu;

    @Override
    public void runOpMode() {

        // Get motors from hardware map
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        leftRear = hardwareMap.get(DcMotor.class, "leftRear");
        rightRear = hardwareMap.get(DcMotor.class, "rightRear");
        intake = hardwareMap.get(DcMotor.class, "intake");
        shooter = hardwareMap.get(DcMotor.class, "shooter");
        uptoyou = hardwareMap.get(CRServo.class, "uptoyou");

        // Get IMU
        imu = hardwareMap.get(IMU.class, "imu");

        /*
         * IMPORTANT:
         *
         * Change these two values if your REV Hub is mounted differently.
         *
         * LogoFacingDirection:
         *   UP, DOWN, FORWARD, BACKWARD
         *
         * UsbFacingDirection:
         *   FORWARD, BACKWARD, UP, DOWN, LEFT, RIGHT
         */
        RevHubOrientationOnRobot.LogoFacingDirection logoDirection =
                RevHubOrientationOnRobot.LogoFacingDirection.RIGHT;

        RevHubOrientationOnRobot.UsbFacingDirection usbDirection =
                RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD;

        RevHubOrientationOnRobot orientationOnRobot =
                new RevHubOrientationOnRobot(logoDirection, usbDirection);

        imu.initialize(new IMU.Parameters(orientationOnRobot));

        // Motor directions
        leftFront.setDirection(DcMotor.Direction.FORWARD);
        leftRear.setDirection(DcMotor.Direction.FORWARD);

        rightFront.setDirection(DcMotor.Direction.REVERSE);
        rightRear.setDirection(DcMotor.Direction.REVERSE);

        // Brake when joystick is released
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addLine("Initialized");
        telemetry.addLine("Press PLAY to start");
        telemetry.update();

        waitForStart();

        // Reset the robot's current heading to 0 degrees
        imu.resetYaw();

        while (opModeIsActive()) {
                
            //keybinds
            double uptoyouSpeed = gamepad2.left_trigger;
            double shooterSpeed = gamepad2.right_trigger;
            boolean intakeBind = gamepad2.y;
            boolean reverseIntakeBind = gamepad2.b;

            if (gamepad1.a) {
                imu.resetYaw();
            }

            // -------------------------------------------------
            // Joystick inputs
            // -------------------------------------------------

            // Left stick:
            //   X = strafe
            //   Y = forward/backward
            //
            // Negate Y because joystick forward is negative.
            double y = -gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x;

            // Right stick X = rotation
            double rx = gamepad1.right_stick_x;

            // -------------------------------------------------
            // Get robot heading from IMU
            // -------------------------------------------------

            double botHeading = imu.getRobotYawPitchRollAngles()
                    .getYaw(AngleUnit.RADIANS);

            // -------------------------------------------------
            // Convert field-relative movement to robot-relative
            // -------------------------------------------------

            double rotX = x * Math.cos(-botHeading)
                    - y * Math.sin(-botHeading);

            double rotY = x * Math.sin(-botHeading)
                    + y * Math.cos(-botHeading);

            /*
             * Counteract imperfect strafing caused by mecanum
             * wheel geometry.
             */
            rotX = rotX * 1.1;

            // -------------------------------------------------
            // Mecanum wheel calculations
            // -------------------------------------------------

            double denominator = Math.max(
                    Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx),
                    1.0
            );

            double frontLeftPower =
                    (rotY + rotX + rx) / denominator;

            double backLeftPower =
                    (rotY - rotX + rx) / denominator;

            double frontRightPower =
                    (rotY - rotX - rx) / denominator;

            double backRightPower =
                    (rotY + rotX - rx) / denominator;

            // -------------------------------------------------
            // Send power to motors
            // -------------------------------------------------

            if (gamepad1.right_bumper) {
                frontLeftPower /= 2;
                backRightPower /= 2;
                backLeftPower /= 2;
                frontRightPower /= 2;

            }

            if (gamepad1.left_bumper) {
                frontLeftPower /= 4;
                frontRightPower /= 4;
                backLeftPower /= 4;
                backRightPower /= 4;

            }

            leftFront.setPower(frontLeftPower);
            leftRear.setPower(backLeftPower);
            rightFront.setPower(frontRightPower);
            rightRear.setPower(backRightPower);

            // -------------------------------------------------
            // Telemetry
            // -------------------------------------------------

            telemetry.addData(
                    "Heading",
                    "%.1f°",
                    Math.toDegrees(botHeading)
            );

            telemetry.addData("FL", "%.2f", frontLeftPower);
            telemetry.addData("FR", "%.2f", frontRightPower);
            telemetry.addData("BL", "%.2f", backLeftPower);
            telemetry.addData("BR", "%.2f", backRightPower);

            telemetry.update();
            if (intakeBind && !lastIntakeBind && intakeBindDebounce.milliseconds() > buttonDebounceTime){
                    intakeActive = !intakeActive;
                    intakeBindDebounce.reset();
            }

            if (intakeActive){
                intake.setPower(1);
            } else {
                intake.setPower(0);
            }

            if (reverseIntakeBind){
                intake.setPower(-1);
            }

            uptoyou.setPower(uptoyouSpeed);
            shooter.setPower(shooterSpeed);
        }
    }
}
