package com.ktds.ipc.vmlooks;

import org.testng.annotations.Test;
import module.Module;

public class CheckTotal_Test extends Module {
	
	@Test
	public void test() 
	{
		changeMTU("eth0", "1500");
	}
	//@Test
	public void s01_CheckCPU() 
	{
		checkCpuUsage();
	}
	
	//@Test
	public void s02_CheckMemory() 
	{
		System.out.println("[SystemLog] - Check Memory Usage");
		checkMemoryUsage();
	}
	
	//@Test
	public void  s03_CheckDiskUsage() 
	{
		System.out.println("[SystemLog] - Check Disk Usage");
		checkDiskUsage();
	}
	
	//@Test
	public void s02_CheckRunService() 
	{
		long beforeTime = System.currentTimeMillis();
		
		System.out.println("[SystemLog] - Check Running Service");
		checkRunningService();
		
		long afterTime = System.currentTimeMillis();
		long secDiffTime = (afterTime - beforeTime)/1000;
		System.out.println("[TESTLOG] - 시간차이(m) : " + secDiffTime);
	}
	
	//@Test
	public void s03_CheckWebPage() 
	{
		long beforeTime = System.currentTimeMillis();
		
		System.out.println("[SystemLog] - Check WebPage");
		checkWebPage("https://www.kt.com/");
		
		long afterTime = System.currentTimeMillis();
		long secDiffTime = (afterTime - beforeTime)/1000;
		System.out.println("[TESTLOG] - 시간차이(m) : " + secDiffTime);
	}
}
