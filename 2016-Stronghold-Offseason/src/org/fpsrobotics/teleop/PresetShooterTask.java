package org.fpsrobotics.teleop;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.fpsrobotics.actuators.manipulator.ManipulatorPreset;
import org.fpsrobotics.actuators.manipulator.MullenatorMechanism;
import org.fpsrobotics.sensors.EJoystickButtons;
import org.fpsrobotics.sensors.IGamepad;

public class PresetShooterTask implements ITeleopTask
{
	private ExecutorService executor;
	private MullenatorMechanism mechanism;
	private IGamepad gamepad;
	private EJoystickButtons shootLowButton, shootHighCornerButton, shootHighCenterButton;
	
	public PresetShooterTask(ExecutorService executor, MullenatorMechanism mechanism, IGamepad gamepad, EJoystickButtons shootLowButton, EJoystickButtons shootHighCornerButton, EJoystickButtons shootHighCenterButton)
	{
		this.executor = executor;
		this.mechanism = mechanism;
		this.gamepad = gamepad;
		this.shootLowButton = shootLowButton;
		this.shootHighCenterButton = shootHighCenterButton;
		this.shootHighCornerButton = shootHighCornerButton;
	}

	@Override
	public Future<?> doTask() {
		return executor.submit(() ->
		{
			if(gamepad.getButtonValue(EJoystickButtons.ONE))
			{
				return;
			}
			
			if(gamepad.getButtonValue(shootLowButton))
			{
				mechanism.goToPreset(ManipulatorPreset.SHOOT_LOW);
				mechanism.shootLow();
				
				while(gamepad.getButtonValue(shootLowButton))
				{
					
				}
			} else if(gamepad.getButtonValue(shootHighCornerButton))
			{
				mechanism.goToPreset(ManipulatorPreset.SHOOT_HIGH_CORNER);
				mechanism.shootHigh();
				
				while(gamepad.getButtonValue(shootHighCornerButton))
				{
					
				}
			} else if(gamepad.getButtonValue(shootHighCenterButton))
			{
				mechanism.goToPreset(ManipulatorPreset.SHOOT_HIGH_CENTER);
				mechanism.shootHigh();
				
				while(gamepad.getButtonValue(shootHighCenterButton))
				{
					
				}
			} else if (gamepad.getPOV() == 90) // manual high shot
			{
				mechanism.shootHigh();
				
				while(gamepad.getPOV() == 90)
				{
					
				}
			} else if (gamepad.getPOV() == 0) // manual low shot
			{
				mechanism.shootLow();
				
				while(gamepad.getPOV() == 0)
				{
					
				}
			}
				
		});
	}
	
	
}
