package org.fpsrobotics.sensors;

/**
 * Defines a power distribution board with it's allocated sensors, our board for
 * the 2016 season is the PDP.
 *
 */
public interface IPowerBoard
{
	public double getVoltage();

	public double getCurrent(int channel);

	public double getPower(int channel);

	public double getTemperature();
}
