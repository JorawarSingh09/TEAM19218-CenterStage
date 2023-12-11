package org.firstinspires.ftc.teamcode.autonomous.compVision;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.CenterStageRobot;
import org.firstinspires.ftc.teamcode.autonomous.Movements;
import org.firstinspires.ftc.teamcode.autonomous.enums.PropPosition;

@Autonomous(name = "CompVision Calibration",
        group = "Linear OpMode")
public class ComputerVisionTest extends LinearOpMode {

    CenterStageRobot myRobot;
    FtcDashboard dashboard = FtcDashboard.getInstance();
    Telemetry dashTelemetry = dashboard.getTelemetry();
    ElapsedTime runtime = new ElapsedTime();
    TensorFlow tf;
    Movements myMovements;
    @Override
    public void runOpMode() throws InterruptedException {
        myRobot = new CenterStageRobot(hardwareMap, telemetry);
        myMovements = new Movements(myRobot);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        tf = new TensorFlow("redpropRESMED.tflite", hardwareMap);

        waitForStart();
        boolean canRun = true;
        runtime.reset();

        while(runtime.seconds() < 2){}

        Recognition myProp = tf.getBestFit();
        PropPosition pos = tf.getPropPosition(myProp);
        float centerCOORD = tf.getObjectCenter(myProp);

        while (opModeIsActive() && canRun) {
            if(centerCOORD < tf.getLeftBoundary()){
                myMovements.dropPixelLeft();
            } else if (centerCOORD > tf.getRightBoundary()) {
                myMovements.dropPixelRight();
            }
            else{
                myMovements.dropPixelCenter();
            }
            canRun = false;
        }
    }
}


