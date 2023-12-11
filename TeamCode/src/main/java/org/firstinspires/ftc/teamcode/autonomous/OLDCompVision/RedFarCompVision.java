package org.firstinspires.ftc.teamcode.autonomous.OLDCompVision;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.CenterStageRobot;
import org.firstinspires.ftc.teamcode.autonomous.interfaces.AutonomousBase;
import org.firstinspires.ftc.teamcode.autonomous.interfaces.TFBase;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;

import java.util.List;

@Autonomous(name = "Red COMP VISION: far - NEEDS TO BE TESTED",
        group = "Linear OpMode")
@Disabled
public class RedFarCompVision extends LinearOpMode implements AutonomousBase, TFBase {
    CenterStageRobot myRobot;
    FtcDashboard dashboard = FtcDashboard.getInstance();
    Telemetry dashTelemetry = dashboard.getTelemetry();

    // TFOD_MODEL_ASSET points to a model file stored in the project Asset location,
    // this is only used for Android Studio when using models in Assets.
    private static final String TFOD_MODEL_ASSET = "redpropRESMED.tflite";
    // TFOD_MODEL_FILE points to a model file stored onboard the Robot Controller's storage,
    // this is used when uploading models directly to the RC using the model upload interface.
    private static final String TFOD_MODEL_FILE = "/sdcard/FIRST/tflitemodels/redpropRESMED" +
            ".tflite";
    // Define the labels recognized in the model for TFOD (must be in training order!)
    private static final String[] LABELS = {
            "Prop",
    };

    /**
     * The variable to store our instance of the TensorFlow Object Detection processor.
     */
    private TfodProcessor tfod;

    /**
     * The variable to store our instance of the vision portal.
     */
    private VisionPortal visionPortal;

    ElapsedTime runtime = new ElapsedTime();
    // variable for autonomous
    public static int driveToProp = 1200, turnDistance = 300, calibratedCenter = 350,
        measuredVisionError = 60;

    // camera values, for calibrating to lighting and etc
    public static int exposure = 30, gain, whiteBalance, focus, ptz;
    @Override
    public void runOpMode() throws InterruptedException {
        myRobot = new CenterStageRobot(hardwareMap, telemetry);

        myRobot.startPosition();

        telemetry.addData("Status", "Initialized");
        dashTelemetry.addData("Status", "Initialized");

        dashTelemetry.update();
        telemetry.update();

        // start TensorFlow
        initTfod();

        waitForStart();
        boolean canRun = true;
        runtime.reset();
        while(opModeIsActive() && canRun){
            myRobot.pushPosition();
            // Stop infront of team prop and find where it is, COMP VISION STUFF HERE
            // check if Prop is in center ie, close to 450
            // we want to stop where we can see all three positions


            Recognition foundProp = getBestFit();
            if(runtime.seconds() > 5) {
                if (foundProp == null) {
                    //run default auto
                    defaultDropAndPark();
                    canRun = false; // make sure loop doesn't run again
                    telemetry.addData("I FOUND NOTHING", "try another position");
                } else {
                    //seek prop
                    double xLoc = (foundProp.getLeft() + foundProp.getRight()) / 2;
                    telemetry.addData("current Location of Prop: ", xLoc);
                    //if the prop is on the center it will print the value
                    // calibratedCenter +- error
                    if (xLoc < calibratedCenter - measuredVisionError) {
                        // TODO make sure this is left of robot
                        dropPixelLeft();
                        canRun = false; // make sure loop doesn't run again
                        telemetry.addData("I NEED TO GO: ", "LEFT");
                    } else if (xLoc > calibratedCenter + measuredVisionError) {
                        // TODO make sure this is right of the robot
                        dropPixelRight();
                        canRun = false; // make sure loop doesn't run again
                        telemetry.addData("I NEED TO GO: ", "RIGHT");

                    } else {
                        // if its in the center we can just run the defualt auto
                        defaultDropAndPark();
                        canRun = false; // make sure loop doesn't run again
                        telemetry.addData("I NEED TO GO: ", "CENTER");

                    }
                }
            }
            telemetry.update();
        }
    }

    @Override
    public void initTfod() {

        // Create the TensorFlow processor by using a builder.
        tfod = new TfodProcessor.Builder()

                // With the following lines commented out, the default TfodProcessor Builder
                // will load the default model for the season. To define a custom model to load,
                // choose one of the following:
                //   Use setModelAssetName() if the custom TF Model is built in as an asset (AS only).
                //   Use setModelFileName() if you have downloaded a custom team model to the Robot Controller.
                .setModelAssetName(TFOD_MODEL_ASSET)
                //.setModelFileName(TFOD_MODEL_FILE)

                // The following default settings are available to un-comment and edit as needed to
                // set parameters for custom models.
                .setModelLabels(LABELS)
                //.setIsModelTensorFlow2(true)
                //.setIsModelQuantized(true)
                //.setModelInputSize(300)
                //.setModelAspectRatio(16.0 / 9.0)

                .build();

        // Create the vision portal by using a builder.
        VisionPortal.Builder builder = new VisionPortal.Builder();
        WebcamName camera = hardwareMap.get(WebcamName.class, "Webcam");
        // Set the camera (webcam vs. built-in RC phone camera).
        builder.setCamera(camera);

        // Choose a camera resolution. Not all cameras support all resolutions.
//        builder.setCameraResolution(new Size(640, 480));
//
        // Enable the RC preview (LiveView).  Set "false" to omit camera monitoring.
        //builder.enableLiveView(true);

        // Set the stream format; MJPEG uses less bandwidth than default YUY2.
        //builder.setStreamFormat(VisionPortal.StreamFormat.YUY2);

        // Choose whether or not LiveView stops if no processors are enabled.
        // If set "true", monitor shows solid orange screen if no processors enabled.
        // If set "false", monitor shows camera view without annotations.
        //builder.setAutoStopLiveView(false);

        // Set and enable the processor.
        builder.addProcessor(tfod);

        // Build the Vision Portal, using the above settings.
        visionPortal = builder.build();

        // Set confidence threshold for TFOD recognitions, at any time.
        tfod.setMinResultConfidence(0.7f);

        // Disable or re-enable the TFOD processor at any time.
        //visionPortal.setProcessorEnabled(tfod, true);

    }

    /**
     * get the recognition that has the greatest confidence
     * @return recognition with the highest confidence
     */

    @Override
    public Recognition getBestFit(){
        List<Recognition> currentRecognitions = tfod.getRecognitions();

        if (currentRecognitions.isEmpty()) {
            return null; // No recognitions, return null or handle accordingly
        }

        Recognition bestFit = currentRecognitions.get(0); // Start with the first recognition

        // Iterate through the list of recognitions to find the one with the highest confidence
        for (Recognition recognition : currentRecognitions) {
            if (recognition.getConfidence() > bestFit.getConfidence()) {
                bestFit = recognition; // Update bestFit if a higher confidence is found
            }
        }

        return bestFit;
    }

    @Override
    public Recognition getSmallestBoundingBox() {
        List<Recognition> currentRecognitions = tfod.getRecognitions();

        if (currentRecognitions.isEmpty()) {
            return null; // No recognitions, return null or handle accordingly
        }

        Recognition smallestBoundingBox = currentRecognitions.get(0); // Start with the first recognition

        // Iterate through the list of recognitions to find the one with the smallest bounding box
        for (Recognition recognition : currentRecognitions) {
            double sizeCurrent = (recognition.getRight() - recognition.getLeft()) * (recognition.getBottom() - recognition.getTop());
            double sizeSmallest = (smallestBoundingBox.getRight() - smallestBoundingBox.getLeft()) * (smallestBoundingBox.getBottom() - smallestBoundingBox.getTop());

            if (sizeCurrent < sizeSmallest) {
                smallestBoundingBox = recognition; // Update smallestBoundingBox if a smaller size is found
            }
        }

        return smallestBoundingBox;
    }
    /**
     * Add telemetry about TensorFlow Object Detection (TFOD) recognitions.
     */
    @Override
    public void telemetryTfod() {

        List<Recognition> currentRecognitions = tfod.getRecognitions();
        telemetry.addData("# Objects Detected", currentRecognitions.size());

        // Step through the list of recognitions and display info for each one.
        for (Recognition recognition : currentRecognitions) {
            double x = (recognition.getLeft() + recognition.getRight()) / 2 ;
            double y = (recognition.getTop()  + recognition.getBottom()) / 2 ;

            telemetry.addData(""," ");
            telemetry.addData("Image", "%s (%.0f %% Conf.)",
                    recognition.getLabel(), recognition.getConfidence() * 100);
            telemetry.addData("- Position", "%.0f / %.0f", x, y);
            telemetry.addData("- raw left", "%.0f", recognition.getLeft());
            telemetry.addData("- raw right", " %.0f", recognition.getRight());
            telemetry.addData("- Size", "%.0f x %.0f",
                    recognition.getWidth(), recognition.getHeight());

            dashTelemetry.addData("", " ");
            dashTelemetry.addData("Image", "%s (%.0f %% Conf.)",
                    recognition.getLabel(), recognition.getConfidence() * 100);
            dashTelemetry.addData("- Position", "%.0f / %.0f", x, y);
            dashTelemetry.update();
        }   // end for() loop

    }

    @Override
    public void defaultDropAndPark() {
        dropPixelCenter();
//        park();
        shakePixel();
    }

    public void dropPixelCenter() {
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

    public void park() {
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

    @Override
    public void shakePixel() {
        myRobot.turnLeft();
        sleep(200);
        myRobot.turnRight();
        sleep(200);
        myRobot.turnLeft();
        sleep(500);
        myRobot.turnRight();
        sleep(200);
        myRobot.turnLeft();
        sleep(200);
        myRobot.turnRight();
        sleep(500);
        myRobot.driveStop();
    }
}
