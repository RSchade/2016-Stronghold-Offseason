package org.fpsrobotics.sensors;

/**
 * 
 * Defines a joystick, also called a flight stick.
 *
 */
public interface IJoystick
{
	public double getX();

	public double getY();

	public double getTwist();

	public double getSwitch();

	public boolean getButtonValue(EJoystickButtons value);

}
