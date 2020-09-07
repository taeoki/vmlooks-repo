package com.ktds.ipc.vmlooks;

import org.testng.annotations.Test;

import module.Module;

public class CheckCpu_Test extends Module {
	
	@Test
	public void CheckCpu()
	{
		System.out.println();
		System.out.println("[RESULT] ------------------------------------------------------------------------");
		System.out.println("[SystemLog] - Check Cpu Usage");
		checkCpuUsage();
		System.out.println();
	}
}
