package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.CenterStageRobot;
import org.firstinspires.ftc.teamcode.autonomous.enums.ParkingMode;

@Autonomous(name = "USED FOR TESTING ONLY", group = "Linear OpMode")
public class DriveCalibrate extends LinearOpMode {
    CenterStageRobot myRobot;

    Movements myMovements;
    Gamepad driver;
    Gamepad operator;

    @Override
    public void runOpMode() throws InterruptedException {
        myRobot = new CenterStageRobot(hardwareMap, telemetry);
        myMovements = new Movements(myRobot);

        myRobot.startPosition();

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();
        boolean canRun = true;
        myMovements.park(ParkingMode.REDCLOSE);
        while(opModeIsActive() && canRun){




            canRun = false; // make sure loop doesn't run again
        }
    }
}
