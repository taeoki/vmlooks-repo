package com.ktds.ipc.vmlooks;

import org.testng.annotations.Test;

import controller.OlvmController;
import module.Module;

public class test extends Module {
	//@Test
	public void s01_tt()
	{
		OlvmController olvmCtrl = new OlvmController();
		olvmCtrl.addNetwork();
	}
	
	//@Test
	public void s00_changeLineInFile()
	{
		changeLineInFile("/tmp/testfile", 
				"#111.111.111.12 ktds.com ktds2", 
				"111.111.111.12 ktds.com ktds2");
	}
	
	//@Test
	public void s01_insertBeforeLineInFile() 
	{
		insertBeforeLineInFile("/tmp/testfile", 
				"111.111.111.12 ktds.com ktds2", 
				"111.111.111.13 ktds.com ktds3");
	}
	
	//@Test
	public void s02_addLineInFile()  
	{
		addLineInFile("/tmp/testfile", 
				"222.222.222.222 line file");
	}
	
	//@Test
	public void s_01_sshkeygen()
	{
		sshKeygen("/tmp/testkey2");
	}
	
	//@Test
	public void s_02_sshcopyid()
	{
		sshCopyId("root", "34.69.212.162", "rhrnakrkawk!23", "/tmp/testkey2");
	}
	//@Test
	public void s0_test()
	{
		String result = inputMsg("df -h /dev/sdc1");
		String temp = result.substring(result.indexOf("/dev/sdc1"), result.length());
		String rtemp = temp.substring(temp.indexOf('%')-2, temp.indexOf('%'));
		rtemp = rtemp.replaceAll(" ", "");
		
		int usage = Integer.parseInt(rtemp);
		System.out.println("[PRINT] /data 사용량 : "+usage);
		
		if(usage >= 2)
		{
			System.out.println("[PRINT] /data 임계치 초과");
			String result2 = inputMsg("cat /dev/null> /data/test01.txt");
			System.out.println("[PRINT] test01.txt 의 내용을 삭제하였습니다");
		}
		else 
		{
			System.out.println("[PRINT] /data 임계치 넘지 않음");
		}
	}
	//@Test
	public void s1_Test ()
	{
		checkDiskUsage();
	}
	
	//@Test
	public void s2_Test()
	{
		checkPing();
	}
	
	//@Test
	public void s3_Test()
	{
		createDirectory("/home/testdir");
	}
	
	
	//@Test
	public void s6_Test()
	{
		checkCpuUsage();
		checkMemoryUsage();
		checkDiskUsage();
		//checkRunningService();
	}
	
	//@Test
	public void ss0002_Test ()
	{
		inputMsg("df -h | grep /dev/sdc1 | awk {print $5}");
	}
	
	@Test
	public void inputCommand()
	{
		inputCommand("dmesg | grep docker");
	}
	
}
