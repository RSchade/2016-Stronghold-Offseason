package org.fpsrobotics.actuators.manipulator;

public interface IAuger
{
	public void moveToTopLimit();
	public void moveToBottomLimit();
	public void moveToPreset(EAugerPresets preset);
	public void moveToValue(int value);
	public void intake();
	public void stopIntake();
	public void goUp();
	public void goDown();
	public void stop();
	public int getPosition();
}
