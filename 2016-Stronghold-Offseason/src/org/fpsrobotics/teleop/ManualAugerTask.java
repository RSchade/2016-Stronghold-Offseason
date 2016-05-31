package org.fpsrobotics.teleop;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.fpsrobotics.actuators.manipulator.MullenatorMechanism;
import org.fpsrobotics.sensors.EJoystickButtons;
import org.fpsrobotics.sensors.IGamepad;

public class ManualAugerTask implements ITeleopTask
{
	private ExecutorService executor;
	private IGamepad gamepad;
	private MullenatorMechanism mechanism;
	private EJoystickButtons upButton, downButton;

	public ManualAugerTask(ExecutorService executor, MullenatorMechanism mechanism, IGamepad gamepad,
			EJoystickButtons upButton, EJoystickButtons downButton)
	{
		this.executor = executor;
		this.mechanism = mechanism;
		this.gamepad = gamepad;
		this.upButton = upButton;
		this.downButton = downButton;
	}

	@Override
	public Future<?> doTask()
	{
		return executor.submit(() ->
		{
			while (gamepad.getButtonValue(upButton))
			{
				mechanism.moveAugerUp();
			}

			while (gamepad.getButtonValue(downButton))
			{
				mechanism.moveAugerDown();
			}

			mechanism.stopAuger();

		});
	}

}
