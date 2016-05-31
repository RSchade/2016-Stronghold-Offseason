package org.fpsrobotics.actuators.drivetrain;

import org.fpsrobotics.actuators.DoubleMotor;
import org.fpsrobotics.sensors.IAccelerometer;
import org.fpsrobotics.sensors.IEncoder;
import org.fpsrobotics.sensors.IGyroscope;
import org.fpsrobotics.sensors.SensorConfig;

public class MullenatorDrive implements ITankDrive, IDriveSmooth
{
	private DoubleMotor leftDrive, rightDrive;
	private IGyroscope gyro;
	private IEncoder leftFeedbackDevice, rightFeedbackDevice;
	private IAccelerometer accel;
	private boolean gyroExists = false;
	private boolean accelerometerExists = false;

	public MullenatorDrive(DoubleMotor leftDrive, DoubleMotor rightDrive)
	{
		this.leftDrive = leftDrive;
		this.rightDrive = rightDrive;
		this.leftFeedbackDevice = (IEncoder) leftDrive.getPIDFeedbackDevice();
		this.rightFeedbackDevice = (IEncoder) rightDrive.getPIDFeedbackDevice();
		leftFeedbackDevice.reverseSensor(true);
		rightFeedbackDevice.reverseSensor(false);
		this.gyro = null;
		this.gyroExists = false;
		this.accel = null;
		this.accelerometerExists = false;
	}

	public MullenatorDrive(DoubleMotor leftDrive, DoubleMotor rightDrive, IGyroscope gyro)
	{
		this.leftDrive = leftDrive;
		this.rightDrive = rightDrive;
		this.leftFeedbackDevice = (IEncoder) leftDrive.getPIDFeedbackDevice();
		this.rightFeedbackDevice = (IEncoder) rightDrive.getPIDFeedbackDevice();
		leftFeedbackDevice.reverseSensor(true);
		rightFeedbackDevice.reverseSensor(false);
		this.gyroExists = true;
		this.gyro = gyro;
		this.accel = null;
		this.accelerometerExists = false;
	}

	public MullenatorDrive(DoubleMotor leftDrive, DoubleMotor rightDrive, IGyroscope gyro, IAccelerometer accel)
	{
		this.leftDrive = leftDrive;
		this.rightDrive = rightDrive;
		this.leftFeedbackDevice = (IEncoder) leftDrive.getPIDFeedbackDevice();
		this.rightFeedbackDevice = (IEncoder) rightDrive.getPIDFeedbackDevice();
		leftFeedbackDevice.reverseSensor(true);
		rightFeedbackDevice.reverseSensor(false);
		this.accel = accel;
		this.gyro = gyro;
		this.gyroExists = true;
		this.accelerometerExists = true;
	}

	double KpTeleop = 0.003;

	@Override
	public synchronized void driveForward(double speed)
	{
		if (speed < 0)
		{
			speed = -speed;
		}

		if (gyroExists)
		{
			// Figure out why this works, and in what configuration of
			// positive/negative signs
			driveUsingGyro(-speed, -gyro.getHardCount() * KpTeleop);
		} else
		{
			drive(-speed, -speed);
		}
	}

	@Override
	public synchronized void driveBackward(double speed)
	{
		if (speed < 0)
		{
			speed = -speed;
		}

		if (gyroExists)
		{
			driveUsingGyro(speed, gyro.getHardCount() * KpTeleop);
		} else
		{
			drive(speed, speed);
		}
	}

	private final double m_sensitivity = 0.5;

	private void driveUsingGyro(double outputMagnitude, double curve)
	{
		double leftOutput, rightOutput;

		if (curve < 0)
		{
			double value = Math.log(-curve);
			double ratio = (value - m_sensitivity) / (value + m_sensitivity);
			if (ratio == 0)
			{
				ratio = .0000000001;
			}
			leftOutput = outputMagnitude / ratio;
			rightOutput = outputMagnitude;
		} else if (curve > 0)
		{
			double value = Math.log(curve);
			double ratio = (value - m_sensitivity) / (value + m_sensitivity);
			if (ratio == 0)
			{
				ratio = .0000000001;
			}
			leftOutput = outputMagnitude;
			rightOutput = outputMagnitude / ratio;
		} else
		{
			leftOutput = outputMagnitude;
			rightOutput = outputMagnitude;
		}

		drive(leftOutput, rightOutput);
	}

	private double rampDownAngleDifference = 5;
	private double slowSpeedTurning = 0.2; // Units need to be created

	@Override
	public synchronized void turnLeft(double speed, double angle)
	{
		if (speed < 0)
		{
			speed = -speed;
		}

		if (gyroExists)
		{
			gyro.softResetCount();

			drive(-speed, speed);

			while (!(gyro.getSoftCount() >= (angle + rampDownAngleDifference)))
				;

			drive(-slowSpeedTurning, slowSpeedTurning);

			while (!(gyro.getSoftCount() >= (angle + 0.5)))
				;

			stop();

		} else
		{
			System.out.println("timebased not implemented");
		}
	}

	@Override
	public synchronized void turnRight(double speed, double angle)
	{
		if (speed < 0)
		{
			speed = -speed;
		}

		if (gyroExists)
		{
			gyro.softResetCount();

			drive(speed, -speed);

			while (!(gyro.getSoftCount() >= (angle - rampDownAngleDifference)))
				;

			drive(slowSpeedTurning, -slowSpeedTurning);

			while (!(gyro.getSoftCount() >= (angle - 0.5)))
				;

			stop();

		} else
		{
			System.out.println("timebased not implemented");
		}
	}

	@Override
	public synchronized void stop()
	{
		// leftDrive.stop();
		// rightDrive.stop();

		stopAtProfile(DriveTrainProfile.VELOCITY);
	}

	private DriveTrainProfile masterProfile = DriveTrainProfile.VELOCITY;

	private double slowDistance = 5;
	private double slowSpeedDriving = 0.2; // get units correct for velocity
	private int steps = 7;

	@Override
	public synchronized void driveAtProfile(double[] profileSetting, DriveTrainProfile profile)
	{
		switch (profile)
		{
		case POSITION: // position response

			// if we are going at .5 or below, 10 inches stopping distance
			// Encoders slipping? Readings are not as accurate as they should be.

			stop();

			try
			{
				Thread.sleep(5);
			} catch (InterruptedException e1)
			{

			}

			leftFeedbackDevice.resetCount();
			rightFeedbackDevice.resetCount();

			SensorConfig.getInstance().getGyro().hardResetCount();

			try
			{
				Thread.sleep(5);
			} catch (InterruptedException e1)
			{

			}

			slowDistance = profileSetting[0] / 2;

			if (profileSetting[1] > 0 && profileSetting[0] > 0)
			{
				while (((leftFeedbackDevice.getDistance() < (profileSetting[0] - slowDistance))
						|| (rightFeedbackDevice.getDistance() < (profileSetting[0] - slowDistance))))
				{
					driveForward(profileSetting[1]);
				}

				System.out.println("stopped " + leftFeedbackDevice.getDistance());

				while (((leftFeedbackDevice.getDistance() < (profileSetting[0] - 2))
						|| (rightFeedbackDevice.getDistance() < (profileSetting[0] - 2))))
					;
				{
					driveForward(slowSpeedDriving);
				}

				stop();

				System.out.println(
						"reached end " + leftFeedbackDevice.getDistance() + " " + leftFeedbackDevice.getCount());

			} else
			{
				while (((leftFeedbackDevice.getDistance() > profileSetting[0] + slowDistance)
						|| (rightFeedbackDevice.getDistance() > profileSetting[0] + slowDistance)))
				{
					driveBackward(profileSetting[1]);
				}

				while (((leftFeedbackDevice.getDistance() > profileSetting[0] + 10)
						|| (rightFeedbackDevice.getDistance() > profileSetting[0] + 10)))
					;
				{
					driveBackward(slowSpeedDriving);
				}

				stop();
			}

			break;

		case VELOCITY: // trapezoidal velocity response

			if (leftDrive.getSpeed() != profileSetting[0] || rightDrive.getSpeed() != profileSetting[1])
			{
				double leftSpeed, rightSpeed, initialLeft, initialRight = 0;

				leftSpeed = leftDrive.getSpeed();
				initialLeft = leftSpeed;

				rightSpeed = rightDrive.getSpeed();
				initialRight = rightSpeed;

				for (int i = 0; i < steps; i++)
				{
					leftSpeed += (profileSetting[0] - initialLeft) / steps;
					rightSpeed += (profileSetting[1] - initialRight) / steps;

					leftDrive.setSpeed(leftSpeed);
					rightDrive.setSpeed(rightSpeed);

					try
					{
						Thread.sleep(3); // wait at least 3 milliseconds
					} catch (InterruptedException e)
					{
						leftDrive.stop();
						rightDrive.stop();
					}
				}
			} else
			{
				leftDrive.setSpeed(profileSetting[0]);
				rightDrive.setSpeed(profileSetting[1]);
			}

			break;

		case ACCELERATION: // trapezoidal acceleration response
			/*
			if (leftDrive.getSpeed() != profileSetting[0] || rightDrive.getSpeed() != profileSetting[1])
			{
				double leftAcceleration, rightAcceleration, initialLeft, initialRight = 0;

				leftAcceleration = leftFeedbackDevice.getAcceleration();
				initialLeft = leftAcceleration;

				rightAcceleration = rightFeedbackDevice.getAcceleration();
				initialRight = rightAcceleration;

				for (int i = 0; i < steps; i++)
				{
					leftAcceleration += (profileSetting[0] - initialLeft) / steps;
					rightAcceleration += (profileSetting[1] - initialRight) / steps;

					leftDrive.setSpeed(leftAcceleration);
					rightDrive.setSpeed(rightAcceleration);

					try
					{
						Thread.sleep(3); // wait at least 3 milliseconds
					} catch (InterruptedException e)
					{
						leftDrive.stop();
						rightDrive.stop();
					}
				}
			} else
			{
				leftDrive.setSpeed(profileSetting[0]);
				rightDrive.setSpeed(profileSetting[1]);
			}
			*/
			break;

		case JERK:
			System.err.println("Jerk mode not implemented");
			break;

		default:
			System.err.println("Drive profile not implemented");
			break;
		}
	}

	@Override
	public void stopAtProfile(DriveTrainProfile profile)
	{
		switch (profile)
		{
		case POSITION:
			break;

		case VELOCITY:

			double leftSpeed, rightSpeed, initialLeft, initialRight = 0;

			leftSpeed = leftDrive.getSpeed();
			initialLeft = leftSpeed;

			rightSpeed = rightDrive.getSpeed();
			initialRight = rightSpeed;

			for (int i = 0; i < steps; i++)
			{
				leftSpeed -= initialLeft / steps;
				rightSpeed -= initialRight / steps;

				leftDrive.setSpeed(leftSpeed);
				rightDrive.setSpeed(rightSpeed);

				try
				{
					Thread.sleep(3); // wait at least 3 milliseconds
				} catch (InterruptedException e)
				{

				}
			}

			break;

		case ACCELERATION:
			System.err.println("Acceleration mode not implemented");
			break;

		case JERK:
			System.err.println("Jerk mode not implemented");
			break;

		default:
			System.err.println("Drive profile not implemented");
			break;
		}
	}

	@Override
	public synchronized void setProfile(DriveTrainProfile profile)
	{
		masterProfile = profile;
	}

	@Override
	public synchronized void driveAtSpecifiedProfile(double[] profileSetting)
	{
		driveAtProfile(profileSetting, masterProfile);
	}

	@Override
	public synchronized void drive(double leftSpeed, double rightSpeed)
	{
		// leftDrive.setSpeed(leftSpeed); // implement auto straighten
		// rightDrive.setSpeed(rightSpeed);

		driveAtProfile(new double[] { leftSpeed, rightSpeed }, DriveTrainProfile.VELOCITY);
	}

	@Override
	public DriveTrainProfile getDriveProfile()
	{
		return masterProfile;
	}
}
