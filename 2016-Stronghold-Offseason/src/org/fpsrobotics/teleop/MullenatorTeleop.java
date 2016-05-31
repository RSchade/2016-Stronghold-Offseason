package org.fpsrobotics.teleop;

import java.util.ArrayList;
import java.util.HashMap;
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

	private Future<?> smartDashboardTask;
	
	private HashMap<String, Future<?>> futureTable;
	private ITeleopTask driverPresetTask, driveTask, intakeTask, lifterTask, manualAugerTask, manualShooterTask, semiAutoTask, presetShooterTask; // implement lifter if needed

	private ActuatorConfig actuators;
	private SensorConfig sensors;

	private ISemiAutonomousMode chevalSemiAuto; // work on this later
	
	private EJoystickButtons shootLowButton, shootHighCornerButton, shootHighCenterButton, lowBarButton, standardDefenseButton, intakeButton, 
							 lifterExtendButton, lifterRetractButton, raiseAugerButton, lowerAugerButton, raiseShooterButton, lowerShooterButton;
	
	private IJoystick presetJoystick;

	// Auto Teleop

	public MullenatorTeleop()
	{
		executor = Executors.newFixedThreadPool(4); // Maximum 4 concurrent
													// tasks
		
		futureTable = new HashMap<>();

		// Instances
		gamepad = SensorConfig.getInstance().getGamepad();
		leftJoystick = SensorConfig.getInstance().getLeftJoystick();
		rightJoystick = SensorConfig.getInstance().getRightJoystick();

		mechanism = ActuatorConfig.getInstance().getMechanism();
		driveTrain = ActuatorConfig.getInstance().getDriveTrain();

		actuators = ActuatorConfig.getInstance();
		sensors = SensorConfig.getInstance();

		chevalSemiAuto = new AutonChevalDeFrise();
		
		lowBarButton = EJoystickButtons.EIGHT;
		standardDefenseButton = EJoystickButtons.NINE;
		intakeButton = EJoystickButtons.THREE;
		lifterExtendButton =  EJoystickButtons.NINE;
		lifterRetractButton = EJoystickButtons.TEN;
		raiseAugerButton = EJoystickButtons.FIVE;
		lowerAugerButton = EJoystickButtons.SIX;
		raiseShooterButton =  EJoystickButtons.FOUR;
		lowerShooterButton =  EJoystickButtons.TWO;
		shootLowButton = EJoystickButtons.SEVEN;
		shootHighCornerButton = EJoystickButtons.EIGHT;
		shootHighCenterButton = EJoystickButtons.FOUR;
		
		presetJoystick = rightJoystick;
				
		driverPresetTask = new DriverPresetTask(executor, mechanism, rightJoystick, lowBarButton, standardDefenseButton);
		driveTask = new DriveTask(executor, driveTrain, leftJoystick, rightJoystick);
		intakeTask = new IntakeTask(executor, mechanism, gamepad, intakeButton);
		lifterTask = new LifterTask(executor, mechanism, gamepad, lifterExtendButton, lifterRetractButton);
		manualAugerTask = new ManualAugerTask(executor, mechanism, gamepad, raiseAugerButton, lowerAugerButton);
		manualShooterTask = new ManualShooterTask(executor, mechanism, gamepad, raiseShooterButton, lowerShooterButton);
		presetShooterTask = new PresetShooterTask(executor, mechanism, gamepad, shootLowButton, shootHighCornerButton, shootHighCenterButton);
	}

	@Override
	public void doTeleop()
	{
		driveTask.doTask();

		while (RobotStatus.isTeleop())
		{
			if(gamepad.getButtonValue(EJoystickButtons.ONE))
			{	
				if((gamepad.getButtonValue(raiseAugerButton) || gamepad.getButtonValue(lowerAugerButton)) 
						&& (futureTable.get("manualauger") == null || futureTable.get("manualauger").isDone()))
				{
					futureTable.put("manualauger", manualAugerTask.doTask());
				}
				
				if((gamepad.getButtonValue(raiseShooterButton) || gamepad.getButtonValue(lowerShooterButton))
						&& (futureTable.get("manualshooter") == null || futureTable.get("manualshooter").isDone()))
				{
					futureTable.put("manualshooter", manualShooterTask.doTask());
				}
			} 
			
			if(gamepad.getButtonValue(EJoystickButtons.TWO)
					&& (futureTable.get("presetshooter") == null || futureTable.get("presetshooter").isDone()))
			{
				futureTable.put("presetshooter", presetShooterTask.doTask());
			}
			
			if(gamepad.getButtonValue(intakeButton)
					&& (futureTable.get("intake") == null || futureTable.get("intake").isDone()))
			{
				futureTable.put("intake", intakeTask.doTask());
			}
			
			if((presetJoystick.getButtonValue(standardDefenseButton) || presetJoystick.getButtonValue(lowBarButton))
					&& (futureTable.get("driverpreset") == null || futureTable.get("driverpreset").isDone()))
			{
				futureTable.put("driverpreset", driverPresetTask.doTask());
			}
			
			// Abort button
			if (rightJoystick.getButtonValue(EJoystickButtons.ELEVEN))
			{
				executor.submit(() ->
				{
					for(String id : futureTable.keySet())
					{
						futureTable.get(id).cancel(true);
					}
				});

				while (rightJoystick.getButtonValue(EJoystickButtons.ELEVEN))
				{

				}
			}

			// SmartDashboard update
			if (smartDashboardTask == null || smartDashboardTask.isDone())
			{
				smartDashboardTask = executor.submit(() ->
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
		for(String id : futureTable.keySet())
		{
			futureTable.get(id).cancel(true);
		}
	}
}
