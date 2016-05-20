package org.fpsrobotics.teleop;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.fpsrobotics.actuators.drivetrain.MullenatorDrive;
import org.fpsrobotics.robot.RobotStatus;
import org.fpsrobotics.sensors.EJoystickButtons;
import org.fpsrobotics.sensors.IJoystick;
import org.fpsrobotics.sensors.SensorConfig;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTask implements ITeleopTask
{
	private ExecutorService executor;
	private IJoystick leftJoystick;
	private IJoystick rightJoystick;
	private MullenatorDrive driveTrain;
	
	private double joystickCuttoff = 0.01;
	
	public DriveTask(ExecutorService executor, MullenatorDrive driveTrain, IJoystick leftJoystick, IJoystick rightJoystick)
	{
		this.executor = executor;
		this.leftJoystick = leftJoystick;
		this.rightJoystick = rightJoystick;
		this.driveTrain = driveTrain;
	}

	@Override
	public Future<?> doTask() 
	{
		return executor.submit(() ->
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
	}

}
