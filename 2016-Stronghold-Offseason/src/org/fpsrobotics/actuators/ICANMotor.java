package org.fpsrobotics.actuators;

import org.fpsrobotics.PID.IPIDEnabledDevice;

/**
 * Describes motors that use the CAN communication interface, specifically the
 * Talon SRX.
 *
 */
public interface ICANMotor extends IMotor, IPIDEnabledDevice
{

}
