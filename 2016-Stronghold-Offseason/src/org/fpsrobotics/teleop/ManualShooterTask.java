package org.fpsrobotics.teleop;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.fpsrobotics.actuators.drivetrain.MullenatorDrive;
import org.fpsrobotics.actuators.manipulator.MullenatorMechanism;
import org.fpsrobotics.sensors.EJoystickButtons;
import org.fpsrobotics.sensors.IGamepad;

public class ManualShooterTask implements ITeleopTask {
	private ExecutorService executor;
	private IGamepad gamepad;
	private MullenatorMechanism mechanism;
	private EJoystickButtons upButton, downButton;

	public ManualShooterTask(ExecutorService executor, MullenatorMechanism mechanism, IGamepad gamepad, EJoystickButtons upButton, EJoystickButtons downButton) {
		this.executor = executor;
		this.mechanism = mechanism;
		this.gamepad = gamepad;
	}

	@Override
	public Future<?> doTask() 
	{
		return executor.submit(() -> {
			while (gamepad.getButtonValue(upButton)) {
				mechanism.moveShooterUp();
			}
			
			while (gamepad.getButtonValue(downButton)) {
				mechanism.moveShooterDown();
			}
			
			mechanism.stopShooter();
		});
	}

}
