package org.fpsrobotics.autonomous;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.actuators.drivetrain.DriveTrainProfile;
import org.fpsrobotics.actuators.manipulator.ManipulatorPreset;
import org.fpsrobotics.robot.RobotStatus;

public class AutonPortcullis implements IAutonomousControl
{

	public void doAuto(EAutoPositions position)
	{
		if (RobotStatus.isAuto())
		{
			ActuatorConfig actuators = ActuatorConfig.getInstance();
			
			actuators.getMechanism().goToPreset(ManipulatorPreset.STANDARD_DEFENSE);
			actuators.getDriveTrain().driveAtProfile(new double[] {130, 0.8}, DriveTrainProfile.POSITION);
			actuators.getAutoShot().shoot(position);
		}
	}
}
