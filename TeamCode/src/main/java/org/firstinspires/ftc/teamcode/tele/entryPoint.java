package org.firstinspires.ftc.teamcode.tele;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystems.DriveTrain;
import org.firstinspires.ftc.teamcode.subsystems.Feeder;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.util.DebouncedToggle;

@TeleOp(name = "mainEntryPoint", group = "TeleOp")
public class entryPoint extends LinearOpMode {

    //Tuning constants
    private static final long   BUTTON_DEBOUNCE_MS = 200;
    private static final double TRIGGER_THRESHOLD = 0.3;
    private static final double SHOOTER_POWER_STEP = 0.005;
    private static final double SLOW_SCALE = 0.5;
    private static final double PRECISION_SCALE = 0.25;

    //Subsystems
    private final DriveTrain drive = new DriveTrain();
    private final Intake intake = new Intake();
    private final Shooter shooter = new Shooter();
    private final Feeder feeder = new Feeder();

    // Toggles
    private final DebouncedToggle intakeToggle = new DebouncedToggle(BUTTON_DEBOUNCE_MS);
    private final DebouncedToggle shooterToggle = new DebouncedToggle(BUTTON_DEBOUNCE_MS);
    private final DebouncedToggle feederToggle = new DebouncedToggle(BUTTON_DEBOUNCE_MS);

    private final ElapsedTime shooterTuneTimer = new ElapsedTime();

    @Override
    public void runOpMode() {
        initHardware();

        telemetry.addLine("Initialized");
        telemetry.addLine("Press PLAY to start");
        telemetry.update();

        waitForStart();
        drive.resetHeading();

        while (opModeIsActive()) {
            handleDrive();
            handleShooterTuning();
            handleMechanisms();
            updateTelemetry();
        }
    }

    // Setup

    private void initHardware() {
        drive.init(hardwareMap);
        intake.init(hardwareMap);
        shooter.init(hardwareMap);
        feeder.init(hardwareMap);
    }

    // Gamepad 1: driving

    private void handleDrive() {
        if (gamepad1.a) {
            drive.resetHeading();
        }

        // Joystick forward is negative, so negate Y
        double y  = -gamepad1.left_stick_y;
        double x  = gamepad1.left_stick_x;
        double rx = gamepad1.right_stick_x;

        drive.driveFieldRelative(x, y, rx, getSpeedScale());
    }

    private double getSpeedScale() {
        if (gamepad1.left_bumper)  return PRECISION_SCALE;
        if (gamepad1.right_bumper) return SLOW_SCALE;
        return 1.0;
    }

    // Gamepad 2: shooter power tuning (dpad)

    private void handleShooterTuning() {
        boolean up   = gamepad2.dpad_up;
        boolean down = gamepad2.dpad_down;

        if ((up || down) && shooterTuneTimer.milliseconds() > BUTTON_DEBOUNCE_MS) {
            if (up)   shooter.adjustPower(SHOOTER_POWER_STEP);
            if (down) shooter.adjustPower(-SHOOTER_POWER_STEP);
            shooterTuneTimer.reset();
        }
    }

    // Gamepad 2: intake / shooter / feeder

    private void handleMechanisms() {
        boolean intakeOn  = intakeToggle.update(gamepad2.y);
        boolean shooterOn = shooterToggle.update(gamepad2.right_trigger >= TRIGGER_THRESHOLD);
        boolean feederOn  = feederToggle.update(gamepad2.left_trigger >= TRIGGER_THRESHOLD);

        shooter.run(shooterOn);

        // Eject overrides intake + feeder while held
        if (gamepad2.b) {
            intake.reverse();
            feeder.reverse();
        } else {
            intake.run(intakeOn);
            feeder.run(feederOn);
        }
    }

    // Telemetry

    private void updateTelemetry() {
        drive.addTelemetry(telemetry);
        telemetry.addData("Shooter Power: ", shooter.getPowerPercent());
        telemetry.update();
    }
}
