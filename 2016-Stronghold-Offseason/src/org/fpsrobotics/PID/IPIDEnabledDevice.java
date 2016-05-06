package org.fpsrobotics.PID;

import edu.wpi.first.wpilibj.CANTalon;

/**
 * Describes a generic device that can use PID, like a drive train or a shooter
 * mechanism.
 */
public interface IPIDEnabledDevice
{
	public void setP(double p);

	public void setI(double i);

	public void setD(double d);

	public void setPIDFeedbackDevice(IPIDFeedbackDevice device);

	public IPIDFeedbackDevice getPIDFeedbackDevice();

	public void enablePID();

	public void disablePID();

	public CANTalon.TalonControlMode getControlMode();

	public void setControlMode(CANTalon.TalonControlMode mode);
}
