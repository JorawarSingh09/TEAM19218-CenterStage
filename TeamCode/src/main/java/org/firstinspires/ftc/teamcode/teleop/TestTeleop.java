package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.CenterStageRobot;

@TeleOp(name = "Tele: CompOp", group = "Linear OpMode")
public class TestTeleop extends LinearOpMode {
	private ElapsedTime runtime = new ElapsedTime();
	FtcDashboard dashboard;
	CenterStageRobot myRobot;
	Gamepad driver;
	Gamepad operator;

	@Override
	public void runOpMode() throws InterruptedException{
		myRobot = new CenterStageRobot(hardwareMap, telemetry);
		dashboard = FtcDashboard.getInstance();
		myRobot.startPosition();

		telemetry.addData("Status", "Initialized");
		telemetry.update();
		waitForStart();
		runtime.reset();
		while(opModeIsActive()){
			driver = gamepad1;
			operator = gamepad2;
			// Driver Controls ===================
			// Drive
			double axial = -gamepad1.left_stick_y;
			double lateral = gamepad1.left_stick_x;
			double yaw = gamepad1.right_stick_x;
			myRobot.drive(axial, lateral, yaw);

			// raise claws for climb
			if(driver.dpad_up){
				myRobot.raiseHooks();
			}
			// Climb
			if(driver.dpad_down){
				myRobot.climb();
			}
			// launch plane and reset the servo
			if(driver.y){
				myRobot.launchPlane();
				sleep(1000);
				myRobot.resetLauncher();
			}
			// Operator Controls =================
			// open claw
			if(operator.left_trigger > 0){
				myRobot.openClaw();
			}

			// close claw
			if(operator.right_trigger > 0){
				myRobot.closeClaw();
			}

			// Adjusting linear slides up
			if(operator.right_bumper){
				myRobot.adjustSlides(30);
			}

			if(operator.left_bumper){
				myRobot.adjustSlides(-30);
			}

			// Pickup Position
			if(operator.a){
				myRobot.pickupPosition();
			}

			// drive position
			if(operator.x){
				myRobot.drivePosition();
			}
			// Position where we drop the pixel
			if(operator.b){
				myRobot.dropPosition();
			}
		}
	}
}
