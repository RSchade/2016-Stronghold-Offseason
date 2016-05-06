package org.fpsrobotics.actuators;

import org.fpsrobotics.actuators.manipulator.EAugerPresets;
import org.fpsrobotics.actuators.manipulator.EShooterPresets;

/**
 * Describes a launcher to be used in the 2016 season for the game Stronghold.
 *
 */
public interface ILauncher
{

	// Lifter Functions
	/**
	 * 
	 * @param slow
	 *            - Used for Driver Control - Gamepad Button 1
	 */
	public void raiseShooter(boolean slow);

	/**
	 * 
	 * @param slow
	 *            - Used for Driver Control - Gamepad Button 1
	 */
	public void lowerShooter(boolean slow);

	public void stopShooterLifter();

	// private boolean isShooterAtTopLimit()

	// private boolean isShooterAtBottomLimit()

	public void moveShooterToPosition(double position);

	// Shooter Functions

	public void intakeBoulder();

	public void stopIntakeBoulder();

	public void launchBoulder();

	/**
	 * Used for Teleop if Sami wants manual spinUp
	 */
	public void spinShooterWheelsHigh();

	/**
	 * Used for Teleop if Sami wants manual spinUp
	 */
	public void spinShooterWheelsLow();

	// private void spinShooterWheels(double speed)

	// private void jostle()

	public void stopShooterWheels();

	// Auger Functions

	public void raiseAuger();

	// private void raiseAuger(double speed)

	// private void rampUpMotor(double speed)

	public void lowerAuger();
	
	public void lowerAugerForEndGame();
	
	public void raiseAugerForEndGame();

	// private void lowerAuger(double speed)

	// private void rampDownMotor(double speed)

	public void stopAugerLifter(boolean ramp);

	public void stopAugerWheels();

	// private void raiseAugerToTopLimit();

	// private void lowerAugerToBottomLimit();

	// private boolean isAugerAtBottomLimit()

	// private boolean isAugerAtTopLimit()

	public void moveAugerToPosition(int position);
	
//	public void moveAugerToPosition(int position, double lowerSpeed);

	// Sequences

	public void moveShooterToPreset(EShooterPresets preset);

	public void moveAugerToPreset(EAugerPresets preset);

	public void shootSequenceHigh();

	public void shootSequenceLow();

	public void shootSequenceLowAuto();

	public void shootSequenceHighAuto();
	
	public void moveAugerToPosition(int desiredPosition, double lowerSpeed);
	
	/**
	 * 
	 * @param desiredShooter
	 * @param desiredAuger
	 */
	public void moveShooterAndAugerToPreset(EShooterPresets desiredShooter, EAugerPresets desiredAuger);
	
	/**
	 * 
	 * @param desiredShooter
	 * @param desiredAuger
	 */
	public void moveShooterAndAugerToPreset(int desiredShooter, int desiredAuger);

	/**
	 * 
	 * @param desiredShooter
	 * @param desiredAuger
	 */
	public void moveShooterAndAugerToPreset(EShooterPresets desiredShooter, int desiredAuger);

	/**
	 * 
	 * @param desiredShooter
	 * @param desiredAuger
	 */
	public void moveShooterAndAugerToPreset(int desiredShooter, EAugerPresets desiredAuger);

	public void setAugerOverride(boolean launcherAndShooterOverride);
	public void setShooterOverride(boolean launcherAndShooterOverride);


}
