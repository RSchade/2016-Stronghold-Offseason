package org.fpsrobotics.teleop;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.actuators.drivetrain.DriveTrainProfile;
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

	// Instances
	private IGamepad gamepad;
	private MullenatorDrive driveTrain;
	private IJoystick leftJoystick;
	private IJoystick rightJoystick;
	private MullenatorMechanism mechanism;

	private Future<?> driveTask, shooterTask, intakeTask, presetTask, shotTask, lifterTask, semiAutoTask,
			smartDashboardTask;

	private ActuatorConfig actuators;
	private SensorConfig sensors;

	private ISemiAutonomousMode chevalSemiAuto;

	// Auto Teleop

	public MullenatorTeleop()
	{
		executor = Executors.newFixedThreadPool(4); // Maximum 4 concurrent
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

	double joystickCuttoff = 0.01;

	@Override
	public void doTeleop()
	{
		// Driving
		driveTask = executor.submit(() ->
		{
			while (RobotStatus.isTeleop())
			{
				if (rightJoystick.getButtonValue(EJoystickButtons.ONE))
				{
					SmartDashboard.putBoolean("DRIVE TOGETHER", true);
					
					SensorConfig.getInstance().getGyro().hardResetCount();

					while (rightJoystick.getButtonValue(EJoystickButtons.ONE))
					{
						double joyValue = rightJoystick.getY();

						// reversed joystick
						if (joyValue < 0)
						{
							//driveTrain.drive(joyValue, joyValue);
							driveTrain.driveForward(joyValue);
						} else if (joyValue > 0)
						{
							//driveTrain.drive(joyValue, joyValue);
							driveTrain.driveBackward(joyValue);
						} else
						{
							driveTrain.stop();
						}

						try
						{
							Thread.sleep(5);
						} catch (Exception e)
						{
							
						}
					}
					SmartDashboard.putBoolean("DRIVE TOGETHER", false);
				}
				
				double leftDrive = leftJoystick.getY(), rightDrive = rightJoystick.getY();

				if (Math.abs(leftDrive) > joystickCuttoff && Math.abs(rightDrive) > joystickCuttoff)
				{
					driveTrain.drive(leftDrive, rightDrive);
				} else if (Math.abs(leftDrive) < joystickCuttoff && Math.abs(rightDrive) < joystickCuttoff)
				{
					driveTrain.stop();
				} else if (Math.abs(leftDrive) < joystickCuttoff)
				{
					driveTrain.drive(0.0, rightDrive);
				} else if (Math.abs(rightDrive) < joystickCuttoff)
				{
					driveTrain.drive(leftDrive, 0.0);
				}

				try
				{
					Thread.sleep(50);
				} catch (Exception e1)
				{

				}
			}
		});

		while (RobotStatus.isTeleop())
		{

			// Manual Shooter
			if (gamepad.getButtonValue(EJoystickButtons.ELEVEN) && (shooterTask == null || shooterTask.isDone()))
			{
				
				shooterTask = executor.submit(() ->
				{
					while (gamepad.getButtonValue(EJoystickButtons.TWO))
					{
						mechanism.moveShooterDown();
					}

					while (gamepad.getButtonValue(EJoystickButtons.FOUR))
					{
						mechanism.moveShooterUp();
					}

					while (gamepad.getButtonValue(EJoystickButtons.FIVE))
					{
						mechanism.moveAugerUp();
					}

					while (gamepad.getButtonValue(EJoystickButtons.SIX))
					{
						mechanism.moveAugerDown();
					}

					mechanism.stopAuger();
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
						
						while(gamepad.getButtonValue(EJoystickButtons.SEVEN))
						{
							
						}
					} else if (gamepad.getButtonValue(EJoystickButtons.EIGHT)) // Shoot
																				// high
																				// corner
					{
						mechanism.goToPreset(ManipulatorPreset.SHOOT_HIGH_CORNER);
						mechanism.shootHigh();
						
						while(gamepad.getButtonValue(EJoystickButtons.EIGHT))
						{
							
						}
					} else if (gamepad.getButtonValue(EJoystickButtons.FOUR)) // Shoot
																				// high
																				// center
					{
						mechanism.goToPreset(ManipulatorPreset.SHOOT_HIGH_CENTER);
						mechanism.shootHigh();
						
						while(gamepad.getButtonValue(EJoystickButtons.FOUR))
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

			// Intake
			if (gamepad.getButtonValue(EJoystickButtons.THREE) && (intakeTask == null || intakeTask.isDone()))
			{
				intakeTask = executor.submit(() ->
				{
					while (gamepad.getButtonValue(EJoystickButtons.THREE))
					{
						mechanism.intake();
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
					if (shooterTask != null)
					{
						shooterTask.cancel(true);
					}

					if (intakeTask != null)
					{
						intakeTask.cancel(true);
					}

					if (presetTask != null)
					{
						presetTask.cancel(true);
					}

					if (shotTask != null)
					{
						shotTask.cancel(true);
					}

					if (lifterTask != null)
					{
						lifterTask.cancel(true);
					}
				});

				while (rightJoystick.getButtonValue(EJoystickButtons.ELEVEN))
				{

				}
			}

			// SmartDashboard update
			if (smartDashboardTask == null || smartDashboardTask.isDone())
			{
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
			}
			
			try
			{
				Thread.sleep(30);
			} catch (InterruptedException e)
			{

			}
		}
	}

	@Override
	public void interruptTeleop()
	{
		if (shooterTask != null)
		{
			shooterTask.cancel(true);
		}

		if (intakeTask != null)
		{
			intakeTask.cancel(true);
		}

		if (presetTask != null)
		{
			presetTask.cancel(true);
		}

		if (shotTask != null)
		{
			shotTask.cancel(true);
		}

		if (lifterTask != null)
		{
			lifterTask.cancel(true);
		}

		if (driveTask != null)
		{
			driveTask.cancel(true);
		}
	}
}
