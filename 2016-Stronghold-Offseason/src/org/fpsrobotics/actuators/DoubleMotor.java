package org.fpsrobotics.actuators;

import org.fpsrobotics.PID.IPIDFeedbackDevice;

import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;

/**
 * This class combines the functionality of two motors into one.
 */
public class DoubleMotor implements ICANMotor
{
	private Motor motorOne, motorTwo;
	private CANMotor CANMotorOne, CANMotorTwo;

	/**
	 * Used for motors that are not connected to Talon SRX motor controllers
	 * 
	 * @param motorOne
	 * @param motorTwo
	 */
	public DoubleMotor(Motor motorOne, Motor motorTwo)
	{
		this.motorOne = motorOne;
		this.motorTwo = motorTwo;
	}

	/**
	 * Used for motors that are connected to Talon SRX motor controllers
	 * 
	 * @param motorOne
	 * @param motorTwo
	 */
	public DoubleMotor(CANMotor motorOne, CANMotor motorTwo)
	{
		this.CANMotorOne = motorOne;
		this.CANMotorTwo = motorTwo;

		// Follower mode makes one CANTalon go the same speed as another
		// CANTalon without issuing them both separate commands.
		motorTwo.setControlMode(TalonControlMode.Follower);
		motorTwo.setFollowerDeviceID(motorOne.getDeviceID());
	}

	@Override
	public double getSpeed()
	{
		if (CANMotorOne != null)
		{
			return CANMotorOne.getSpeed();
		} else
		{
			return ((motorOne.getSpeed() + motorTwo.getSpeed()) / (2));
		}

	}

	@Override
	public void setSpeed(double speed)
	{
		if (CANMotorOne != null)
		{
			CANMotorOne.setSpeed(speed);
		} else
		{
			motorOne.setSpeed(speed);
			motorTwo.setSpeed(speed);
		}
	}

	@Override
	public void stop()
	{
		if (CANMotorOne != null)
		{
			CANMotorOne.stop();
		} else
		{
			motorOne.stop();
			motorTwo.stop();
		}
	}

	public void setP(double p)
	{
		if (CANMotorOne != null)
		{
			CANMotorOne.setP(p);
		} else
		{
			System.out.println("CANNOT SET PID OF A NON-CAN MOTOR");
		}
	}

	public void setI(double i)
	{
		if (CANMotorOne != null)
		{
			CANMotorOne.setI(i);
		} else
		{
			System.out.println("CANNOT SET PID OF A NON-CAN MOTOR");
		}
	}

	public void setD(double d)
	{
		if (CANMotorOne != null)
		{
			CANMotorOne.setD(d);
		} else
		{
			System.out.println("CANNOT SET PID OF A NON-CAN MOTOR");
		}
	}

	public void setPIDFeedbackDevice(IPIDFeedbackDevice device)
	{
		if (CANMotorOne != null)
		{
			CANMotorOne.setPIDFeedbackDevice(device);
		} else
		{
			System.out.println("CANNOT SET PID OF A NON-CAN MOTOR");
		}
	}

	public IPIDFeedbackDevice getPIDFeedbackDevice()
	{
		if (CANMotorOne != null)
		{
			return CANMotorOne.getPIDFeedbackDevice();
		} else
		{
			System.out.println("CANNOT SET PID OF A NON-CAN MOTOR");
			return null;
		}

	}

	public void enablePID()
	{
		if (CANMotorOne != null)
		{
			// only enable one because we are in follower mode.
			CANMotorOne.enablePID();
		} else
		{
			System.out.println("CANNOT SET PID OF A NON-CAN MOTOR");
		}
	}

	public void disablePID()
	{
		if (CANMotorOne != null)
		{
			CANMotorOne.disablePID();
		} else
		{
			System.out.println("CANNOT SET PID OF A NON-CAN MOTOR");
		}
	}

	public TalonControlMode getControlMode()
	{
		if (CANMotorOne != null)
		{
			return CANMotorOne.getControlMode();
		} else
		{
			System.out.println("CANNOT SET PID OF A NON-CAN MOTOR");
			return null;
		}
	}

	public void setControlMode(TalonControlMode mode)
	{
		if (CANMotorOne != null)
		{
			CANMotorOne.setControlMode(mode);
		} else
		{
			System.out.println("CANNOT SET PID OF A NON-CAN MOTOR");
		}
	}

	public CANMotor getCANMotorOne()
	{
		return CANMotorOne;
	}

	public CANMotor getCANMotorTwo()
	{
		return CANMotorTwo;
	}

	int steps = 5;
	
	@Override
	public void rampTo(double speed)
	{
		if (getSpeed() != speed)
		{
			double rampSpeed, initialSpeed = 0;

			rampSpeed = getSpeed();
			initialSpeed = rampSpeed;

			for (int i = 0; i < steps; i++)
			{
				rampSpeed += (speed - initialSpeed) / steps;

				setSpeed(speed);

				try
				{
					Thread.sleep(2); // wait at least 2 milliseconds
				} catch (InterruptedException e)
				{
					stop();
					return;
				}
			}
		} else
		{
			setSpeed(speed);
		}
	}

	@Override
	public void rampDown()
	{
		double rampSpeed, initialSpeed = 0;

		rampSpeed = getSpeed();
		initialSpeed = rampSpeed;

		for (int i = 0; i < 100; i++)
		{
			rampSpeed -= initialSpeed / 100;

			setSpeed(rampSpeed);

			try
			{
				Thread.sleep(2); // wait at least 2 milliseconds
			} catch (InterruptedException e)
			{

			}
		}
	}

	@Override
	public void rampTo(double speed, int steps)
	{
		if (getSpeed() != speed)
		{
			double rampSpeed, initialSpeed = 0;

			rampSpeed = getSpeed();
			initialSpeed = rampSpeed;

			for (int i = 0; i < steps; i++)
			{
				rampSpeed += (speed - initialSpeed) / steps;

				setSpeed(speed);

				try
				{
					Thread.sleep(2); // wait at least 2 milliseconds
				} catch (InterruptedException e)
				{
					stop();
					return;
				}
			}
		} else
		{
			setSpeed(speed);
		}
	}

}
