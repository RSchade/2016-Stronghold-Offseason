package org.fpsrobotics.sensors;

/**
 * Defines the Power Distribution Panel and it's sensors onboard. You can
 * measure the voltage and temperature for the whole system. Along with Current
 * and power on each channel.
 *
 */
public class PowerDistributionPanel implements IPowerBoard
{
	private edu.wpi.first.wpilibj.PowerDistributionPanel pdp;

	public PowerDistributionPanel()
	{
		pdp = new edu.wpi.first.wpilibj.PowerDistributionPanel();
	}

	@Override
	public double getVoltage()
	{
		return pdp.getVoltage();
	}

	@Override
	public double getCurrent(int channel)
	{
		return pdp.getCurrent(channel);
	}

	@Override
	public double getPower(int channel)
	{
		return pdp.getCurrent(channel) * pdp.getVoltage();
	}

	@Override
	public double getTemperature()
	{
		return pdp.getTemperature();
	}

}
