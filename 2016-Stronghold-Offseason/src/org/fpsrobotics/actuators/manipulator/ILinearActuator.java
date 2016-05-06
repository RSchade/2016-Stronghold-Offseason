package org.fpsrobotics.actuators.manipulator;

/**
 * Describes a motor that has a linear travel with bottom and top limits defined
 * through sensors.
 *
 */
public interface ILinearActuator
{
	public void goToTopLimit(double speed);

	public void goToBottomLimit(double speed);

	public void goToPosition(double speed, int position);
	
	public void goUp(double speed);
	
	public void goDown(double speed);
	
	public double getFeedbackCount();
	
	public boolean getIsUpAPositiveSlope();
	
	public void stop();
}
