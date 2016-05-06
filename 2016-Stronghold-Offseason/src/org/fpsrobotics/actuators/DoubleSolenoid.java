package org.fpsrobotics.actuators;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

/**
 * Used to control a double solenoid connected to the pneumatics breakout board
 * in the 2016 season.
 *
 */
public class DoubleSolenoid implements ISolenoid
{
	private edu.wpi.first.wpilibj.DoubleSolenoid solenoid;

	public DoubleSolenoid(edu.wpi.first.wpilibj.DoubleSolenoid solenoid)
	{
		this.solenoid = solenoid;
	}

	@Override
	public void engage()
	{
		solenoid.set(Value.kForward);
	}

	@Override
	public void disengage()
	{
		solenoid.set(Value.kReverse);
	}

	@Override
	public void set(ESolenoidValues value)
	{
		switch (value)
		{
		case ON:
			solenoid.set(Value.kForward);
			break;
		case OFF:
			solenoid.set(Value.kReverse);
			break;
		case NEUTRALSTATE:
			solenoid.set(Value.kOff);
			break;
		}
	}

}
