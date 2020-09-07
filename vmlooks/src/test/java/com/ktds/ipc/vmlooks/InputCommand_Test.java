package com.ktds.ipc.vmlooks;

import org.testng.annotations.Test;
import module.Module;

public class InputCommand_Test extends Module {

	@Test
	/**
	 * InputCommand 
	 * 
	 */
	public void InputCommand() 
	{
		System.out.println("[SystemLog] - InputComand");
		//example
		inputCommand("cal");
		//inputCommand("rm -f test.txt");
	}
}
