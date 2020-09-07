package com.ktds.ipc.vmlooks;

import org.testng.annotations.Test;
import module.Module;

public class CheckPing_Test extends Module {
	
	@Test
	public void PingTest() 
	{
		System.out.println();
		System.out.println("[RESULT] ------------------------------------------------------------------------");
		System.out.println("[SystemLog] - PING CHECK TEST");
		checkPing();
	}
}
