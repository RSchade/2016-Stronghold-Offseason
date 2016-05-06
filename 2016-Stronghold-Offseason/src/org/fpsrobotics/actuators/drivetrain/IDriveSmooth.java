package org.fpsrobotics.actuators.drivetrain;

public interface IDriveSmooth
{
	public void driveAtProfile(double[] profileSetting, DriveTrainProfile profile);
	public void stopAtProfile(DriveTrainProfile profile);
	public void setProfile(DriveTrainProfile profile);
	public void driveAtSpecifiedProfile(double[] profileSetting);
	public DriveTrainProfile getDriveProfile();
}
