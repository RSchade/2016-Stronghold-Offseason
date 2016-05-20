package org.fpsrobotics.sensors;

import org.fpsrobotics.PID.IPIDFeedbackDevice;

public interface IEncoder extends IPIDFeedbackDevice
{
	public double getAcceleration();
	
	public double getJerk();
}
