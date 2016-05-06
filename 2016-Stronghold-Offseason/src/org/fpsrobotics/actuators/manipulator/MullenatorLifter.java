package org.fpsrobotics.actuators.manipulator;

import org.fpsrobotics.actuators.DoubleSolenoid;

public class MullenatorLifter implements ILifter
{
	private DoubleSolenoid lifterOne, lifterTwo;

	public MullenatorLifter(DoubleSolenoid lifterOne, DoubleSolenoid lifterTwo)
	{
		this.lifterOne = lifterOne;
		this.lifterTwo = lifterTwo;
	}

	public void lift()
	{
		lifterOne.engage();
		lifterTwo.engage();
	}

	public void retract()
	{
		lifterOne.disengage();
		lifterTwo.disengage();
	}
}
