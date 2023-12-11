package org.firstinspires.ftc.teamcode.autonomous.normal;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.CenterStageRobot;
import org.firstinspires.ftc.teamcode.autonomous.interfaces.AutonomousBase;

@Autonomous(name = "Red: far - NEEDS TO BE TESTED", group = "Linear OpMode")
@Disabled
public class RedFarGuess extends LinearOpMode implements AutonomousBase {
    CenterStageRobot myRobot;
    public static int driveToScanArea = 1500, turnDistance = 300;
    @Override
    public void runOpMode() throws InterruptedException {
        myRobot = new CenterStageRobot(hardwareMap, telemetry);

        myRobot.startPosition();

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();
        boolean canRun = true;

        while(opModeIsActive() && canRun){
            myRobot.pushPosition();
            defaultDropAndPark();
        }
    }

    @Override
    public void defaultDropAndPark() {
        dropPixelCenter();
        park();
        shakePixel();

    }

    @Override
    public void dropPixelCenter() {
        myRobot.pushPosition();
        myRobot.driveForward();
        sleep(1800);
        myRobot.driveStop();
        myRobot.pickupPosition();
        myRobot.driveStop();
        sleep(100);
        myRobot.driveBack();
        sleep(200);
        myRobot.driveStop();
        myRobot.closeClaw();
        myRobot.drivePosition();
    }

    @Override
    public void dropPixelLeft() {
        myRobot.driveForward();
        sleep(1300);
        myRobot.turnLeft();
        sleep(800);
        myRobot.driveForward();
        sleep(150);
        myRobot.driveStop();
        myRobot.pickupPosition();
        myRobot.driveBack();
        sleep(450);
        myRobot.driveStop();
        myRobot.closeClaw();
        myRobot.drivePosition();
    }

    @Override
    public void dropPixelRight() {
        myRobot.driveForward();
        sleep(1300);
        myRobot.turnRight();
        sleep(800);
        myRobot.driveForward();
        sleep(150);
        myRobot.driveStop();
        myRobot.pickupPosition();
        myRobot.driveBack();
        sleep(450);
        myRobot.driveStop();
        myRobot.closeClaw();
        myRobot.drivePosition();
    }

    public void park() {
        myRobot.driveBack();
        sleep(1100);
        myRobot.strafeRight();
        sleep(8000);
        myRobot.driveStop();
        myRobot.driveForward();
        sleep(1900);
        myRobot.strafeRight();
        sleep(1500);
        myRobot.driveStop();
    }

    @Override
    public void shakePixel() {
        myRobot.turnLeft();
        sleep(1600);//turn 180 degrees
        myRobot.turnLeft();
        sleep(200);
        myRobot.turnRight();
        sleep(200);
        myRobot.turnLeft();
        sleep(500);
        myRobot.turnRight();
        sleep(200);
        myRobot.turnLeft();
        sleep(200);
        myRobot.turnRight();
        sleep(500);
        myRobot.driveStop();
    }
}
