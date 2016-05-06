package org.fpsrobotics.actuators.drivetrain;

public interface ITankDrive extends IDriveTrain
{
	public void drive(double leftSpeed, double rightSpeed);
}
