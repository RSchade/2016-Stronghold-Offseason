package org.fpsrobotics.actuators;

/**
 * This class controls a generic hobby servo, it has options to set it's
 * position from -1.0 to 1.0 as well as a predefined latched and unlatched
 * position.
 *
 */
public class Servo implements IServo
{
	private static double SERVO_LATCHED = 0.6;
	private static double SERVO_UNLATCHED = 1.0;

	private edu.wpi.first.wpilibj.Servo servo;

	public Servo(edu.wpi.first.wpilibj.Servo servo)
	{
		this.servo = servo;
	}

	public Servo(edu.wpi.first.wpilibj.Servo servo, double servo_latched, double servo_unlatched)
	{
		this(servo);
		SERVO_LATCHED = servo_latched;
		SERVO_UNLATCHED = servo_unlatched;
	}

	@Override
	public void engage()
	{
		servo.set(SERVO_LATCHED);
	}

	@Override
	public void disengage()
	{
		servo.set(SERVO_UNLATCHED);
	}

	@Override
	public void set(double value)
	{
		if ((SERVO_LATCHED < value && value < SERVO_UNLATCHED) || (SERVO_UNLATCHED < value && value < SERVO_LATCHED))
		{
			servo.set(value);
		}
	}

	@Override
	public double get()
	{
		return servo.get();
	}

}
