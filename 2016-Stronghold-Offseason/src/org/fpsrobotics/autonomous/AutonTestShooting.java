package org.fpsrobotics.autonomous;

import org.fpsrobotics.actuators.ActuatorConfig;

public class AutonTestShooting implements IAutonomousControl
{
	public AutonTestShooting()
	{

	}

	@Override
	public void doAuto(EAutoPositions position)
	{
		System.out.println("Testing Shoot -Raul");
		
		ActuatorConfig.getInstance().getAutoShot().shoot(position); 
	}

}
