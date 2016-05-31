package org.fpsrobotics.actuators.manipulator;

import org.fpsrobotics.actuators.ICANMotor;
import org.fpsrobotics.actuators.ISolenoid;
import org.fpsrobotics.robot.RobotStatus;
import org.fpsrobotics.sensors.SensorConfig;

public class MullenatorShooter implements IMullenatorShooter
{
	private ICANMotor leftShooterMotor;
	private ICANMotor rightShooterMotor;
	private ILinearActuator dart;
	private ISolenoid shooterPiston;

	private final double SHOOT_SPEED_HIGH = 1.0;
	private final double SHOOT_SPEED_LOW = 0.5;

	private final double INTAKE_SHOOTER_SPEED = 0.7;

	private final double SHOOTER_RAISE_SPEED = 0.5;
	private final double SHOOTER_LOWER_SPEED = 0.5;

	protected final int STANDARD_DEFENSE_SHOOTER, SHOOTER_POSITION_AT_POSITION_TWO_AUTO,
			SHOOTER_POSITION_AT_POSITION_THREE_AUTO, SHOOTER_POSITION_AT_POSITION_FOUR_AUTO,
			SHOOTER_POSITION_AT_POSITION_FIVE_AUTO, SHOOTER_POSITION_CENTER_TELEOP, SHOOTER_POSITION_CORNER_TELEOP,
			SHOOTER_POSITION_LOW_BAR_SHOOT_HIGH_AUTO, BOTTOM_LIMIT_SHOOTER, TOP_LIMIT_SHOOTER, LOW_BAR_SHOOTER;

	public MullenatorShooter(ICANMotor leftShooterMotor, ICANMotor rightShooterMotor, ILinearActuator dart,
			ISolenoid shooterPiston, boolean isAlpha)
	{
		if (isAlpha)
		{
			this.leftShooterMotor = leftShooterMotor;
			this.rightShooterMotor = rightShooterMotor;
			this.shooterPiston = shooterPiston;
			this.dart = dart;

			// To Shoot
			SHOOTER_POSITION_AT_POSITION_TWO_AUTO = 800;
			SHOOTER_POSITION_AT_POSITION_THREE_AUTO = 600;
			SHOOTER_POSITION_AT_POSITION_FOUR_AUTO = 600;
			SHOOTER_POSITION_AT_POSITION_FIVE_AUTO = 758;
			SHOOTER_POSITION_CENTER_TELEOP = 600;
			SHOOTER_POSITION_CORNER_TELEOP = 635;
			SHOOTER_POSITION_LOW_BAR_SHOOT_HIGH_AUTO = 765;

			// Shooter
			BOTTOM_LIMIT_SHOOTER = 2100;
			TOP_LIMIT_SHOOTER = 588;
			
			LOW_BAR_SHOOTER = 1800;
			STANDARD_DEFENSE_SHOOTER = 800;
		} else
		{
			// Shooter
			BOTTOM_LIMIT_SHOOTER = 1200;
			TOP_LIMIT_SHOOTER = 250;
		
			LOW_BAR_SHOOTER = 1130; // must be just below lowest collision point
			// Presets
			STANDARD_DEFENSE_SHOOTER = 800;
			
			// To Shoot
			SHOOTER_POSITION_AT_POSITION_TWO_AUTO = 300;
			SHOOTER_POSITION_AT_POSITION_THREE_AUTO = 200;
			SHOOTER_POSITION_AT_POSITION_FOUR_AUTO = 300;
			SHOOTER_POSITION_AT_POSITION_FIVE_AUTO = 258;
			SHOOTER_POSITION_CENTER_TELEOP = 250;
			SHOOTER_POSITION_CORNER_TELEOP = 250;
			SHOOTER_POSITION_LOW_BAR_SHOOT_HIGH_AUTO = 300;
		}
	}

	@Override
	public void moveToTopLimit()
	{
		dart.goToTopLimit(SHOOTER_RAISE_SPEED);
	}

	@Override
	public void moveToBottomLimit()
	{
		dart.goToBottomLimit(SHOOTER_LOWER_SPEED);
	}

	@Override
	public void moveToPreset(EShooterPresets preset)
	{
		switch (preset)
		{
		case INTAKE: // When Intaking the ball
			moveToBottomLimit();
			break;
		case LOW_BAR: // Going under the low bar
			moveToValue(LOW_BAR_SHOOTER);
			break;
		case STANDARD_DEFENSE_SHOOTER: // Going over a standard defense
			moveToValue(STANDARD_DEFENSE_SHOOTER);
			break;
		case SHOOTER_POSITION_AT_DEFENSE_TWO_AUTO: // AutoShot Auto
			moveToValue(SHOOTER_POSITION_AT_POSITION_TWO_AUTO);
			break;
		case SHOOTER_POSITION_AT_DEFENSE_THREE_AUTO: // AutoShot Auto
			moveToValue(SHOOTER_POSITION_AT_POSITION_THREE_AUTO);
			break;
		case SHOOTER_POSITION_AT_DEFENSE_FOUR_AUTO: // AutoShot Auto
			moveToValue(SHOOTER_POSITION_AT_POSITION_FOUR_AUTO);
			break;
		case SHOOTER_POSITION_AT_DEFENSE_FIVE_AUTO: // AutoShot Auto
			moveToValue(SHOOTER_POSITION_AT_POSITION_FIVE_AUTO);
			break;
		case CENTER_SHOT_TELEOP: // Center Shooting Teleop
			moveToValue(SHOOTER_POSITION_CENTER_TELEOP);
			break;
		case CORNER_SHOT_TELEOP: // Corner Shooting Teleop
			moveToValue(SHOOTER_POSITION_CORNER_TELEOP);
			break;
		case LOW_BAR_SHOOT_HIGH_AUTO: // Low bar and shoot high auto
			moveToValue(SHOOTER_POSITION_LOW_BAR_SHOOT_HIGH_AUTO);
			break;
		default:
			// Do Nothing
			System.err.println("Shooter Defaulted: Doing Nothing, Probably Going Haywire");
		}
	}

	@Override
	public void moveToValue(int value)
	{
		if (dart.getIsUpAPositiveSlope())
		{
			if (dart.getFeedbackCount() > value)
			{
				dart.goToPosition(SHOOTER_LOWER_SPEED, value);
			} else
			{
				dart.goToPosition(SHOOTER_RAISE_SPEED, value);
			}
		} else
		{
			if (dart.getFeedbackCount() < value)
			{
				dart.goToPosition(SHOOTER_LOWER_SPEED, value);
			} else
			{
				dart.goToPosition(SHOOTER_RAISE_SPEED, value);
			}
		}
	}

	@Override
	public void intake()
	{
		rightShooterMotor.setSpeed(-Math.abs(INTAKE_SHOOTER_SPEED));
		leftShooterMotor.setSpeed(-Math.abs(INTAKE_SHOOTER_SPEED));

		if (Thread.interrupted())
		{
			rightShooterMotor.stop();
			leftShooterMotor.stop();
			return;
		}

		moveToPreset(EShooterPresets.INTAKE);
	}

	@Override
	public void stopWheels()
	{
		rightShooterMotor.stop();
		leftShooterMotor.stop();
	}

	@Override
	public void shootHigh()
	{
		rightShooterMotor.setSpeed(SHOOT_SPEED_HIGH);
		leftShooterMotor.setSpeed(SHOOT_SPEED_HIGH);

		try
		{
			Thread.sleep(500);
		} catch (InterruptedException e)
		{
			rightShooterMotor.stop();
			leftShooterMotor.stop();
			return;
		}

		launch();
	}

	@Override
	public void shootLow()
	{
		rightShooterMotor.setSpeed(SHOOT_SPEED_LOW);
		leftShooterMotor.setSpeed(SHOOT_SPEED_LOW);

		try
		{
			Thread.sleep(1000);
		} catch (InterruptedException e)
		{
			rightShooterMotor.stop();
			leftShooterMotor.stop();
			return;
		}

		launch();
	}

	private void launch()
	{
		shooterPiston.engage();
		try
		{
			Thread.sleep(1000);
		} catch (InterruptedException e)
		{

		}

		stopWheels();
		shooterPiston.disengage();
	}

	@Override
	public int getPosition()
	{
		return (int) dart.getFeedbackCount();
	}

	@Override
	public void goDown()
	{
		dart.goDown(SHOOTER_LOWER_SPEED);
	}

	@Override
	public void goUp()
	{
		dart.goUp(SHOOTER_RAISE_SPEED);
	}

	@Override
	public void stop()
	{
		dart.stop();
	}
}
