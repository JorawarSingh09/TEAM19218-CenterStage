package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class GrabberArm {
    private Servo claw = null;
    private Servo wrist = null;
    private DcMotor arm = null;
    private GrabberState grabberState = GrabberState.CLOSED;

    public GrabberArm(HardwareMap hm){
        claw = hm.get(Servo.class, "claw");
        wrist = hm.get(Servo.class, "wrist");
        arm = hm.get(DcMotor.class, "arm");

        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setDirection(DcMotorSimple.Direction.REVERSE);
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void openClaw(){
        claw.setPosition(1);
        grabberState = GrabberState.OPEN;
    }

    public void closeClaw(){
        claw.setPosition(0);
        grabberState = GrabberState.CLOSED;
    }

    public GrabberState getGrabberState(){
        return grabberState;
    }

    public void setWristPosition(double pos){
        wrist.setPosition(pos + 0.36);
    }

    public void changeArmPosition(int pos){
        //if arm is at bottom dont move
        if(pos < 0 && arm.getCurrentPosition() < 0) return;

        double power = 0.3;
        arm.setTargetPosition(arm.getCurrentPosition() + pos);

        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        arm.setPower(power);

        while (arm.isBusy()) {
            // do some stuff here i guess
        }

        // Stop the motors after reaching the position
        arm.setPower(0.1);

        // Switch back to RUN_USING_ENCODER mode
//        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void setArmPosition(int pos){
        double power = 0.3;
        arm.setTargetPosition(pos);

        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        arm.setPower(power);

        while (arm.isBusy()) {
            // do some stuff here i guess
            if(arm.getCurrentPosition() > 0.8 * pos || arm.getCurrentPosition() < 0.2 * pos){
                arm.setPower(0.1);
            }
            else{
                arm.setPower(0.3);
            }
        }
    }

    public void manualPower(){
        arm.setPower(0.05);
    }
    public int getArm(){
        return arm.getCurrentPosition();
    }

    public double getWrist(){
        return wrist.getPosition();
    }

    public double getClaw(){
        return claw.getPosition();
    }
}
