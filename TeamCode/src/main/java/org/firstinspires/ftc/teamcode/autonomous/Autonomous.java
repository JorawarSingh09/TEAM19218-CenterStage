package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="Auto Main")
public class Autonomous extends LinearOpMode {
    int ticks = 1120;
    double newTarget;
    private DcMotorEx FrontLeft;
    private DcMotorEx FrontRight;
    private DcMotorEx BackLeft;
    private DcMotorEx BackRight;
    ElapsedTime timer;


    @Override
    public void runOpMode() {
        FrontLeft = hardwareMap.get(DcMotorEx.class, "MotorFL");
        FrontRight = hardwareMap.get(DcMotorEx.class, "MotorFR");
        BackLeft = hardwareMap.get(DcMotorEx.class, "MotorBL");
        BackRight = hardwareMap.get(DcMotorEx.class, "MotorBR");

        // Direction of Motors
        FrontLeft.setDirection(DcMotor.Direction.FORWARD);
        FrontRight.setDirection(DcMotor.Direction.REVERSE);
        BackLeft.setDirection(DcMotor.Direction.FORWARD);
        BackRight.setDirection(DcMotor.Direction.REVERSE);

        FrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        FrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        timer = new ElapsedTime();
        waitForStart();
        timer.reset();

        while(opModeIsActive()) {
            FrontLeft.setTargetPosition(ticks);
            FrontRight.setTargetPosition(ticks);
            BackRight.setTargetPosition(ticks);
            BackLeft.setTargetPosition(ticks);
            FrontLeft.setTargetPositionTolerance(3);
            FrontRight.setTargetPositionTolerance(3);
            BackRight.setTargetPositionTolerance(3);
            BackLeft.setTargetPositionTolerance(3);
            FrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            FrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            BackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            BackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            FrontLeft.setPower(1);
            FrontRight.setPower(1);
            BackRight.setPower(1);
            BackLeft.setPower(1);

        }
    }
}