package org.fpsrobotics.sensors;

import edu.wpi.first.wpilibj.DigitalInput;

/**
 * Switch that turns on when above a value set on the switch itself.
 *
 */
public class IS1000PressureSwitch implements ILimitSwitch
{

	private DigitalInput pressureSwitch;
	private boolean isInverted = false;

	public IS1000PressureSwitch(int channel)
	{
		pressureSwitch = new DigitalInput(channel);
	}

	public IS1000PressureSwitch(int channel, boolean inverted)
	{
		this.isInverted = inverted;
		pressureSwitch = new DigitalInput(channel);
	}

	@Override
	public boolean isHit()
	{
		if (isInverted)
		{
			return !pressureSwitch.get();
		} else
		{
			return pressureSwitch.get();
		}
	}

}
