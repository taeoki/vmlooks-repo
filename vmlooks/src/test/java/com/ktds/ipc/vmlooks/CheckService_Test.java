package com.ktds.ipc.vmlooks;

import org.testng.annotations.Test;
import module.Module;

public class CheckService_Test extends Module {

	@Test
	public void CheckService_Test () 
	{
		long beforeTime = System.currentTimeMillis();
		
		System.out.println("[SystemLog] - Check Run Service");
		
		checkRunningService();
		
		long afterTime = System.currentTimeMillis();
		long secDiffTime = (afterTime - beforeTime)/1000;
		System.out.println("[SystemLog] - Job 시작시간: "+beforeTime);
		System.out.println("[SystemLog] - Job 종료시간: "+afterTime);
		System.out.println("[TESTLOG] - (종료시간-시작시간) = 시간차이(m) : " + secDiffTime);
		
	}
}
