package org.fpsrobotics.autonomous;

import org.fpsrobotics.actuators.drivetrain.DriveTrainProfile;
import org.fpsrobotics.actuators.drivetrain.MullenatorDrive;
import org.fpsrobotics.actuators.manipulator.ManipulatorPreset;
import org.fpsrobotics.actuators.manipulator.MullenatorMechanism;

public class AutoShot
{
	private MullenatorMechanism mechanism;
	private MullenatorDrive driveTrain;

	private double TURN_SPEED = 0.3; // used to be 0.2
	private double DRIVE_SPEED = 0.7;

	public AutoShot(MullenatorMechanism mechanism, MullenatorDrive driveTrain)
	{
		this.mechanism = mechanism;
		this.driveTrain = driveTrain;
	}

	public void shoot(EAutoPositions position)
	{
		// NEW
		switch (position)
		{
		case ZERO:
			System.out.println("Didn't know how to shoot it -Raul");
			break;
		case TWO:
			System.out.println("Position Two");
			driveTrain.driveAtProfile(new double[] { 3, DRIVE_SPEED / 5 }, DriveTrainProfile.POSITION);
			driveTrain.turnRight(TURN_SPEED, 33);
			mechanism.goToPreset(ManipulatorPreset.POSITION_TWO_AUTO);
			mechanism.shootHigh();
			break;
		case THREE:
			System.out.println("Position Three");
			driveTrain.driveAtProfile(new double[] { 3, DRIVE_SPEED / 5 }, DriveTrainProfile.POSITION);
			driveTrain.turnRight(TURN_SPEED, 15);
			mechanism.goToPreset(ManipulatorPreset.POSITION_THREE_AUTO);
			mechanism.shootHigh();
			break;
		case FOUR:
			System.out.println("Position Four");
			driveTrain.driveAtProfile(new double[] { 3, DRIVE_SPEED / 5 }, DriveTrainProfile.POSITION);
			driveTrain.turnLeft(TURN_SPEED, 5);
			mechanism.goToPreset(ManipulatorPreset.POSITION_FOUR_AUTO);
			mechanism.shootHigh();
			break;
		case FIVE:
			System.out.println("Position Five");
			driveTrain.driveAtProfile(new double[] { 3, DRIVE_SPEED / 5 }, DriveTrainProfile.POSITION);
			driveTrain.turnLeft(TURN_SPEED, 22);
			mechanism.goToPreset(ManipulatorPreset.POSITION_FIVE_AUTO);
			mechanism.shootHigh();
			break;
		}
	}
}
