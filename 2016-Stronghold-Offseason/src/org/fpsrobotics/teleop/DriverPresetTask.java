package org.fpsrobotics.teleop;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.fpsrobotics.actuators.manipulator.ManipulatorPreset;
import org.fpsrobotics.actuators.manipulator.MullenatorMechanism;
import org.fpsrobotics.sensors.EJoystickButtons;
import org.fpsrobotics.sensors.IJoystick;

public class DriverPresetTask implements ITeleopTask {

	private ExecutorService executor;
	private MullenatorMechanism mechanism;
	private IJoystick joystick;
	private EJoystickButtons lowBarButton, standardDefenseButton;

	public DriverPresetTask(ExecutorService executor, MullenatorMechanism mechanism, IJoystick joystick,
			EJoystickButtons lowBarButton, EJoystickButtons standardDefenseButton) {
		this.executor = executor;
		this.mechanism = mechanism;
		this.joystick = joystick;
		this.standardDefenseButton = standardDefenseButton;
		this.lowBarButton = lowBarButton;
	}

	@Override
	public Future<?> doTask() {
		return executor.submit(() -> {
			// Low bar
			if (joystick.getButtonValue(lowBarButton)) {
				mechanism.goToPreset(ManipulatorPreset.LOW_BAR);
			} else if (joystick.getButtonValue(standardDefenseButton)) // Standard
																		// defense
			{
				mechanism.goToPreset(ManipulatorPreset.LOW_BAR);
			}
		});

	}

}
