package org.fpsrobotics.actuators;

/**
 * Describes a generic pneumatic solenoid
 *
 */
public interface ISolenoid
{
	public void engage();

	public void disengage();

	public void set(ESolenoidValues value);
}
