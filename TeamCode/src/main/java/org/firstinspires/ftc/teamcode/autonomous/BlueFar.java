package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.CenterStageRobot;
import org.firstinspires.ftc.teamcode.autonomous.Movements;
import org.firstinspires.ftc.teamcode.autonomous.enums.ParkingMode;
import org.firstinspires.ftc.teamcode.autonomous.enums.PropPosition;
import org.firstinspires.ftc.teamcode.subsystems.TensorFlow;

@Autonomous(name = "Blue Far", group = "Linear OpMode")
public class BlueFar extends LinearOpMode {
	CenterStageRobot myRobot;
	ElapsedTime runtime = new ElapsedTime();
	TensorFlow tf;
	Movements myMovements;

	@Override
	public void runOpMode() throws InterruptedException{
		myRobot = new CenterStageRobot(hardwareMap, telemetry);
		myMovements = new Movements(myRobot);

		myRobot.startPosition();

		telemetry.addData("Status", "Initialized");
		telemetry.update();

		tf = new TensorFlow("bluepropRESMED.tflite", hardwareMap);

		waitForStart();
		boolean canRun = true;
		runtime.reset();

		while(runtime.seconds() < 2){
			telemetry.addData("Scanning for Prop", "Please Wait");
			telemetry.update();
		}

		Recognition myProp = tf.getBestFit();
		PropPosition pos = tf.getPropPosition(myProp);
		float centerCOORD = tf.getObjectCenter(myProp);
		telemetry.addData("Found prop at ", centerCOORD);
		telemetry.update();

		while(opModeIsActive() && canRun){
			if(centerCOORD < tf.getLeftBoundary()){
				myMovements.dropPixelLeft();
			}else if(centerCOORD > tf.getRightBoundary()){
				myMovements.dropPixelRight();
				myMovements.park(ParkingMode.BLUEFAR);
			}else{
				myMovements.dropPixelCenter();
			}
			canRun = false;
		}
	}

}
