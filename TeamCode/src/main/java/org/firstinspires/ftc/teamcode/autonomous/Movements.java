package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.CenterStageRobot;
import org.firstinspires.ftc.teamcode.autonomous.enums.ParkingMode;
import org.firstinspires.ftc.teamcode.autonomous.interfaces.AutonomousBase;

public class Movements extends LinearOpMode implements AutonomousBase {

    CenterStageRobot myRobot;
    public Movements(CenterStageRobot myRobot){
        this.myRobot = myRobot;
    }
    @Override
    public void runOpMode() throws InterruptedException {

    }

    @Override
    public void defaultDropAndPark() {

    }

    @Override
    public void dropPixelCenter() {
        myRobot.pushPosition();
        myRobot.driveForward();
        sleep(1550);
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
        myRobot.pushPosition();
        myRobot.driveForward();
        sleep(1600);
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
        myRobot.pushPosition();
        myRobot.driveForward();
        sleep(1550);
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

    @Override
    public void shakePixel() {
        myRobot.driveBack();
        sleep(1100);
        myRobot.driveStop();
        myRobot.strafeLeft();
        sleep(4000);
        myRobot.driveForward();
        sleep(2000);
        myRobot.strafeLeft();
        sleep(2000);
        myRobot.driveStop();
    }

    public void park(ParkingMode parkMode){
        switch(parkMode){
            case REDFAR: parkRedFar();
            case BLUEFAR: parkBlueFar();
            case REDCLOSE: parkRedClose();
            case BLUECLOSE: parkBlueClose();
        }
        shakePixel();
    }

    private void parkBlueFar(){
        myRobot.driveBack();
        sleep(1200);
        myRobot.strafeLeft();
        sleep(8000);
        myRobot.driveStop();
        myRobot.driveForward();
        sleep(1900);
        myRobot.strafeLeft();
        sleep(1500);
        myRobot.driveStop();
    }

    private void parkBlueClose(){
        myRobot.driveBack();
        sleep(1100);
        myRobot.driveStop();
        myRobot.strafeLeft();
        sleep(4000);
        myRobot.driveForward();
        sleep(2000);
        myRobot.strafeLeft();
        sleep(2000);
        myRobot.driveStop();
    }

    private void parkRedFar(){
        myRobot.driveBack();
        sleep(1200);
        myRobot.strafeRight();
        sleep(8000);
        myRobot.driveStop();
        myRobot.driveForward();
        sleep(1900);
        myRobot.strafeRight();
        sleep(1500);
        myRobot.driveStop();
    }

    private void parkRedClose(){
        myRobot.driveBack();
        sleep(1100);
        myRobot.driveStop();
        myRobot.strafeLeft();
        sleep(4000);
        myRobot.driveForward();
        sleep(2000);
        myRobot.strafeLeft();
        sleep(2000);
        myRobot.driveStop();
    }
}
