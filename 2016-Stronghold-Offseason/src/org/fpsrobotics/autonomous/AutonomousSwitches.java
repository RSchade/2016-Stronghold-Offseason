package org.fpsrobotics.autonomous;

import org.fpsrobotics.sensors.ILimitSwitch;

/**
 * 
 * A class that implements the 8 position switch that is used to determine the
 * autonomous mode at the beginning of a match.
 *
 * NOT USED FOR 2016
 */
public class AutonomousSwitches implements IAutoSwitches
{
	private ILimitSwitch ones;
	private ILimitSwitch twos;
	private ILimitSwitch fours;

	public AutonomousSwitches(ILimitSwitch _ones, ILimitSwitch _twos, ILimitSwitch _fours)
	{
		ones = _ones;
		twos = _twos;
		fours = _fours;
	}

	@Override
	public EAutoStates getAutoState()
	{
		if (ones.isHit())
		{
			if (twos.isHit())
			{
				if (fours.isHit())
				{
					// One & Two & Four
					return EAutoStates.SEVEN;
				} else
				{
					// One & Two & !Four
					return EAutoStates.THREE;
				}
			} else
			{
				if (fours.isHit())
				{
					// One & !Two & Four
					return EAutoStates.FIVE;
				} else
				{
					// One & !Two & !Four
					return EAutoStates.ONE;
				}
			}
		} else
		{
			if (twos.isHit())
			{
				if (fours.isHit())
				{
					// !One & Two & Four
					return EAutoStates.SIX;
				} else
				{
					// !One & Two & !Four
					return EAutoStates.TWO;
				}
			} else
			{
				if (fours.isHit())
				{
					// !One & !Two & Four
					return EAutoStates.FOUR;
				} else
				{
					// !One & !Two & !Four
					return EAutoStates.ZERO;
				}
			}
		}

	}
}
