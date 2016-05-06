package org.fpsrobotics.autonomous;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.actuators.drivetrain.DriveTrainProfile;
import org.fpsrobotics.actuators.manipulator.ManipulatorPreset;
import org.fpsrobotics.sensors.SensorConfig;

public class AutonChevalDeFrise implements IAutonomousControl, ISemiAutonomousMode
{
	ActuatorConfig actuators = ActuatorConfig.getInstance();
	
	public AutonChevalDeFrise()
	{
		
	}

	public void doAuto(EAutoPositions position)
	{	
		actuators.getMechanism().goToPreset(ManipulatorPreset.STANDARD_DEFENSE);

		actuators.getDriveTrain().driveAtProfile(new double[] {60, 0.8}, DriveTrainProfile.POSITION);
		
		SensorConfig.getInstance().getTimer().waitTimeInMillis(250);

		doSemiAutonomous();
		
		actuators.getAutoShot().shoot(position);
	}

	@Override
	public void doSemiAutonomous()
	{
		actuators.getDriveTrain().driveAtProfile(new double[] {6, -0.8}, DriveTrainProfile.POSITION);

		try
		{
			Thread.sleep(250);
		} catch (InterruptedException e)
		{
			return;
		}

		actuators.getMechanism().goToPreset(ManipulatorPreset.LOW_BAR);

		actuators.getDriveTrain().driveAtProfile(new double[] {90, 0.8}, DriveTrainProfile.POSITION);
	}
}
