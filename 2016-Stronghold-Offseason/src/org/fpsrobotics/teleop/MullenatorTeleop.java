package org.fpsrobotics.teleop;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.actuators.drivetrain.MullenatorDrive;
import org.fpsrobotics.actuators.manipulator.ManipulatorPreset;
import org.fpsrobotics.actuators.manipulator.MullenatorMechanism;
import org.fpsrobotics.autonomous.AutonChevalDeFrise;
import org.fpsrobotics.autonomous.ISemiAutonomousMode;
import org.fpsrobotics.robot.RobotStatus;
import org.fpsrobotics.sensors.EJoystickButtons;
import org.fpsrobotics.sensors.IGamepad;
import org.fpsrobotics.sensors.IJoystick;
import org.fpsrobotics.sensors.SensorConfig;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class MullenatorTeleop implements ITeleopControl
{
	private ExecutorService executor;
	
	private boolean autoGyroDriveActivated = false;

	// Instances
	private IGamepad gamepad;
	private MullenatorDrive driveTrain;
	private IJoystick leftJoystick;
	private IJoystick rightJoystick;
	private MullenatorMechanism mechanism;

	private Future<?> driveTask, gyroTask, shooterTask, intakeTask, presetTask, shotTask, lifterTask, semiAutoTask;

	private ActuatorConfig actuators;
	private SensorConfig sensors;
	
	private ISemiAutonomousMode chevalSemiAuto;

	// Auto Teleop

	public MullenatorTeleop()
	{
		executor = Executors.newFixedThreadPool(6); // Maximum 6 concurrent
													// tasks

		// Instances
		gamepad = SensorConfig.getInstance().getGamepad();
		leftJoystick = SensorConfig.getInstance().getLeftJoystick();
		rightJoystick = SensorConfig.getInstance().getRightJoystick();

		mechanism = ActuatorConfig.getInstance().getMechanism();
		driveTrain = ActuatorConfig.getInstance().getDriveTrain();

		actuators = ActuatorConfig.getInstance();
		sensors = SensorConfig.getInstance();
		
		chevalSemiAuto = new AutonChevalDeFrise();
	}

	@Override
	public void doTeleop()
	{
		// Driving
		driveTask = executor.submit(() ->
		{
			while (RobotStatus.isTeleop())
			{
				driveTrain.drive(leftJoystick.getY(), rightJoystick.getY());

				try
				{
					Thread.sleep(50);
				} catch (Exception e1)
				{
					
				}

				while (autoGyroDriveActivated)
				{
					try
					{
						Thread.sleep(150);
					} catch (Exception e)
					{
						
					}
				}
			}
		});

		while (RobotStatus.isTeleop())
		{
			// Driver Side
			if (rightJoystick.getButtonValue(EJoystickButtons.ONE) && (gyroTask == null || gyroTask.isDone()))
			{
				gyroTask = executor.submit(() ->
				{
					autoGyroDriveActivated = true;
					SmartDashboard.putBoolean("DRIVE TOGETHER", true);

					SensorConfig.getInstance().getGyro().softResetCount();

					while (rightJoystick.getButtonValue(EJoystickButtons.ONE))
					{
						double joyValue = rightJoystick.getY();
						
						if(joyValue > 0)
						{
							driveTrain.driveForward(joyValue);
						} else
						{
							driveTrain.driveBackward(joyValue);
						}
						
						try
						{
							Thread.sleep(50);
						} catch (Exception e)
						{
							break;
						}
					}
					
					autoGyroDriveActivated = false;
					SmartDashboard.putBoolean("DRIVE TOGETHER", false);
				});
			}

			// Manual Shooter
			if (gamepad.getButtonValue(EJoystickButtons.ONE) && (shooterTask == null || shooterTask.isDone()))
			{
				shooterTask = executor.submit(() ->
				{
					while (gamepad.getButtonValue(EJoystickButtons.TWO))
					{
						mechanism.moveShooterDown();

						try
						{
							Thread.sleep(50);
						} catch (Exception e)
						{
							break;
						}
					}

					while (gamepad.getButtonValue(EJoystickButtons.FOUR))
					{
						mechanism.moveShooterUp();

						try
						{
							Thread.sleep(50);
						} catch (Exception e)
						{
							break;
						}
					}

					mechanism.stopShooter();
				});
			}

			// Shoot
			if ((gamepad.getButtonValue(EJoystickButtons.ONE) && gamepad.getButtonValue(EJoystickButtons.SEVEN))
					|| gamepad.getButtonValue(EJoystickButtons.EIGHT) || (gamepad.getPOV() == 90)
					|| (gamepad.getPOV() == 0) && (shotTask == null || shotTask.isDone()))
			{
				shotTask = executor.submit(() ->
				{
					// Shoot low
					if (gamepad.getButtonValue(EJoystickButtons.ONE) && gamepad.getButtonValue(EJoystickButtons.SEVEN))
					{
						mechanism.goToPreset(ManipulatorPreset.SHOOT_LOW);
						mechanism.shootLow();
					} else if (gamepad.getButtonValue(EJoystickButtons.EIGHT)) // Shoot
																				// high
																				// corner
					{
						mechanism.goToPreset(ManipulatorPreset.SHOOT_HIGH_CORNER);
						mechanism.shootHigh();
					} else if (gamepad.getButtonValue(EJoystickButtons.EIGHT)) // Shoot
																				// high
																				// center
					{
						mechanism.goToPreset(ManipulatorPreset.SHOOT_HIGH_CENTER);
						mechanism.shootHigh();
					} else if (gamepad.getPOV() == 90) // manual high shot
					{
						mechanism.shootHigh();
					} else if (gamepad.getPOV() == 0) // manual low shot
					{
						mechanism.shootLow();
					}
				});
			}

			// Intake
			if (gamepad.getButtonValue(EJoystickButtons.THREE) && (intakeTask == null || intakeTask.isDone()))
			{
				intakeTask = executor.submit(() ->
				{
					while (gamepad.getButtonValue(EJoystickButtons.THREE))
					{
						mechanism.intake();

						try
						{
							Thread.sleep(50);
						} catch (Exception e)
						{
							break;
						}
					}

					mechanism.stopIntake();
				});
			}

			// Presets
			if (rightJoystick.getButtonValue(EJoystickButtons.THREE)
					|| rightJoystick.getButtonValue(EJoystickButtons.FOUR)
							&& (presetTask == null || presetTask.isDone()))
			{
				presetTask = executor.submit(() ->
				{
					// Low bar
					if (rightJoystick.getButtonValue(EJoystickButtons.THREE))
					{
						mechanism.goToPreset(ManipulatorPreset.LOW_BAR);
					} else if (rightJoystick.getButtonValue(EJoystickButtons.FOUR)) // Standard
																					// defense
					{
						mechanism.goToPreset(ManipulatorPreset.LOW_BAR);
					}
				});
			}

			// Lifter
			if (gamepad.getButtonValue(EJoystickButtons.NINE)
					|| gamepad.getButtonValue(EJoystickButtons.TEN) && (lifterTask == null || lifterTask.isDone()))
			{
				lifterTask = executor.submit(() ->
				{
					if (gamepad.getButtonValue(EJoystickButtons.NINE))
					{
						mechanism.extend();
					} else if (gamepad.getButtonValue(EJoystickButtons.TEN))
					{
						mechanism.retract();
						mechanism.goToPreset(ManipulatorPreset.LIFTER_AUGER_MOVEMENTS);
					}
				});
			}

			// SemiAutonomous Modes
			if (leftJoystick.getButtonValue(EJoystickButtons.NINE) && (semiAutoTask == null || semiAutoTask.isDone()))
			{
				semiAutoTask = executor.submit(() ->
				{
					if (leftJoystick.getButtonValue(EJoystickButtons.NINE))
					{
						chevalSemiAuto.doSemiAutonomous();
					}
				});
			}
					
			
			
			// Abort button
			if (rightJoystick.getButtonValue(EJoystickButtons.ELEVEN))
			{
				executor.submit(() ->
				{
					gyroTask.cancel(true);
					shooterTask.cancel(true);
					intakeTask.cancel(true);
					presetTask.cancel(true);
					shotTask.cancel(true);
					lifterTask.cancel(true);
				});
			}

			// SmartDashboard update
			executor.submit(() ->
			{
				// Shooter Pot Value
				SmartDashboard.putNumber("Shooter Pot", actuators.getShooterPotentiometer().getCount());

				// Pressure sensor feedback
				SmartDashboard.putBoolean("Pressure", sensors.getPressureSwitch().isHit());

				// Auger Pot Value
				SmartDashboard.putNumber("Auger Pot", actuators.getAugerPotentiometer().getCount());

				// Compass or gyro
				SmartDashboard.putNumber("Soft Count", sensors.getGyro().getSoftCount());
				SmartDashboard.putNumber("Hard Count", sensors.getGyro().getHardCount());

				if ((-10 < sensors.getGyro().getSoftCount()) && ((sensors.getGyro().getSoftCount() < 10)))
				{
					SmartDashboard.putBoolean("Are we Straight?", true);
				} else
				{
					SmartDashboard.putBoolean("Are we Straight?", false);
				}
			});

			try
			{
				Thread.sleep(75);
			} catch (InterruptedException e)
			{

			}
		}

	}

	@Override
	public void interruptTeleop()
	{
		gyroTask.cancel(true);
		shooterTask.cancel(true);
		intakeTask.cancel(true);
		presetTask.cancel(true);
		shotTask.cancel(true);
		lifterTask.cancel(true);
		//driveTask.cancel(true); don't interrupt manual driving for now...
	}
}
