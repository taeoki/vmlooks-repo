package com.ktds.ipc.vmlooks;

import org.testng.annotations.Test;
import org.testng.annotations.Test;
import module.Module;

public class job1_test extends Module {
	
	/*
	 * s01
		-폴더생성( /home/new)
		-패키지파일( 복사 )

		s02
		-패키지 설치
		
		s03
		-패키지 설치 확인
		
		s04
		-interface mtu값 변경
		
		s05 
		-(test용) config파일 생성 
	 */
	@Test
	public void s00_()
	{
		createDirectory("/test");
	}
	@Test
	public void s01_()
	{
		j1_s1();
	}

	@Test
	public void s02_()
	{
		j1_s2();
	}
	
	@Test
	public void s03_()
	{
		j1_s3();
	}
	//@Test
	public void s04_()
	{
		changeMTU("eth0", "1550");
	}
	@Test
	public void s05_()
	{
		j1_s5();
	}
}
