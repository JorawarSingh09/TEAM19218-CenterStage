package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.autonomous.enums.PropPosition;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;

import java.util.List;

public class TensorFlow {

	// TFOD_MODEL_ASSET points to a model file stored in the project Asset location,
	// this is only used for Android Studio when using models in Assets.
	private String TFOD_MODEL_ASSET;
	// TFOD_MODEL_FILE points to a model file stored onboard the Robot Controller's storage,
	// this is used when uploading models directly to the RC using the model upload interface.
	private String TFOD_MODEL_FILE;
	// Define the labels recognized in the model for TFOD (must be in training order!)
	private static final String[] LABELS = {"TEAM PROP",};

	private TfodProcessor tfod;
	private VisionPortal visionPortal;

	private HardwareMap hardwareMap;

	private int calibratedCenter, measuredError;

	public TensorFlow(String TFOD_MODEL_ASSET, HardwareMap hardwareMap){
		this.TFOD_MODEL_ASSET = TFOD_MODEL_ASSET;
		this.hardwareMap = hardwareMap;
		this.calibratedCenter = 350;
		this.measuredError = 70;

		initTfod();
	}

	public TensorFlow(String TFOD_MODEL_ASSET, HardwareMap hardwareMap, int calibratedCenter, int measuredError){
		this.TFOD_MODEL_ASSET = TFOD_MODEL_ASSET;
		this.hardwareMap = hardwareMap;
		this.calibratedCenter = calibratedCenter;
		this.measuredError = measuredError;

		initTfod();
	}

	public void initTfod(){

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
	 *
	 * @return recognition with the highest confidence
	 */
	public Recognition getBestFit(){
		List<Recognition> currentRecognitions = tfod.getRecognitions();

		if(currentRecognitions.isEmpty()){
			return null; // No recognitions, return null or handle accordingly
		}

		Recognition bestFit = currentRecognitions.get(0); // Start with the first recognition

		// Iterate through the list of recognitions to find the one with the highest confidence
		for(Recognition recognition : currentRecognitions){
			if(recognition.getConfidence() > bestFit.getConfidence()){
				bestFit = recognition; // Update bestFit if a higher confidence is found
			}
		}

		return bestFit;
	}

	public Recognition getSmallestBoundingBox(){
		List<Recognition> currentRecognitions = tfod.getRecognitions();

		if(currentRecognitions.isEmpty()){
			return null; // No recognitions, return null or handle accordingly
		}

		Recognition smallestBoundingBox = currentRecognitions.get(0); // Start with the first recognition

		// Iterate through the list of recognitions to find the one with the smallest bounding box
		for(Recognition recognition : currentRecognitions){
			double sizeCurrent = (recognition.getRight() - recognition.getLeft()) * (recognition.getBottom() - recognition.getTop());
			double sizeSmallest = (smallestBoundingBox.getRight() - smallestBoundingBox.getLeft()) * (smallestBoundingBox.getBottom() - smallestBoundingBox.getTop());

			if(sizeCurrent < sizeSmallest){
				smallestBoundingBox = recognition; // Update smallestBoundingBox if a smaller size is found
			}
		}

		return smallestBoundingBox;
	}

	public TfodProcessor getTfod(){
		return tfod;
	}

	public float getObjectCenter(Recognition recognition){
		if(recognition == null){
			return -1;
		}
		return (recognition.getLeft() + recognition.getRight()) / 2;
	}

	public PropPosition getPropPosition(Recognition recog){
		float objectCenter = getObjectCenter(recog);
		if(objectCenter < calibratedCenter - measuredError){
			return PropPosition.LEFT;
		}else if(objectCenter > calibratedCenter + measuredError){
			return PropPosition.RIGHT;
		}
		return PropPosition.CENTER;
	}

	public float getLeftBoundary(){
		return calibratedCenter - measuredError;
	}

	public float getRightBoundary(){
		return calibratedCenter + measuredError;
	}
}