package org.fpsrobotics.actuators.manipulator;

import org.fpsrobotics.actuators.ICANMotor;

public class MullenatorAuger implements IAuger
{
	private DartLinearActuator dart;
	private ICANMotor intakeWheels;

	private final double INTAKE_SPEED = 0.8;

	int FOURTY_KAI, INTAKE_AUGER, STANDARD_DEFENSE_AUGER, BOTTOM_LIMIT_AUGER, TOP_LIMIT_AUGER;

	private double UP_FAST_ZONE_SPEED = 0.8;
	private double UP_SPEED = 0.5;
	
	private double DOWN_FAST_ZONE_SPEED = 0.5;
	private double DOWN_SPEED = 0.5;

	private int UP_FAST_ZONE_VALUE;
	private int DOWN_FAST_ZONE_VALUE;

	public MullenatorAuger(DartLinearActuator dart, ICANMotor intakeWheels, boolean isAlpha)
	{
		this.dart = dart;
		this.intakeWheels = intakeWheels;

		if (isAlpha)
		{
			// AUGER
			TOP_LIMIT_AUGER = 1800;
			BOTTOM_LIMIT_AUGER = 546;
			FOURTY_KAI = 1400; // Used for High Shot Auto
			STANDARD_DEFENSE_AUGER = 740; // should be above
			INTAKE_AUGER = 690;
			
			UP_FAST_ZONE_VALUE = 1300;
			
			DOWN_FAST_ZONE_VALUE = 0;
		} else
		{
			// AUGER
			TOP_LIMIT_AUGER = 2200;
			BOTTOM_LIMIT_AUGER = 825;
			FOURTY_KAI = 1650;
			STANDARD_DEFENSE_AUGER = 1000;
			INTAKE_AUGER = 1050;
			
			UP_FAST_ZONE_VALUE = 1200;
			
			DOWN_FAST_ZONE_VALUE = 0;
		}
	}

	@Override
	public void moveToTopLimit()
	{
		moveToValue(TOP_LIMIT_AUGER);
	}

	@Override
	public void moveToBottomLimit()
	{
		moveToValue(BOTTOM_LIMIT_AUGER);
	}

	@Override
	public void moveToPreset(EAugerPresets preset)
	{
		switch (preset)
		{
		case LOW_BAR: // Going under the low bar
			moveToBottomLimit();
			break;
		case TOP_LIMIT: // NOT USED
			moveToTopLimit();
			break;
		case FOURTY_KAI: // Used at start of match
			moveToValue(FOURTY_KAI);
			break;
		case INTAKE: // Intake a ball
			moveToValue(INTAKE_AUGER);
			break;
		case STANDARD_DEFENSE_AUGER: // Going over a standard defense
			moveToValue(STANDARD_DEFENSE_AUGER);
			break;
		default:
			// Do Nothing
			System.err.println("Auger Defaulted: Doing Nothing, Probably Going Haywire");
		}
	}

	@Override
	public void moveToValue(int value)
	{
		if (dart.getIsUpAPositiveSlope())
		{
			if (value > dart.getFeedbackCount())
			{
				if (dart.getFeedbackCount() >= UP_FAST_ZONE_VALUE)
				{
					dart.goToPosition(UP_FAST_ZONE_SPEED, value);
					return;
				}

				if (value > UP_FAST_ZONE_VALUE)
				{
					dart.goToPositionNonstop(UP_SPEED, UP_FAST_ZONE_VALUE);
					dart.goToPosition(UP_FAST_ZONE_SPEED, value);
					return;
				}

				dart.goToPosition(UP_SPEED, value);
				return;
			} else
			{
				if (dart.getFeedbackCount() <= DOWN_FAST_ZONE_VALUE)
				{
					dart.goToPosition(DOWN_FAST_ZONE_SPEED, value);
					return;
				}

				if (value < DOWN_FAST_ZONE_VALUE)
				{
					dart.goToPositionNonstop(DOWN_SPEED, DOWN_FAST_ZONE_VALUE);
					dart.goToPosition(DOWN_FAST_ZONE_SPEED, value);
					return;
				}

				dart.goToPosition(DOWN_SPEED, value);
				return;
			}
		} else
		{
			if (value < dart.getFeedbackCount())
			{
				if (dart.getFeedbackCount() <= UP_FAST_ZONE_VALUE)
				{
					dart.goToPosition(UP_FAST_ZONE_SPEED, value);
					return;
				}

				if (value < UP_FAST_ZONE_VALUE)
				{
					dart.goToPositionNonstop(UP_SPEED, UP_FAST_ZONE_VALUE);
					dart.goToPosition(UP_FAST_ZONE_SPEED, value);
					return;
				}

				dart.goToPosition(UP_SPEED, value);
				return;
			} else
			{
				if (dart.getFeedbackCount() >= DOWN_FAST_ZONE_VALUE)
				{
					dart.goToPosition(DOWN_FAST_ZONE_SPEED, value);
					return;
				}

				if (value > DOWN_FAST_ZONE_VALUE)
				{
					dart.goToPositionNonstop(DOWN_SPEED, DOWN_FAST_ZONE_VALUE);
					dart.goToPosition(DOWN_FAST_ZONE_SPEED, value);
					return;
				}

				dart.goToPosition(DOWN_SPEED, value);
				return;
			}
		}
	}

	@Override
	public void intake()
	{
		intakeWheels.setSpeed(INTAKE_SPEED);

		if (Thread.interrupted())
		{
			intakeWheels.stop();
		}
	}

	@Override
	public void goUp()
	{
		if (dart.getIsUpAPositiveSlope())
		{
			if (dart.getFeedbackCount() > UP_FAST_ZONE_VALUE)
			{
				dart.goUp(UP_FAST_ZONE_SPEED);
			} else
			{
				dart.goUp(UP_SPEED);
			}
		} else
		{
			if (dart.getFeedbackCount() < UP_FAST_ZONE_VALUE)
			{
				dart.goUp(UP_FAST_ZONE_SPEED);
			} else
			{
				dart.goUp(UP_SPEED);
			}
		}
	}

	@Override
	public void goDown()
	{
		if (dart.getIsUpAPositiveSlope())
		{
			if (dart.getFeedbackCount() < DOWN_FAST_ZONE_VALUE)
			{
				dart.goUp(DOWN_FAST_ZONE_SPEED);
			} else
			{
				dart.goUp(DOWN_SPEED);
			}
		} else
		{
			if (dart.getFeedbackCount() > DOWN_FAST_ZONE_VALUE)
			{
				dart.goUp(DOWN_FAST_ZONE_SPEED);
			} else
			{
				dart.goUp(DOWN_SPEED);
			}
		}
	}

	@Override
	public void stop()
	{
		dart.stop();
	}

	@Override
	public int getPosition()
	{
		return (int) dart.getFeedbackCount();
	}

	@Override
	public void stopIntake()
	{
		intakeWheels.stop();
	}
}
