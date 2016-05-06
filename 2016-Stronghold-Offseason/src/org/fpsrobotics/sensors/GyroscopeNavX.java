package org.fpsrobotics.sensors;

import com.kauailabs.navx.frc.AHRS;

/**
 * Implements the gyroscope included on the NavX MXP board.
 *
 */
public class GyroscopeNavX implements IGyroscope
{
	private AHRS ahrs;
	private boolean isEnabled = true;
	
	private double pastYaw = 0;
	private double currentYaw = 0;

	public GyroscopeNavX(AHRS ahrs)
	{
		this.ahrs = ahrs;

		ahrs.reset();
	}

	@Override
	/**
	 * Returns values in between -180 and 180
	 * Use hard count for finding the value based on the last reset
	 */
	public double getHardCount()
	{
		if (isEnabled)
		{
			return ahrs.getYaw();
		} else
		{
			return 0;
		}
	}
	
	@Override
	/**
	 * Use Soft Count for finding the value based on the initial position of the robot
	 */
	public double getSoftCount()
	{
		currentYaw = pastYaw + ahrs.getYaw();
		if (currentYaw > 180)
		{
			currentYaw -= 360;
		}
		if (currentYaw < -180)
		{
			currentYaw += 360;
		}
		return currentYaw;
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
	/**
	 * Use soft reset for making the gyro reset without changing the initial position of the robot 
	 */
//	public void softResetCount()
//	{
//		pastYaw += ahrs.getYaw();
//		ahrs.reset();
//	}
//	
//	@Override
//	/**
//	 * Use hard reset for making the gyro reset AND change the initial position of the robot
//	 * (Soft Count becomes the same as Hard Count = 0)
//	 */
//	public void hardResetCount()
//	{
//		pastYaw = ahrs.getYaw();
//		ahrs.reset();
//	}
	public void softResetCount()
	{
		pastYaw += ahrs.getYaw();
		ahrs.reset();
	}
	
	@Override
	/**
	 * Use hard reset for making the gyro reset AND change the initial position of the robot
	 * (Soft Count becomes the same as Hard Count = 0)
	 */
	public void hardResetCount()
	{
		pastYaw = 0;
		currentYaw = 0;
		ahrs.reset();
	}

	@Override
	public double getPitch()
	{
		return ahrs.getRoll();
	}

	@Override
	public double getRate()
	{
		return ahrs.getRate();
	}
	
	public double getPitchRate()
	{
		return ahrs.getRawGyroY();
	}
}
