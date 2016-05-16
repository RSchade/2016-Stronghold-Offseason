package org.fpsrobotics.PID;

import edu.wpi.first.wpilibj.CANTalon;

/**
 * Describes a generic device that can provide feedback to a PID loop, like an
 * encoder on a wheel of a drive train.
 */
public interface IPIDFeedbackDevice
{
	public double getCount();

	public void enable();

	public void disable();

	public void resetCount();

	public double getError();

	public double getRate();

	public CANTalon.FeedbackDevice whatPIDDevice();

	public double getDistance();
	
	public void reverseSensor(boolean reversed);
}
