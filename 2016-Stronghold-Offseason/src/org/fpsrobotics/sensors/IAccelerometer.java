package org.fpsrobotics.sensors;

/**
 * Defines a 3 dimensional accelerometer.
 *
 */
public interface IAccelerometer
{
	public double getX();

	public double getY();

	public double getZ();
	
	public double getResultant();

	public void reset();
}
