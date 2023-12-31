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
		driver = gamepad1;
		operator = gamepad2;

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

		telemetry.addData("State: ", myRobot.getRobotState());
		telemetry.update();

		myRobot.drive(driver.left_stick_y, driver.left_stick_x, driver.right_stick_x);
		// raise claws for climb
		if(driver.dpad_up){
			myRobot.setRobotState(RobotState.ClimbPosition);
		}

		// Pickup Position
		if(driver.a){
			myRobot.setRobotState(RobotState.PickupPosition);
		}

		// Position where we drop the pixel
		if(driver.b){
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
		if(driver.left_trigger > 0){
			myRobot.openClaw();
		}

		// close claw
		if(driver.right_trigger > 0){
			myRobot.closeClaw();
		}

		switch(myRobot.getRobotState()){
			case ClimbPosition:
				myRobot.raiseHooks();
				break;
			case DropPosition:
				myRobot.dropPosition();
				break;
			case PickupPosition:
				myRobot.pickupPosition();
				break;
			case DrivePosition:
				myRobot.drivePosition();
				break;
			default:
				break;
		}
	}
}
