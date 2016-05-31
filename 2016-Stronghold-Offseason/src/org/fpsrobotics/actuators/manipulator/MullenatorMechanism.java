package org.fpsrobotics.actuators.manipulator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MullenatorMechanism
{
	private MullenatorAuger auger;
	private MullenatorLifter lifter;
	private MullenatorShooter shooter;

	private int AUGER_VALUE_WILL_HIT_SHOOTER = 670;

	ExecutorService executor;

	public MullenatorMechanism(MullenatorAuger auger, MullenatorLifter lifter, MullenatorShooter shooter)
	{
		this.auger = auger;
		this.lifter = lifter;
		this.shooter = shooter;

		executor = Executors.newFixedThreadPool(5);
	}

	public void moveAugerUp()
	{
		auger.goUp();
	}

	public void moveAugerDown()
	{
		auger.goDown();

		if ((auger.getPosition() <= AUGER_VALUE_WILL_HIT_SHOOTER) && (shooter.getPosition() <= shooter.LOW_BAR_SHOOTER))
		{
			executor.submit(() ->
			{
				shooter.moveToPreset(EShooterPresets.LOW_BAR);
			});
		}
	}

	public void goToPreset(ManipulatorPreset preset)
	{
		switch (preset)
		{
		case LOW_BAR:
			executor.submit(() ->
			{
				try
				{
					Thread.sleep(200);
				} catch (Exception e)
				{
					
				}
				auger.moveToBottomLimit();
			});
			
			shooter.moveToPreset(EShooterPresets.LOW_BAR);
			
			break;
		case SHOOT_HIGH_CORNER:
			if (auger.getPosition() < auger.FOURTY_KAI)
			{
				executor.submit(() ->
				{
					auger.moveToPreset(EAugerPresets.FOURTY_KAI);
				});
			}

			shooter.moveToPreset(EShooterPresets.CORNER_SHOT_TELEOP);
			break;
		case SHOOT_HIGH_CENTER:
			if (auger.getPosition() < auger.FOURTY_KAI)
			{
				executor.submit(() ->
				{
					auger.moveToPreset(EAugerPresets.FOURTY_KAI);
				});
			}

			shooter.moveToPreset(EShooterPresets.CENTER_SHOT_TELEOP);
			break;
		case SHOOT_LOW:
			if (auger.getPosition() < auger.STANDARD_DEFENSE_AUGER)
			{
				executor.submit(() ->
				{
					auger.moveToPreset(EAugerPresets.STANDARD_DEFENSE_AUGER);
				});
			}

			shooter.moveToPreset(EShooterPresets.LOW_BAR);
			break;
		case STANDARD_DEFENSE:

			executor.submit(() ->
			{
				auger.moveToPreset(EAugerPresets.STANDARD_DEFENSE_AUGER);
			});

			shooter.moveToPreset(EShooterPresets.STANDARD_DEFENSE_SHOOTER);
			break;

		case LIFTER_AUGER_MOVEMENTS:
			// implement later
			break;
		case POSITION_FIVE_AUTO:
			if (auger.getPosition() < auger.FOURTY_KAI)
			{
				executor.submit(() ->
				{
					auger.moveToPreset(EAugerPresets.FOURTY_KAI);
				});
			}

			shooter.moveToPreset(EShooterPresets.SHOOTER_POSITION_AT_DEFENSE_FIVE_AUTO);
			break;
		case POSITION_FOUR_AUTO:
			if (auger.getPosition() < auger.FOURTY_KAI)
			{
				executor.submit(() ->
				{
					auger.moveToPreset(EAugerPresets.FOURTY_KAI);
				});
			}

			shooter.moveToPreset(EShooterPresets.SHOOTER_POSITION_AT_DEFENSE_FOUR_AUTO);
			break;
		case POSITION_THREE_AUTO:
			if (auger.getPosition() < auger.FOURTY_KAI)
			{
				executor.submit(() ->
				{
					auger.moveToPreset(EAugerPresets.FOURTY_KAI);
				});
			}

			shooter.moveToPreset(EShooterPresets.SHOOTER_POSITION_AT_DEFENSE_THREE_AUTO);
			break;
		case POSITION_TWO_AUTO:
			if (auger.getPosition() < auger.FOURTY_KAI)
			{
				executor.submit(() ->
				{
					auger.moveToPreset(EAugerPresets.FOURTY_KAI);
				});
			}

			shooter.moveToPreset(EShooterPresets.SHOOTER_POSITION_AT_DEFENSE_TWO_AUTO);
			break;
		case FOURTY_KAI:
			executor.submit(() ->
			{
				auger.moveToPreset(EAugerPresets.FOURTY_KAI);
			});
			
			shooter.moveToPreset(EShooterPresets.LOW_BAR);
		default:
			break;
		}
	}

	public void moveShooterUp()
	{
		if (shooter.getPosition() <= AUGER_VALUE_WILL_HIT_SHOOTER)
		{
			stopShooter();
		} else
		{
			shooter.goUp();
		}
	}

	public void moveShooterDown()
	{
		shooter.goDown();
	}

	public void stopShooter()
	{
		shooter.stop();
	}

	public void stopAuger()
	{
		auger.stop();
	}

	public void shootHigh()
	{
		shooter.shootHigh();
	}

	public void shootLow()
	{
		shooter.shootLow();
	}

	public void intake()
	{
		auger.intake();

		executor.submit(() ->
		{
			auger.moveToPreset(EAugerPresets.INTAKE);
		});
		
		shooter.intake();
	}

	public void stopIntake()
	{
		auger.stopIntake();
		shooter.stopWheels();
	}

	public void extend()
	{
		lifter.lift();
	}

	public void retract()
	{
		lifter.retract();
	}
}
