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

@Autonomous(name = "AutoPath", group = "Autonomous", preselectTeleOp = "mainEntryPoint")
public class pathingTest extends LinearOpMode {

    private Follower follower;
    private final PoseFactory poseFactory = PoseFactory.degrees();




    private final Pose start = poseFactory.of(61.2799, 10.4199, 270);
    private final Pose point1 = poseFactory.of(61.0837, 21.5943, 134);
    private final Pose point2 = poseFactory.of(13.9074, 47.1533, -178.6421);
    private final Pose point2Control1 = poseFactory.of(35.9157, 47.675, 0);
    private final Pose point3Start = poseFactory.of(13.9074, 47.1533, 178);
    private final Pose point3 = poseFactory.of(33.0353, 107.8995, 0);
    private final Pose point4 = poseFactory.of(60.0373, 133.3625, 89.9594);
    private final Pose point4Control1 = poseFactory.of(60.018, 106.1734, 0);
    private final Pose point5Start = poseFactory.of(60.0373, 133.3625, 90);
    private final Pose point5 = poseFactory.of(12.3838, 120.7948, 0);
    private final Pose point5Control1 = poseFactory.of(44.529, 91.3071, 0);

    //define modules

    private final Shooter shooter = new Shooter();
    private final Intake intake = new Intake();
    private final Feeder uptoyou = new Feeder();




    // Autonomous routine
    public Command autoRoutine() {
        return sequential(
                follow(follower, path1()),
                follow(follower, path2()),
                follow(follower, path3()),
                follow(follower, path4()),
                follow(follower, path5()),

                instant(() -> follower.stop())

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
        return Paths.line(start, point1).linear(start, point1);
    }

    public Path path2() {
        return Paths.curve(point1, point2Control1, point2).tangent();
    }

    public Path path3() {
        return Paths.line(point3Start, point3).linear(point3Start, point3);
    }

    public Path path4() {
        return Paths.curve(point3, point4Control1, point4).tangent();
    }

    public Path path5() {
        return Paths.curve(point5Start, point5Control1, point5).linear(point5Start, point5);
    }
}
