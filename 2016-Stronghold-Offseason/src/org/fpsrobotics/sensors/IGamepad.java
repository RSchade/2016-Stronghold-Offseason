package org.fpsrobotics.sensors;

/**
 * Defines a gamepad, a controller with analog sticks.
 *
 */
public interface IGamepad
{
	public double getAnalogStickValue(EAnalogStickAxis axis);

	public boolean getButtonValue(EJoystickButtons button);
	
	public int getPOV();
}
