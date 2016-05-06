package org.fpsrobotics.sensors;

//import edu.wpi.first.wpilibj.Timer;

/**
 * A class that allows other parts of the code call it in order to wait for a certain amount of time.
 */
public class ClockTimer implements ITimer
{

	public ClockTimer()
	{

	}

	@Override
	public void waitTimeInMillis(double milliseconds)
	{
		// Timer.delay(milliseconds / 1000);
		try
		{
			Thread.sleep((long) milliseconds);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void waitTimeInSeconds(double seconds)
	{
		// Timer.delay(seconds);
		this.waitTimeInMillis(seconds * 60);
	}

	@Override
	public void waitTimeInMinutes(double minutes)
	{
		// Timer.delay(minutes * 60);
		this.waitTimeInMillis(minutes * 3600);
	}

}
