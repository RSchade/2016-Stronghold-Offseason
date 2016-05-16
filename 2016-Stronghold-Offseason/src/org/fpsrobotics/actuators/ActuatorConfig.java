package org.fpsrobotics.actuators;

import org.fpsrobotics.PID.IPIDFeedbackDevice;
import org.fpsrobotics.actuators.drivetrain.MullenatorDrive;
import org.fpsrobotics.actuators.manipulator.DartLinearActuator;
import org.fpsrobotics.actuators.manipulator.MullenatorAuger;
import org.fpsrobotics.actuators.manipulator.MullenatorLifter;
import org.fpsrobotics.actuators.manipulator.MullenatorMechanism;
import org.fpsrobotics.actuators.manipulator.MullenatorShooter;
import org.fpsrobotics.autonomous.AutoShot;
import org.fpsrobotics.robot.RobotStatus;
import org.fpsrobotics.sensors.BuiltInCANTalonEncoder;
import org.fpsrobotics.sensors.Potentiometer;
import org.fpsrobotics.sensors.SensorConfig;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Solenoid;

/**
 * Singleton class that creates and distributes all of the actuators (motors,
 * solenoids, linear actuators, etc.) to the rest of the program. Encoders are
 * included in this class as well because of the close integration with motors.
 */
public class ActuatorConfig
{
	private static ActuatorConfig singleton = null;

	//TODO: Set PID in the silverlight dashboard on the RoboRIO

	// Port definitions
	private final int MOTOR_LEFT_FRONT = 3;
	private final int MOTOR_LEFT_REAR = 4;
	private final int MOTOR_RIGHT_FRONT = 1;
	private final int MOTOR_RIGHT_REAR = 2;

	private final int LEFT_SHOOTER_MOTOR = 5;
	private final int RIGHT_SHOOTER_MOTOR = 6;

	private final int LINEAR_ACTUATOR_MOTOR = 7;
	private final int AUGER_INTAKE_MOTOR = 8;
	private final int AUGER_LIFTER_MOTOR = 9;
	private final int AUGER_POTENTIOMETER_CHANNEL = 1;
	private final int SHOOTER_POTENTIOMETER_CHANNEL = 0;

	private final int SHOOTER_SOLENOID_PORT = 0;
	private final int LIFTER_SOLENOID_ONE_PORT_A = 1;
	private final int LIFTER_SOLENOID_ONE_PORT_B = 2;
	private final int LIFTER_SOLENOID_TWO_PORT_A = 3;
	private final int LIFTER_SOLENOID_TWO_PORT_B = 4;
	
	private final int LOWER_SHOOTER_LIMIT = 2100;
	private final int UPPER_SHOOTER_LIMIT = 588;
	private final int LOWER_AUGER_LIMIT = 718;
	private final int UPPER_AUGER_LIMIT = 1900;

	// Shooter Solenoid
	private ISolenoid shooterSolenoid;

	// Drive base classes
	private CANTalon leftFrontMotor;
	private CANTalon leftRearMotor;
	private CANTalon rightFrontMotor;
	private CANTalon rightRearMotor;

	// Drive CAN Motors
	private CANMotor leftFrontCANMotor;
	private CANMotor leftRearCANMotor;
	private CANMotor rightFrontCANMotor;
	private CANMotor rightRearCANMotor;

	// Drive double motors
	private DoubleMotor leftDoubleMotor;
	private DoubleMotor rightDoubleMotor;

	// Drive encoders
	private IPIDFeedbackDevice leftDriveEncoder;
	private IPIDFeedbackDevice rightDriveEncoder;

	// Auger encoder
	private IPIDFeedbackDevice augerPotentiometer;
	
	private IPIDFeedbackDevice shooterPotentiometer;

	// Drive train
	private MullenatorDrive driveTrain;

	// Shooter motors
	private CANTalon leftShooterMotor;
	private CANTalon rightShooterMotor;

	// Auger motors
	private CANTalon augerIntakeMotor;
	private CANTalon augerLifterMotor;

	// Shooter lifter motor
	private DartLinearActuator shooterLifter;

	// Launcher
	private MullenatorShooter launcher;
	
	private ICANMotor leftShooter;
	private ICANMotor rightShooter;
	
	// Auger
	private MullenatorAuger auger;
	
	private DartLinearActuator augerLinearActuator;
	
	// Full mechanism
	private MullenatorMechanism mechanism;
	
	//Lifter
	private MullenatorLifter lift;
	
	//AutoShot
	private AutoShot autoShot;
	
	private ActuatorConfig()
	{
		boolean isAlpha = RobotStatus.isAlpha();
		
		try
		{
			// Instantiate CANTalons
			leftFrontMotor = new CANTalon(MOTOR_LEFT_FRONT);
			leftRearMotor = new CANTalon(MOTOR_LEFT_REAR);
			rightFrontMotor = new CANTalon(MOTOR_RIGHT_FRONT);
			rightRearMotor = new CANTalon(MOTOR_RIGHT_REAR);
		} catch (Exception e)
		{
			System.err.println("Drive Train CANTalons failed to initialize");
		}

		try
		{
			// Instantiate encoders
			leftDriveEncoder = new BuiltInCANTalonEncoder(leftFrontMotor);
			rightDriveEncoder = new BuiltInCANTalonEncoder(rightFrontMotor);
		} catch (Exception e)
		{
			System.err.println("Drive encoders failed to initialize");
		}

		// Instantiate CANMotors
		leftFrontCANMotor = new CANMotor(leftFrontMotor, true, leftDriveEncoder);
		leftRearCANMotor = new CANMotor(leftRearMotor, true);

		rightFrontCANMotor = new CANMotor(rightFrontMotor, false, rightDriveEncoder);
		rightRearCANMotor = new CANMotor(rightRearMotor, false);

		// Create the double motors
		leftDoubleMotor = new DoubleMotor(leftFrontCANMotor, leftRearCANMotor);
		rightDoubleMotor = new DoubleMotor(rightFrontCANMotor, rightRearCANMotor);

		// Create the whole drive train
		driveTrain = new MullenatorDrive(leftDoubleMotor, rightDoubleMotor, SensorConfig.getInstance().getGyro(), SensorConfig.getInstance().getAccelerometer());

		try
		{
			// Instantiate solenoid
			shooterSolenoid = new SingleSolenoid(new Solenoid(SHOOTER_SOLENOID_PORT));
		} catch (Exception e)
		{
			System.err.println("Shooter Solenoid failed to initialize");
		}
		
		try
		{
			shooterPotentiometer = new Potentiometer(new AnalogInput(SHOOTER_POTENTIOMETER_CHANNEL));
			
			// Instantiate shooter motors
			leftShooterMotor = new CANTalon(LEFT_SHOOTER_MOTOR);
			rightShooterMotor = new CANTalon(RIGHT_SHOOTER_MOTOR);

			leftShooter = new CANMotor(leftShooterMotor, false);
			rightShooter = new CANMotor(rightShooterMotor, false);
			
			CANTalon shooterLifterTalon = new CANTalon(LINEAR_ACTUATOR_MOTOR);
			
			// Instantiate shooter lifter
			shooterLifter = new DartLinearActuator(new CANMotor(shooterLifterTalon, true), shooterPotentiometer, false, LOWER_SHOOTER_LIMIT, UPPER_SHOOTER_LIMIT, 200, 3, 0.3);
			
			// Instantiate the shooter
			launcher = new MullenatorShooter(leftShooter, rightShooter, shooterLifter, shooterSolenoid, isAlpha);
		} catch (Exception e)
		{
			System.err.println("Shooter linear actuator failed to initalize");
		}

		try
		{
			//Instantiate the auger potentiometer
			augerPotentiometer = new Potentiometer(new AnalogInput(AUGER_POTENTIOMETER_CHANNEL));
			
			// Instantiate auger motors
			augerIntakeMotor = new CANTalon(AUGER_INTAKE_MOTOR);
			augerLifterMotor = new CANTalon(AUGER_LIFTER_MOTOR);
			
			augerLinearActuator = new DartLinearActuator(new CANMotor(augerLifterMotor, false), augerPotentiometer, true, LOWER_AUGER_LIMIT, UPPER_AUGER_LIMIT, 300, 15, true, 1.2);
			
			auger = new MullenatorAuger(augerLinearActuator, new CANMotor(augerIntakeMotor, false), isAlpha);
		} catch (Exception e)
		{
			System.err.println("Auger failed to initalize");
		}
		
		// Instantiate the lifter
		lift = new MullenatorLifter(new DoubleSolenoid(new edu.wpi.first.wpilibj.DoubleSolenoid(LIFTER_SOLENOID_ONE_PORT_A, LIFTER_SOLENOID_ONE_PORT_B)),
									new DoubleSolenoid(new edu.wpi.first.wpilibj.DoubleSolenoid(LIFTER_SOLENOID_TWO_PORT_A, LIFTER_SOLENOID_TWO_PORT_B)));
		
		// create mechanism
		mechanism = new MullenatorMechanism(auger, lift, launcher);
		
		autoShot = new AutoShot(mechanism, driveTrain);
	}

	public static synchronized ActuatorConfig getInstance()
	{
		if (singleton == null)
		{
			singleton = new ActuatorConfig();
		}

		return singleton;
	}

	public MullenatorMechanism getMechanism()
	{
		return mechanism;
	}

	public MullenatorDrive getDriveTrain()
	{
		return driveTrain;
	}

	public IPIDFeedbackDevice getAugerPotentiometer()
	{
		return augerPotentiometer;
	}

	public IPIDFeedbackDevice getShooterPotentiometer()
	{
		return shooterPotentiometer;
	}

	public AutoShot getAutoShot()
	{
		return autoShot;
	}
	
}
