package org.fpsrobotics.teleop;

import java.util.concurrent.Future;

public interface ITeleopTask 
{
	public Future<?> doTask();
}
