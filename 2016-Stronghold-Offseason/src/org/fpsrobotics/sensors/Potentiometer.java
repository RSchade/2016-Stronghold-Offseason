package org.fpsrobotics.sensors;

import org.fpsrobotics.PID.IPIDFeedbackDevice;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;

/**
 * 
 * Defines an analog potentiometer.
 *
 */
public class Potentiometer implements IPIDFeedbackDevice
{
	private AnalogInput pot;
	private boolean isEnabled;
	
	public Potentiometer(AnalogInput pot)
	{
		this.pot = pot;
		isEnabled = true;
	}

	@Override
	public double getCount()
	{
		if (isEnabled)
		{
			return pot.getValue();
		} else
		{
			return 0;
		}
	}

	@Override
	public void enable()
	{
		isEnabled = true;
	}

	@Override
	public void disable()
	{
		isEnabled = false;
	}

	@Override
	public void resetCount()
	{
		pot.resetAccumulator();
	}

	@Override
	public FeedbackDevice whatPIDDevice()
	{
		return FeedbackDevice.AnalogPot;
	}

	@Override
	public double getError()
	{
		return 0;
	}

	@Override
	public double getRate()
	{
		return 0;
	}

	@Override
	public double getDistance()
	{
		if (isEnabled)
		{
			return pot.getValue();
		} else
		{
			return 0;
		}
	}

}
