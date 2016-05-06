package org.fpsrobotics.actuators.manipulator;

public interface IMullenatorShooter
{
	public void moveToTopLimit();
	public void moveToBottomLimit();
	public void moveToPreset(EShooterPresets preset);
	public void moveToValue(int value);
	public void intake();
	public void stopWheels();
	public void shootHigh();
	public void shootLow();
	public int getPosition();
	public void goDown();
	public void goUp();
	public void stop();
}
