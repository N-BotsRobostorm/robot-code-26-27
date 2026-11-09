package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {
    public static Follower create(HardwareMap h) {
        // return new Follower(Drivetrain, Localizer, Foresight);
        return null;
    }
    public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
        c.frontLeftName.set("leftFront");
        c.frontRightName.set("rightFront");
        c.backLeftName.set("leftRear");
        c.backRightName.set("rightRear");
        c.frontLeftDirection.set(DcMotorSimple.Direction.FORWARD);
        c.frontRightDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backLeftDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backRightDirection.set(DcMotorSimple.Direction.REVERSE);
    });


    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("pinpoint");
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_SWINGARM_POD);
        c.xPodOffset.set(-1.935844271201787);
        c.yPodOffset.set(-5.054050355445682);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });


    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(6.8947653465278735);
                Controller secondaryTranslationalForward = Controller.proportional(2.5474315640189498);
                Controller primaryTranslationalLateral = Controller.proportional(0.447596495656834);
                Controller secondaryTranslationalLateral = Controller.proportional(0.16537494514656575);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.019089901338780343));
                c.brake.set(Controller.proportionalFeedforward(0.01622641613796329));

                c.headingFeedback.set(Controller.proportional(2.9762518895127776));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.0384602436666725, 0.003969046879625988));

                c.linearBrakeCoefficients.set(Matrix.diag(-0.008938918371102382, 0.06485694398952264));
                c.quadraticBrakeCoefficients.set(Matrix.diag(0.004373451971918321, 7.937113439850185E-4));

                c.maxAchievableForwardVelocity.set(59.88839888008556);
                c.maxAchievableStrafeVelocity.set(54.78805562509344);
                c.naturalForwardDeceleration.set(28.739821059331486);
                c.naturalStrafeDeceleration.set(115.27811497372699);
            }
    );
}