package org.fpsrobotics.sensors;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.vision.USBCamera;

public class MicrosoftLifeCam implements ICamera
{
	private CameraServer server;
	private String usbPort;

	public MicrosoftLifeCam(String usbPort)
	{
		server = CameraServer.getInstance();
		server.setQuality(50);
		this.usbPort = usbPort;
	}

	@Override
	public void enable()
	{
		server.startAutomaticCapture(usbPort);
	}

	@Override
	public void disable()
	{

	}

}
