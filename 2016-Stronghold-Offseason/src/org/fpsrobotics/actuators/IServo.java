package org.fpsrobotics.actuators;

/**
 * Describes a generic servo motor
 *
 */
public interface IServo
{
	public void engage();

	public void disengage();

	public void set(double value);

	public double get();
}
