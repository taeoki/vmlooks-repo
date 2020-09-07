package com.ktds.ipc.vmlooks;

import org.testng.annotations.Test;
import module.Module;

public class CheckResource_Test extends Module {

	@Test
	public void s01_CheckCPU()
	{
		System.out.println("[SystemLog] - Check Cpu Usage");
		checkCpuUsage();
	}
	@Test
	public void CheckMem()
	{
		System.out.println("[SystemLog] - Check Memory Usage");
		checkMemoryUsage();
	}
	@Test
	public void CheckDisk()
	{
		System.out.println("[SystemLog] - Check Disk Usage");
		checkDiskUsage();
	}
}
