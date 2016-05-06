package org.fpsrobotics.sensors;

/**
 * Defines a clock that allows processes to wait for a specified period of time.
 *
 */
public interface ITimer
{
	public void waitTimeInMillis(double milliseconds);

	public void waitTimeInSeconds(double seconds);

	public void waitTimeInMinutes(double minutes);
}
