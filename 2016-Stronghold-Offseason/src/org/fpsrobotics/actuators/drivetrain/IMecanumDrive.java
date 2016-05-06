package org.fpsrobotics.actuators.drivetrain;

public interface IMecanumDrive extends IDriveTrain
{
	public void driveLeft(double speed);
	public void driveRight(double speed);
	public void driveAtAngle(double speed, double angle);
}
