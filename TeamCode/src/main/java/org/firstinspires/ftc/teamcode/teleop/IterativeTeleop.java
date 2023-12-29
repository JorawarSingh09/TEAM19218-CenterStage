package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.CenterStageRobot;

@TeleOp(name = "Tele: IterativeOp", group = "Iterative OpMode")
public class IterativeTeleop extends OpMode {
	/**
	 * User-defined init method
	 * <p>
	 * This method will be called once, when the INIT button is pressed.
	 */

	private ElapsedTime runtime = new ElapsedTime();
	FtcDashboard dashboard;
	CenterStageRobot myRobot;

	Gamepad driver;
	Gamepad operator;

	@Override
	public void init(){
		myRobot = new CenterStageRobot(hardwareMap, telemetry);
		dashboard = FtcDashboard.getInstance();

		myRobot.startPosition();

		telemetry.addData("Status", "Initialized");
		telemetry.update();
	}

	/**
	 * User-defined loop method
	 * <p>
	 * This method will be called repeatedly during the period between when
	 * the play button is pressed and when the OpMode is stopped.
	 */
	@Override
	public void loop(){
		switch(myRobot.getRobotState()){
			case ClimbPosition:
				myRobot.raiseHooks();
			case DropPosition:
				myRobot.dropPosition();
			case PickupPosition:
				myRobot.pickupPosition();
			case DrivePosition:
				myRobot.drivePosition();
			default:
				break;

		}

		// raise claws for climb
		if(driver.dpad_up){
			myRobot.setRobotState(RobotState.ClimbPosition);
		}

		// Pickup Position
		if(operator.a){
			myRobot.setRobotState(RobotState.PickupPosition);
		}

		// Position where we drop the pixel
		if(operator.b){
			myRobot.setRobotState(RobotState.DropPosition);
		}

		// Climb
		if(driver.dpad_down){
			myRobot.climb();
		}

		// launch plane and reset the servo
		if(driver.y){
			myRobot.launchPlane();
		}

		// open claw
		if(operator.left_trigger > 0){
			myRobot.openClaw();
		}

		// close claw
		if(operator.right_trigger > 0){
			myRobot.closeClaw();
		}
	}
}
