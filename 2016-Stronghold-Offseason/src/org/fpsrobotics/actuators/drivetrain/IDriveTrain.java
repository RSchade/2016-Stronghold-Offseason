package org.fpsrobotics.actuators.drivetrain;

/**
 * Describes a drive train that can be controlled by the driver or the code in
 * autonomous mode.
 *
 */
public interface IDriveTrain
{
	public void driveForward(double speed);
	public void driveBackward(double speed);
	public void turnLeft(double speed, double angle);
	public void turnRight(double speed, double angle);
	public void stop();
}
