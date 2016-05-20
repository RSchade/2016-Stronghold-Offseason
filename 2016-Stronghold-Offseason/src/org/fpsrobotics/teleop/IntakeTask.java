package org.fpsrobotics.teleop;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.fpsrobotics.actuators.manipulator.MullenatorMechanism;
import org.fpsrobotics.sensors.EJoystickButtons;
import org.fpsrobotics.sensors.IGamepad;

public class IntakeTask implements ITeleopTask
{
	private ExecutorService executor;
	private MullenatorMechanism mechanism;
	private IGamepad gamepad;
	private EJoystickButtons intakeButton;
	
	public IntakeTask(ExecutorService executor, MullenatorMechanism mechanism, IGamepad gamepad, EJoystickButtons intakeButton)
	{
		this.executor = executor;
		this.mechanism = mechanism;
		this.gamepad = gamepad;
		this.intakeButton = intakeButton;
	}
	
	@Override
	public Future<?> doTask() {

		return executor.submit(() ->
		{
			while (gamepad.getButtonValue(intakeButton))
			{
				mechanism.intake();
			}

			mechanism.stopIntake();
		});
	}

}
