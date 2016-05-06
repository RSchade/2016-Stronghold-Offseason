package org.fpsrobotics.sensors;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class VisionCenterTarget implements IVision
{
	private CameraServer server = CameraServer.getInstance();
	private NetworkTable table;

	public VisionCenterTarget()
	{

	}

	double[] centerYs, centerXs;
	double[] defaultValue = new double[0];

	@Override
	public void runSequence()
	{
		table = NetworkTable.getTable("GRIP/goalReport");

		centerYs = table.getNumberArray("centerY", defaultValue);
		centerXs = table.getNumberArray("centerX", defaultValue);

		System.out.println("centerX: " + centerXs[0]);
		/*
		 * for(double centerX: centerXs) { System.out.println(centerX + " "); }
		 */

		System.out.println("centerY: " + centerYs[0]);
		/*
		 * for(double centerY: centerYs) { System.out.println(centerY + " "); }
		 */

		/*
		 * if(centerXs[0] > 320) { SmartDashboard.putString("turn", "right"); }
		 * else if(centerYs[0] < 320) { SmartDashboard.putString("turn",
		 * "left"); } else { SmartDashboard.putString("turn", "none"); }
		 */

		SensorConfig.getInstance().getTimer().waitTimeInMillis(500);

	}

}
