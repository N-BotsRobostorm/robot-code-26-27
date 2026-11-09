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

@Autonomous(name = "AutoPath", group = "Autonomous")
public class pathingTest extends LinearOpMode {

    private Follower follower;

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(132.8154, 8.5986, 180);
    private final Pose point1 = poseFactory.of(112.9776, 33.4049, 144);
    private final Pose penStroke = poseFactory.of(104.1697, 129.3292, -68.283);
    private final Pose penStrokeThrough1 = poseFactory.of(113.2612, 46.5145, 0);
    private final Pose penStrokeThrough2 = poseFactory.of(112.4409, 59.7172, 0);
    private final Pose penStrokeThrough3 = poseFactory.of(111.6909, 72.9186, 0);
    private final Pose penStrokeThrough4 = poseFactory.of(112.7491, 86.1093, 0);
    private final Pose penStrokeThrough5 = poseFactory.of(112.7491, 99.3779, 0);
    private final Pose penStrokeThrough6 = poseFactory.of(114.5448, 112.3465, 0);
    private final Pose penStrokeThrough7 = poseFactory.of(113.4975, 125.4381, 0);
    private final Pose point3Start = poseFactory.of(104.1697, 129.3292, -68);
    private final Pose point3 = poseFactory.of(82.3096, 131.2308, -90);
    private final Pose point4 = poseFactory.of(108.5003, 94.3492, -54.6203);
    private final Pose point5Start = poseFactory.of(108.5003, 94.3492, -55);
    private final Pose point5 = poseFactory.of(131.1543, 46.1726, 180);

    // Autonomous routine
    public Command autoRoutine() {
        return sequential(
            follow(follower, path1()),
            follow(follower, penStroke()),
            follow(follower, path3()),
            follow(follower, path4()),
            follow(follower, path5())
        );
    }

    @Override
    public void runOpMode() {
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

    public Path penStroke() {
        return Paths.through(point1, penStrokeThrough1, penStrokeThrough2, penStrokeThrough3, penStrokeThrough4, penStrokeThrough5, penStrokeThrough6, penStrokeThrough7, penStroke).tangent();
    }

    public Path path3() {
        return Paths.line(point3Start, point3).linear(point3Start, point3);
    }

    public Path path4() {
        return Paths.line(point3, point4).tangent();
    }

    public Path path5() {
        return Paths.line(point5Start, point5).linear(point5Start, point5);
    }
}
