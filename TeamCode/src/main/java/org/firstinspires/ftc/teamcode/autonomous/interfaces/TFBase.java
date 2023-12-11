package org.firstinspires.ftc.teamcode.autonomous.interfaces;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

public interface TFBase {

    /**
     * Initialise TFOD
     */
    public void initTfod();

    /**
     * Add telemetry about TensorFlow Object Detection (TFOD) recognitions.
     */
    public void telemetryTfod();

    /**
     * get the recognition that has the greatest confidence
     * @return recognition with the highest confidence
     */
    public Recognition getBestFit();
    public Recognition getSmallestBoundingBox();





}
