package org.fpsrobotics.actuators;

import edu.wpi.first.wpilibj.Solenoid;

/**
 * This controls a single pneumatic solenoid, one that can either let air
 * through a single tube or restrict it.
 *
 */
public class SingleSolenoid implements ISolenoid
{
	private Solenoid solenoid;

	public SingleSolenoid(Solenoid solenoid)
	{
		this.solenoid = solenoid;
	}

	@Override
	public void engage()
	{
		solenoid.set(true);
	}

	@Override
	public void disengage()
	{
		solenoid.set(false);
	}

	@Override
	public void set(ESolenoidValues value)
	{
		switch (value)
		{
		case ON:
			solenoid.set(true);
			break;
		default:
			solenoid.set(false);

		}
	}

}
