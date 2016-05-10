package org.fpsrobotics.actuators.manipulator;

import org.fpsrobotics.PID.IPIDFeedbackDevice;
import org.fpsrobotics.actuators.IMotor;

public class DartLinearActuator implements ILinearActuator
{
	private IMotor screwMotor;
	private IPIDFeedbackDevice pot;
	private boolean isUpAPositiveSlope = true;
	int topLimit, bottomLimit, slowBuffer, steps;
	
	public DartLinearActuator(IMotor screwMotor, IPIDFeedbackDevice pot, boolean isUpAPositiveSlope, int bottomLimit, int topLimit, int slowBuffer, int rampSteps)
	{
		this.screwMotor = screwMotor;
		this.pot = pot;
		this.isUpAPositiveSlope = isUpAPositiveSlope;
		this.topLimit = topLimit;
		this.bottomLimit = bottomLimit;
		this.slowBuffer = slowBuffer;
		this.steps = rampSteps;
	}
	
	@Override
	public void goToTopLimit(double speed)
	{
		goToPosition(speed, topLimit);
	}

	@Override
	public void goToBottomLimit(double speed)
	{
		goToPosition(speed, bottomLimit);
	}
	
	@Override
	public void goToPosition(double speed, int position)
	{
		if(isUpAPositiveSlope)
		{
			if(pot.getCount() > position)
			{
				while(pot.getCount() > (position + slowBuffer) && !Thread.interrupted())
				{
					goDown(speed);
				}
				
				while(pot.getCount() > (position + 30) && !Thread.interrupted())
				{
					goDown(0.2);
				}
				
				stop();
				
			} else
			{
				while(pot.getCount() < (position - slowBuffer) && !Thread.interrupted())
				{
					goUp(speed);
				}
				
				while(pot.getCount() < (position - 30) && !Thread.interrupted())
				{
					goUp(0.2);
				}
				
				stop();
			}
		} else
		{
			if(pot.getCount() < position)
			{
				while(pot.getCount() < (position - slowBuffer) && !Thread.interrupted())
				{
					goDown(speed);
				}
				
				while(pot.getCount() < (position - 30) && !Thread.interrupted())
				{
					goDown(0.2);
				}
				
				stop();
				
			} else
			{
				while(pot.getCount() > (position + slowBuffer) && !Thread.interrupted())
				{
					goUp(speed);
				}
				
				while(pot.getCount() > (position + 30) && !Thread.interrupted())
				{
					goUp(0.2);
				}
				
				stop();
			}
		}
	}
	
	int deadZone = 10;

	@Override
	public void goUp(double speed)
	{
		if(Thread.interrupted())
		{
			stop();
			return;
		}
		
		if(speed < 0)
		{
			speed = -speed;
		}
		
		if(isUpAPositiveSlope)
		{
			if(!(pot.getCount() > topLimit - deadZone))
			{
				screwMotor.rampTo(speed, steps);
			} else
			{
				stop();
			}
		} else
		{
			if(!(pot.getCount() < topLimit + deadZone))
			{
				screwMotor.rampTo(speed, steps);
			} else
			{
				stop();
			}
		}
	}

	@Override
	public void goDown(double speed)
	{
		if(Thread.interrupted())
		{
			stop();
			return;
		}
		
		if(speed > 0)
		{
			speed = -speed;
		}
		
		if(isUpAPositiveSlope)
		{
			if(!(pot.getCount() < bottomLimit + deadZone))
			{
				screwMotor.rampTo(speed, steps);
			} else
			{
				stop();
			}
		} else
		{
			if(!(pot.getCount() > bottomLimit - deadZone))
			{
				screwMotor.rampTo(speed, steps);
			} else
			{
				stop();
			}
		}
	}

	@Override
	public double getFeedbackCount()
	{
		return pot.getCount();
	}
	
	public boolean getIsUpAPositiveSlope()
	{
		return isUpAPositiveSlope;
	}

	@Override
	public void stop()
	{
		screwMotor.rampDown();
	}
	
	protected void goToPositionNonstop(double speed, int position)
	{
		if(isUpAPositiveSlope)
		{
			if(pot.getCount() > position)
			{
				while(pot.getCount() > (position + slowBuffer) && !Thread.interrupted())
				{
					goDown(speed);
				}
				
			} else
			{
				while(pot.getCount() < (position - slowBuffer) && !Thread.interrupted())
				{
					goUp(speed);
				}
			}
		} else
		{
			if(pot.getCount() < position)
			{
				while(pot.getCount() < (position - slowBuffer) && !Thread.interrupted())
				{
					goDown(speed);
				}
				
			} else
			{
				while(pot.getCount() > (position + slowBuffer) && !Thread.interrupted())
				{
					goUp(speed);
				}
			}
		}
	}
	
}
