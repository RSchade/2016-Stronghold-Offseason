package org.fpsrobotics.autonomous;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.actuators.drivetrain.DriveTrainProfile;
import org.fpsrobotics.actuators.manipulator.ManipulatorPreset;
import org.fpsrobotics.robot.RobotStatus;
import org.fpsrobotics.sensors.SensorConfig;

public class AutonLowBarAndShootHigh implements IAutonomousControl
{
	private int SHOOT_ANGLE = 46;

	private double DRIVE_SPEED = 1.00; // 0.80
	private double DRIVE_DISTANCE;

	public AutonLowBarAndShootHigh()
	{
		if (RobotStatus.isAlpha())
		{
			DRIVE_DISTANCE = 178.165; // 140_000 // 178.165
		} else
		{
			DRIVE_DISTANCE = 180; // 130_000 //165.438
		}
	}

	@Override
	public void doAuto(EAutoPositions position)
	{
		ActuatorConfig actuators = ActuatorConfig.getInstance();
		
		actuators.getMechanism().goToPreset(ManipulatorPreset.LOW_BAR);
		actuators.getDriveTrain().driveAtProfile(new double[] {DRIVE_DISTANCE, DRIVE_SPEED}, DriveTrainProfile.POSITION);
		SensorConfig.getInstance().getTimer().waitTimeInMillis(250);

		// lower numbers to raise shooter
		actuators.getDriveTrain().turnRight(0.3, SHOOT_ANGLE);
		actuators.getMechanism().goToPreset(ManipulatorPreset.SHOOT_HIGH_CORNER);

		SensorConfig.getInstance().getTimer().waitTimeInMillis(250);
		actuators.getMechanism().shootHigh();
	}
}
