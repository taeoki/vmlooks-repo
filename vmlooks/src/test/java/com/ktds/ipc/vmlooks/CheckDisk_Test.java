package com.ktds.ipc.vmlooks;

import org.testng.annotations.Test;
import module.Module;

public class CheckDisk_Test extends Module {
	
	@Test
	public void CheckDisk () 
	{
		System.out.println("[SystemLog] - Check Disk Usage");
		checkDiskUsage();
	}
}
