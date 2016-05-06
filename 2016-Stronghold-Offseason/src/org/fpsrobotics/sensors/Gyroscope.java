package org.fpsrobotics.sensors;

import edu.wpi.first.wpilibj.AnalogGyro;

/**
 * Implements a simple analog gyroscope.
 */
public class Gyroscope implements IGyroscope
{
	private AnalogGyro gyro;
	private boolean isEnabled;

	private double pastAngle = 0;
	private double currentAngle = 0;

	public Gyroscope(AnalogGyro gyro)
	{
		this.gyro = gyro;

		gyro.initGyro();

		isEnabled = true;
	}

	@Override
	/**
	 * Returns values in between 0 and 0
	 */
	public double getHardCount()
	{
		if (isEnabled)
		{
			return gyro.getAngle();
		} else
		{
			return 0;
		}
	}

	@Override
	public double getSoftCount()
	{
		currentAngle = pastAngle + gyro.getAngle();
		if (currentAngle > 180)
		{
			currentAngle -= 360;
		}
		if (currentAngle < -180)
		{
			currentAngle += 360;
		}
		return currentAngle;
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
	public void softResetCount()
	{
		pastAngle += gyro.getAngle();
		gyro.reset();
	}
	
	@Override
	public void hardResetCount()
	{
//		pastAngle = gyro.getAngle();
		pastAngle = 0;
		currentAngle = 0;
		gyro.reset();
	}

	public double getAttitude()
	{
		return 0.0;
	}

	@Override
	public double getPitch()
	{
		return 0;
	}

	@Override
	public double getRate()
	{
		return 0;
	}

	@Override
	public double getPitchRate()
	{
		return 0;
	}

}
