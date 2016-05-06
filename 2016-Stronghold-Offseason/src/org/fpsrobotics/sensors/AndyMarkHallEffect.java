package org.fpsrobotics.sensors;

import edu.wpi.first.wpilibj.DigitalInput;

/**
 * 
 * A class that implements the Andy Mark Hall Effect Sensor, a sensor that is
 * triggered when a magnetic field is near.
 *
 */
public class AndyMarkHallEffect implements IHallEffectSensor
{
	private DigitalInput input;

	public AndyMarkHallEffect(int channel)
	{
		input = new DigitalInput(channel);
	}

	@Override
	public boolean isTriggered()
	{
		return input.get();
	}

}
