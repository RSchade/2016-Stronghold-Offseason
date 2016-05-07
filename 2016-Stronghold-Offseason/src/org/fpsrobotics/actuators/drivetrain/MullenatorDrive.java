package org.fpsrobotics.actuators.drivetrain;

import org.fpsrobotics.PID.IPIDFeedbackDevice;
import org.fpsrobotics.actuators.DoubleMotor;
import org.fpsrobotics.sensors.IAccelerometer;
import org.fpsrobotics.sensors.IGyroscope;

public class MullenatorDrive implements ITankDrive, IDriveSmooth
{
	private DoubleMotor leftDrive, rightDrive;
	private IGyroscope gyro;
	private IPIDFeedbackDevice leftFeedbackDevice, rightFeedbackDevice;
	private IAccelerometer accel;
	private boolean gyroExists = false;
	private boolean accelerometerExists = false;

	public MullenatorDrive(DoubleMotor leftDrive, DoubleMotor rightDrive)
	{
		this.leftDrive = leftDrive;
		this.rightDrive = rightDrive;
		this.leftFeedbackDevice = leftDrive.getPIDFeedbackDevice();
		this.rightFeedbackDevice = rightDrive.getPIDFeedbackDevice();
		this.gyro = null;
		this.gyroExists = false;
		this.accel = null;
		this.accelerometerExists = false;
	}

	public MullenatorDrive(DoubleMotor leftDrive, DoubleMotor rightDrive, IGyroscope gyro)
	{
		this.leftDrive = leftDrive;
		this.rightDrive = rightDrive;
		this.leftFeedbackDevice = leftDrive.getPIDFeedbackDevice();
		this.rightFeedbackDevice = rightDrive.getPIDFeedbackDevice();
		this.gyroExists = true;
		this.accel = null;
		this.accelerometerExists = false;
	}

	public MullenatorDrive(DoubleMotor leftDrive, DoubleMotor rightDrive, IGyroscope gyro, IAccelerometer accel)
	{
		this.leftDrive = leftDrive;
		this.rightDrive = rightDrive;
		this.leftFeedbackDevice = leftDrive.getPIDFeedbackDevice();
		this.rightFeedbackDevice = rightDrive.getPIDFeedbackDevice();
		this.accel = accel;
		this.gyroExists = true;
		this.accelerometerExists = true;
	}

	double KpTeleop = 0.003;

	@Override
	public void driveForward(double speed)
	{
		if (speed < 0)
		{
			System.err.println("Negative Speed on Drive Forward");
			speed = -speed;
		}

		if (gyroExists)
		{
			// Figure out why this works, and in what configuration of
			// positive/negative signs
			driveUsingGyro(speed, gyro.getHardCount() * KpTeleop);
		} else
		{
			// drive(speed, speed);
			stop(); // testing
		}
	}

	@Override
	public void driveBackward(double speed)
	{
		if (speed < 0)
		{
			System.err.println("Negative Speed on Drive Backward");
			speed = -speed;
		}

		if (gyroExists)
		{
			// Figure out why this works, and in what configuration of
			// positive/negative signs
			driveUsingGyro(-speed, -gyro.getHardCount() * KpTeleop);
		} else
		{
			// drive(-speed, -speed);
			stop(); // testing
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

		driveAtSpecifiedProfile(new double[] { leftOutput, rightOutput });
	}

	private double rampDownAngleDifference = 5;
	private double slowSpeedTurning = 0.2; // Units need to be created

	@Override
	public void turnLeft(double speed, double angle)
	{
		if (speed < 0)
		{
			speed = -speed;
		}

		if (gyroExists)
		{
			gyro.softResetCount();

			drive(-speed, speed);

			while (!(gyro.getSoftCount() >= (angle + rampDownAngleDifference)));

			drive(-slowSpeedTurning, slowSpeedTurning);

			while (!(gyro.getSoftCount() >= (angle + 0.5)));

			stop();

		} else
		{
			System.out.println("timebased not implemented");
		}
	}

	@Override
	public void turnRight(double speed, double angle)
	{
		if (speed < 0)
		{
			speed = -speed;
		}

		if (gyroExists)
		{
			gyro.softResetCount();

			drive(speed, -speed);

			while (!(gyro.getSoftCount() >= (angle - rampDownAngleDifference)));

			drive(slowSpeedTurning, -slowSpeedTurning);

			while (!(gyro.getSoftCount() >= (angle - 0.5)));

			stop();

		} else
		{
			System.out.println("timebased not implemented");
		}
	}

	@Override
	public void stop()
	{
		// leftDrive.stop();
		// rightDrive.stop();

		stopAtProfile(DriveTrainProfile.VELOCITY);
	}

	private DriveTrainProfile masterProfile = DriveTrainProfile.VELOCITY;

	private double slowDistance = 200;
	private double slowSpeedDriving = 0.2; // get units correct for velocity
	private int steps = 5;

	@Override
	public void driveAtProfile(double[] profileSetting, DriveTrainProfile profile)
	{
		switch (profile)
		{
		case POSITION: // position response

			leftFeedbackDevice.resetCount();
			rightFeedbackDevice.resetCount();

			if (profileSetting[1] > 0 && profileSetting[0] > 0)
			{
				while (((leftFeedbackDevice.getCount() < profileSetting[0] - slowDistance)
						|| (rightFeedbackDevice.getCount() < profileSetting[0] - slowDistance)))
				{
					driveForward(profileSetting[1]);
				}

				while (((leftFeedbackDevice.getCount() < profileSetting[0] - 10)
						|| (rightFeedbackDevice.getCount() < profileSetting[0] - 10)));
				{
					driveForward(slowSpeedDriving);
				}

				stop();

			} else
			{
				while (((leftFeedbackDevice.getCount() > profileSetting[0] + slowDistance)
						|| (rightFeedbackDevice.getCount() > profileSetting[0] + slowDistance)))
				{
					driveBackward(profileSetting[1]);
				}

				while (((leftFeedbackDevice.getCount() > profileSetting[0] + 10)
						|| (rightFeedbackDevice.getCount() > profileSetting[0] + 10)));
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

			if (accelerometerExists)
			{
				System.err.println("Acceleration mode not implemented");
			} else
			{
				System.err.println("derivative acceleration using encoder not implemented");
			}

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
	public void setProfile(DriveTrainProfile profile)
	{
		masterProfile = profile;
	}

	@Override
	public void driveAtSpecifiedProfile(double[] profileSetting)
	{
		driveAtProfile(profileSetting, masterProfile);
	}

	@Override
	public void drive(double leftSpeed, double rightSpeed)
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
