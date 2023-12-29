package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class LinearSlides {
    //Motors
    private DcMotor leftLinearSlide;
    private DcMotor rightLinearSlide;

    public LinearSlides(HardwareMap hm){
        leftLinearSlide = hm.get(DcMotor.class, "l_linear_slides");
        rightLinearSlide = hm.get(DcMotor.class, "r_linear_slides");

        //direction of motors
        leftLinearSlide.setDirection(DcMotorSimple.Direction.FORWARD);
        rightLinearSlide.setDirection(DcMotorSimple.Direction.REVERSE);

        // Set the motors to run with encoders.
        leftLinearSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightLinearSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        // Reset encoders
        leftLinearSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightLinearSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Hold position
        leftLinearSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightLinearSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    /**
    Change Position
     INPUT: pos - change we want from current position
     Move the linear slide up or down from current position
     */
    public void changePosition(int pos){

        // do nothing if linear slides are already at the bottom or top
        int newPos =
                Math.max(getPosition()[0], getPosition()[1]) + pos;
        if((pos > 0 &&  newPos > 1550) || (pos < 0 && newPos < 0) ) return;

        double power = 0.6;
        leftLinearSlide.setTargetPosition(leftLinearSlide.getCurrentPosition() + pos);
        rightLinearSlide.setTargetPosition(rightLinearSlide.getCurrentPosition() + pos);

        leftLinearSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightLinearSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftLinearSlide.setPower(power);
        rightLinearSlide.setPower(power);

        while (leftLinearSlide.isBusy() && rightLinearSlide.isBusy()) {
            // do some stuff here i guess
        }

        // Stop the motors after reaching the position
        leftLinearSlide.setPower(0.1);
        rightLinearSlide.setPower(0.1);

        // Switch back to RUN_USING_ENCODER mode
//        leftLinearSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        rightLinearSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    private void setPosition(int pos){
        leftLinearSlide.setTargetPosition(pos);
        rightLinearSlide.setTargetPosition(pos);

        leftLinearSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightLinearSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftLinearSlide.setPower(0.8);
        rightLinearSlide.setPower(0.8);

        while (leftLinearSlide.isBusy() && rightLinearSlide.isBusy()) {
            // do some stuff here i guess
        }

        // Stop the motors after reaching the position
        leftLinearSlide.setPower(0.1);
        rightLinearSlide.setPower(0.1);
    }

    public void bottomPosition(){
        setPosition(0);
    }

    public void topPosition(){
        // power at 80
        setPosition(1200);
    }

    public void climbPosition(){
        setPosition(1240);
    }

    public void climb(double power){
        leftLinearSlide.setPower(power);
        rightLinearSlide.setPower(power);
        leftLinearSlide.setTargetPosition(300);
        rightLinearSlide.setTargetPosition(300);

        leftLinearSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightLinearSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftLinearSlide.setPower(power);
        rightLinearSlide.setPower(power);
    }

    public void reset(){
        leftLinearSlide.setTargetPosition(1400);
        rightLinearSlide.setTargetPosition(1400);

        leftLinearSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightLinearSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftLinearSlide.setPower(0.4);
        rightLinearSlide.setPower(0.4);

        while (leftLinearSlide.isBusy() && rightLinearSlide.isBusy()) {
            // do some stuff here i guess
        }

        // Stop the motors after reaching the position
        leftLinearSlide.setPower(0.1);
        rightLinearSlide.setPower(0.1);
    }
    public void manualDrive(boolean up){
        double power =  0.1;
        if(!up) power = - 0.1;
        leftLinearSlide.setPower(power);
        rightLinearSlide.setPower((power));
    }
    public int[] getPosition() {
        int[] pos = {leftLinearSlide.getCurrentPosition(),
                    rightLinearSlide.getCurrentPosition()};
        return pos;
    }
}
