package org.fpsrobotics.robot;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.autonomous.AutonBreachDefensesAndShoot;
import org.fpsrobotics.autonomous.AutonChevalDeFrise;
import org.fpsrobotics.autonomous.AutonDoNothing;
import org.fpsrobotics.autonomous.AutonLowBarAndShootHigh;
import org.fpsrobotics.autonomous.AutonPortcullis;
import org.fpsrobotics.autonomous.AutonReachDefenses;
import org.fpsrobotics.autonomous.EAutoPositions;
import org.fpsrobotics.autonomous.FourtyKai;
import org.fpsrobotics.autonomous.IAutonomousControl;
import org.fpsrobotics.sensors.SensorConfig;
import org.fpsrobotics.teleop.ITeleopControl;
import org.fpsrobotics.teleop.MullenatorTeleop;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends SampleRobot
{
	private ExecutorService executor;
	private ITeleopControl teleop;
	
	private SendableChooser autoChooser, positionChooser;
	
	public Robot()
	{
		executor = Executors.newFixedThreadPool(2);
		
		teleop = new MullenatorTeleop();
		
		RobotStatus.checkIsAlpha();
	}

	public void robotInit()
	{
		ActuatorConfig.getInstance();
		SensorConfig.getInstance();
		
		autoChooser = new SendableChooser();

		autoChooser.addObject("Do Nothing", new AutonDoNothing());
		autoChooser.addObject("Fourty Kai", new FourtyKai());
		autoChooser.addObject("Reach Defenses", new AutonReachDefenses());
		autoChooser.addObject("Breach Standard Defenses", new AutonBreachDefensesAndShoot());
		autoChooser.addObject("Low Bar and Shoot High - PICK THIS ALMOST ALWAYS", new AutonLowBarAndShootHigh());
		autoChooser.addObject("Cheval De Frise", new AutonChevalDeFrise());
		autoChooser.addObject("Portcullis (This drives forward)", new AutonPortcullis());

		SmartDashboard.putData("Autonomous Chooser", autoChooser);
		
		positionChooser = new SendableChooser();

		positionChooser.addObject("Don't shoot at Raul", EAutoPositions.ZERO);
		positionChooser.addObject("Position Two", EAutoPositions.TWO);
		positionChooser.addObject("Position Three", EAutoPositions.THREE);
		positionChooser.addObject("Position Four", EAutoPositions.FOUR);
		positionChooser.addObject("Position Five", EAutoPositions.FIVE);

		SmartDashboard.putData("Autonomous Position Chooser", positionChooser);
	}

	public void autonomous()
	{
		RobotStatus.setIsRunning(true);
		RobotStatus.setIsAuto(true);
		RobotStatus.setIsTeleop(false);
		
		SensorConfig.getInstance().getGyro().hardResetCount();
		SensorConfig.getInstance().getGyro().softResetCount();
		
		executor.submit(() ->
		{
			((IAutonomousControl) autoChooser.getSelected()).doAuto((EAutoPositions) positionChooser.getSelected());
		});
	}

	public void operatorControl()
	{
		RobotStatus.setIsRunning(true);
		RobotStatus.setIsAuto(false);
		RobotStatus.setIsTeleop(true);
		
		teleop.doTeleop();
	}

	public void disabled()
	{
		RobotStatus.setIsRunning(false);
		RobotStatus.setIsAuto(false);
		RobotStatus.setIsTeleop(false);
		
		teleop.interruptTeleop();
	}

	public void test()
	{
		RobotStatus.setIsRunning(true);
	}
}
