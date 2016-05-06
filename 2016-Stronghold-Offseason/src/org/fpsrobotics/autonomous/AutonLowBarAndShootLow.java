package org.fpsrobotics.autonomous;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.actuators.drivetrain.DriveTrainProfile;
import org.fpsrobotics.actuators.manipulator.ManipulatorPreset;
import org.fpsrobotics.robot.RobotStatus;

public class AutonLowBarAndShootLow implements IAutonomousControl
{
	private int SHOOT_ANGLE = 46;

	private double DRIVE_SPEED = 0.80;
	private double DRIVE_DISTANCE;

	public AutonLowBarAndShootLow()
	{
		if (RobotStatus.isAlpha())
		{
			DRIVE_DISTANCE = 178.165; // 140_000 // 178.165
		} else
		{
			DRIVE_DISTANCE = 39; // 130_000 //165.438
		}
	}
	@Override
	public void doAuto(EAutoPositions position)
	{
			ActuatorConfig actuators = ActuatorConfig.getInstance();
			
			actuators.getMechanism().goToPreset(ManipulatorPreset.LOW_BAR);
			actuators.getDriveTrain().driveAtProfile(new double[] {DRIVE_DISTANCE, DRIVE_SPEED}, DriveTrainProfile.POSITION);
			actuators.getDriveTrain().turnRight(SHOOT_ANGLE, 0.3);
			actuators.getMechanism().shootLow();
	}

}
