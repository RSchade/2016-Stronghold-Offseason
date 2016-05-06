package org.fpsrobotics.autonomous;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.actuators.drivetrain.DriveTrainProfile;
import org.fpsrobotics.actuators.manipulator.ManipulatorPreset;

public class AutonBreachDefensesAndShoot implements IAutonomousControl
{
	// private boolean timeBased = false;
	// private boolean shoot = true;

	public AutonBreachDefensesAndShoot()
	{
	}

	@Override
	public void doAuto(EAutoPositions position)
	{
		ActuatorConfig actuators = ActuatorConfig.getInstance();

		actuators.getMechanism().goToPreset(ManipulatorPreset.STANDARD_DEFENSE);

		actuators.getDriveTrain().driveAtProfile(new double[] { 130, 1.0 }, DriveTrainProfile.POSITION);

		actuators.getAutoShot().shoot(position);

	}
}
