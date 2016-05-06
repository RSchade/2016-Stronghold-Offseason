package org.fpsrobotics.autonomous;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.actuators.drivetrain.DriveTrainProfile;
import org.fpsrobotics.actuators.manipulator.ManipulatorPreset;

public class AutonReachDefenses implements IAutonomousControl
{

	// private boolean timeBased = false;

	public AutonReachDefenses()
	{

	}

	@Override
	public void doAuto(EAutoPositions position)
	{
		ActuatorConfig actuators = ActuatorConfig.getInstance();

		actuators.getMechanism().goToPreset(ManipulatorPreset.STANDARD_DEFENSE);
		actuators.getDriveTrain().driveAtProfile(new double[] { 80, 0.5 }, DriveTrainProfile.POSITION);
	}

}
