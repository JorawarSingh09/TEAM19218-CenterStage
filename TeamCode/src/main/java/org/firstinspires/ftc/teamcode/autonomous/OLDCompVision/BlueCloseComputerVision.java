package org.firstinspires.ftc.teamcode.autonomous.OLDCompVision;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.CenterStageRobot;
import org.firstinspires.ftc.teamcode.autonomous.interfaces.AutonomousBase;
import org.firstinspires.ftc.teamcode.autonomous.interfaces.TFBase;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;

import java.util.List;

@Autonomous(name = "BLUE COMP VISION: close - Needs to be tested",
        group = "Linear OpMode")
@Disabled
public class BlueCloseComputerVision extends LinearOpMode implements TFBase, AutonomousBase {
    private static final String TFOD_MODEL_ASSET = "bluepropRESMED.tflite";
    private static final String TFOD_MODEL_FILE = "/sdcard/FIRST/tflitemodels/bluepropRESMED.tflite";
    private static final String[] LABELS = {
            "Prop",
    };
    CenterStageRobot myRobot;
    private TfodProcessor tfod;
    private VisionPortal visionPortal;
    public int calibratedCenter = 350, measuredVisionError = 60;
    public static int driveToScanArea = 1300, turnDistance = 300;
    private ElapsedTime runtime = new ElapsedTime();
    @Override
    public void runOpMode() throws InterruptedException {
        myRobot = new CenterStageRobot(hardwareMap, telemetry);
        initTfod();

        myRobot.startPosition();

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();
        boolean canRun = true;

        runtime.reset();
        while(opModeIsActive() && canRun){
            myRobot.pushPosition();
            if(runtime.seconds() > 5) {
                //give the robot some time to find the object
                Recognition foundProp = getBestFit();
                if (foundProp == null) {
                    //run default auto if nothing found
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
                telemetry.update();
            }
        }
    }

    @Override
    public void defaultDropAndPark() {
        dropPixelCenter();
        park();
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
        myRobot.driveStop();

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
        tfod.setMinResultConfidence(0.6f);

        // Disable or re-enable the TFOD processor at any time.
        //visionPortal.setProcessorEnabled(tfod, true);
    }

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
        }   // end for() loop
    }

    @Override
    public Recognition getBestFit() {
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
}
