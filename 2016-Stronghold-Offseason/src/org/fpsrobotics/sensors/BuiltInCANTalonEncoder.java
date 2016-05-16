package org.fpsrobotics.sensors;

import org.fpsrobotics.PID.IPIDFeedbackDevice;
import org.fpsrobotics.robot.RobotStatus;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;

/**
 * A class that implements the encoder connected to directly to the CAN Talon motor controller.
 */
public class BuiltInCANTalonEncoder implements IPIDFeedbackDevice
{
	private CANTalon canMotor;
	private boolean flip = false;
	
	public BuiltInCANTalonEncoder(CANTalon canMotor)
	{
		this.canMotor = canMotor;
		canMotor.configEncoderCodesPerRev(2048);
	}

	@Override
	public double getCount()
	{
		try
		{
			if(flip)
			{
				return (canMotor.getEncPosition() * -1);
			} else
			{
				return canMotor.getEncPosition();
			}
		} catch (Exception e)
		{
			System.out.println("Encoder not mounted somewhere, proceed with caution");
			return 0;
		}
	}

	@Override
	public void enable()
	{
		canMotor.enableControl();
	}

	@Override
	public void disable()
	{
		canMotor.disableControl();
	}

	@Override
	public void resetCount()
	{
		canMotor.setPosition(0);
	}

	@Override
	public FeedbackDevice whatPIDDevice()
	{
		return CANTalon.FeedbackDevice.QuadEncoder;
	}

	@Override
	public double getError()
	{
		try
		{
			return canMotor.getClosedLoopError(); // does this need flip?
		} catch (Exception e)
		{
			System.out.println("Encoder not mounted somewhere, proceed with caution");
			return 0;
		}
	}

	@Override
	public double getRate()
	{
		try
		{
			if(flip)
			{
				return (canMotor.getEncVelocity() * -1);
			} else
			{
				return canMotor.getEncVelocity();
			}
			
		} catch (Exception e)
		{
			System.out.println("Encoder not mounted somewhere, proceed with caution");
			return 0;
		}
	}

	@Override
	public double getDistance() // TODO: This is where we Tune Encoder for Alpha
	{
		if (RobotStatus.isAlpha())
		{
			//return (getCount() / 785.79);
			return (getCount() * 0.0018703241895262);
		} else
		{
			return (getCount() / 785.79);
		}
	}

	@Override
	public void reverseSensor(boolean reversed)
	{
		//canMotor.reverseSensor(reversed);
		
		if(reversed)
		{
			flip = true;
		} else
		{
			flip = false;
		}
	}

}
