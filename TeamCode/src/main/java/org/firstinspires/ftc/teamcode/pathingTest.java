package org.firstinspires.ftc.teamcode;


import static com.pedropathing.api.Paths.*;
import com.pedropathing.api.Paths;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.*;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Feeder;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;

@Autonomous(name = "AutoPath", group = "Autonomous", preselectTeleOp = "mainEntryPonit")
public class pathingTest extends LinearOpMode {

    private Follower follower;
    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(59.5199, 9.32, 90);
    private final Pose point1 = poseFactory.of(59.5438, 23.3543, -90);
    private final Pose point2 = poseFactory.of(15.2171, 90.6596, 90.7994);
    private final Pose point2Control1 = poseFactory.of(15.8011, 48.8034, 0);


    //define modules

    private final Shooter shooter = new Shooter();
    private final Intake intake = new Intake();
    private final Feeder uptoyou = new Feeder();




    // Autonomous routine
    public Command autoRoutine() {
        return sequential(
                instant(() -> shooter.run(true)),
                waitMs(5000),
                instant(() -> {
                    intake.run(true);
                    uptoyou.run(true);
                }),
                waitMs(15000),
                instant(() -> {
                    shooter.run(false);
                    intake.run(false);
                    uptoyou.run(false);
                }),
                follow(follower, path1()),
                follow(follower, path2())


        );
    }

    @Override
    public void runOpMode() {

        //init hardware
        shooter.init(hardwareMap);
        uptoyou.init(hardwareMap);
        intake.init(hardwareMap);

        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(start);
        follower.update();

        waitForStart();
        schedule(autoRoutine());

        while (opModeIsActive()) {
            follower.update();
            Scheduler.execute();

            telemetry.addData("x", follower.pose().x());
            telemetry.addData("y", follower.pose().y());
            telemetry.addData("heading", follower.pose().heading());

            if (follower.currentPath() != null) {
                telemetry.addData("Current path distance remaining", follower.distanceToEndpoint());
                telemetry.addData("Path number", follower.pathIndex());
            }

            telemetry.update();
        }
    }

    public Path path1() {
        return Paths.line(start, point1).constant(point1);
    }

    public Path path2() {
        return Paths.curve(point1, point2Control1, point2).tangent();
    }
}
