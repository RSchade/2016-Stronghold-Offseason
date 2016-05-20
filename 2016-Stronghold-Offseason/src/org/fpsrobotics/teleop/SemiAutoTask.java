package org.fpsrobotics.teleop;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.fpsrobotics.actuators.manipulator.MullenatorMechanism;
import org.fpsrobotics.autonomous.ISemiAutonomousMode;
import org.fpsrobotics.sensors.EJoystickButtons;
import org.fpsrobotics.sensors.IJoystick;

public class SemiAutoTask implements ITeleopTask
{
	private ExecutorService executor;
	private MullenatorMechanism mechanism;
	private IJoystick joystick;
	private EJoystickButtons chevalButton;
	private ISemiAutonomousMode chevalSemiAuto;
	
	public SemiAutoTask(ExecutorService executor, ISemiAutonomousMode chevalSemiAuto, IJoystick joystick, EJoystickButtons chevalButton)
	{
		this.executor = executor;
		this.mechanism = mechanism;
		this.joystick = joystick;
		this.chevalButton = chevalButton;
		this.chevalSemiAuto = chevalSemiAuto;
	}
	
	@Override
	public Future<?> doTask() {

		return executor.submit(() ->
		{
			if (joystick.getButtonValue(chevalButton))
			{
				chevalSemiAuto.doSemiAutonomous();
			}
		});
	}

}
