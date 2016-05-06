package org.fpsrobotics.autonomous;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.actuators.manipulator.EAugerPresets;
import org.fpsrobotics.actuators.manipulator.EShooterPresets;
import org.fpsrobotics.actuators.manipulator.ManipulatorPreset;

public class FourtyKai implements IAutonomousControl
{
	public FourtyKai()
	{

	}

	@Override
	public void doAuto(EAutoPositions position)
	{
		ActuatorConfig.getInstance().getMechanism().goToPreset(ManipulatorPreset.FOURTY_KAI);
		
		System.out.println("Fourty Kai'd");
	}

}
