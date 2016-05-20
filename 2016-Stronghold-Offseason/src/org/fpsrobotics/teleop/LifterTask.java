package org.fpsrobotics.teleop;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.fpsrobotics.actuators.manipulator.ManipulatorPreset;
import org.fpsrobotics.actuators.manipulator.MullenatorMechanism;
import org.fpsrobotics.sensors.EJoystickButtons;
import org.fpsrobotics.sensors.IGamepad;

public class LifterTask implements ITeleopTask
{
	private ExecutorService executor;
	private MullenatorMechanism mechanism;
	private IGamepad gamepad;
	private EJoystickButtons lifterExtendButton, lifterRetractButton;
	
	public LifterTask(ExecutorService executor, MullenatorMechanism mechanism, IGamepad gamepad, EJoystickButtons lifterExtendButton, EJoystickButtons lifterRetractButton)
	{
		this.executor = executor;
		this.mechanism = mechanism;
		this.gamepad = gamepad;
		this.lifterExtendButton = lifterExtendButton;
		this.lifterRetractButton = lifterRetractButton;
	}
	
	@Override
	public Future<?> doTask() {

		return executor.submit(() ->
		{
			if (gamepad.getButtonValue(lifterExtendButton))
			{
				mechanism.extend();
			} else if (gamepad.getButtonValue(lifterRetractButton))
			{
				mechanism.retract();
				mechanism.goToPreset(ManipulatorPreset.LIFTER_AUGER_MOVEMENTS);
			}
		});
	}

}
