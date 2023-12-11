package org.firstinspires.ftc.teamcode.autonomous.interfaces;

public interface AutonomousBase {
    /**
     * If no recognition found place pixel on center tape and park.
     */
    public void defaultDropAndPark();

    /**
     * Strafe and place pixel on Center and return to Positio
     */
    public void dropPixelCenter();

    /**
     * Strafe and place pixel on left and return to Positio
     */
    public void dropPixelLeft();

    /**
     * Strafe and place pixel on Right and return to Position
     */
    public void dropPixelRight();

    /**
     * drop a pixel into parking area by shaking
     */
    public void shakePixel();

}
