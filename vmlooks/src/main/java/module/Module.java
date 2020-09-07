package module;

import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.Iterator;

import controller.Command;
import controller.ResultController;
import controller.StringController;
import controller.WebController;
import dto.HostVM;
import setup.Setup;

public class Module extends Setup {
	
	static Command cmd;
	
	//static StringController resultAnalysis;
	
	//static HashMap<String, HostVM> VMList;
	
	public Module()
	{
		cmd = new Command();
		VMList = new HashMap<String, HostVM>();
		//resultAnalysis = new StringController(VMList);
	}
	
	
	/**
	 * 호스트VM과의 연결 확인 
	 */
	public static void checkPing() 
	{
		String result = RunPlaybook("checkPing.yml");
		
		// 결과값만 출력
		PrintResult(result);
	}
	
	/**
	 * ${basedir}/setting/config.json 에 지정된 서비스의 상태를 확인 
	 * 
	 */
	public static void checkRunningService() 
	{
		String serviceList = (String) getService("String");
		
		/*
		 * --extra-vars '{"service_list": "1","2","3"}'
		 */
		String command = "checkService.yml --extra-vars '{\"service_list\": "+serviceList+"}'";
		String result = RunPlaybook(command);
		assertTrue(checkBuildSuccess(result));
		
		// 결과만 출력
		Print(result);
		
	}
	
	/**
	 * 디스크 사용량을 확인
	 * ${basedir}/setting/config.json 에 임계치 입력 시, 
	 * 디스크 사용 위험 알림 확인할 수 있음 
	 * 
	 */
	public static void checkDiskUsage()
	{
		String result = RunPlaybook("checkDiskUsage.yml");
		assertTrue(checkBuildSuccess(result));
		
		// 결과만 출력
		PrintResult(result);
		
		// 디스크 사용량만 파싱 후, HostVM 에 사용량 정보 저장.
		System.out.println();
		System.out.println();
		saveVMUsageFromResult(result);
		System.out.println();
		
		if(VMList.size() > 0 && getDiskLimit() != null)
		{
			Iterator<String> keys = VMList.keySet().iterator();
			while(keys.hasNext())
			{
				HostVM VM = VMList.get(keys.next());
				VM.checkDisk(getDiskLimit());
			}
		}
		
	}
	
	/**
	 * Cpu 사용량을 확인 
	 * ${basedir}/setting/config.json에 임계치 입력 시, 
	 * Cpu 사용 위험 알림을 확인할 수 있음
	 */
	public static void checkCpuUsage() 
	{
		String result = RunPlaybook("checkCpuUsage.yml");
		assertTrue(checkBuildSuccess(result));
		
		PrintResult(result);
		
		saveVMUsageFromResult(result);
		
		if(VMList.size() > 0 && getCpuLimit() != null)
		{
			Iterator<String> keys = VMList.keySet().iterator();
			while(keys.hasNext())
			{
				HostVM VM = VMList.get(keys.next());
				VM.checkCpu(getCpuLimit());
			}
		}
	}
	
	/**
	 * Memory 사용량을 확인 
	 * ${basedir}/setting/config.json에 임계치 입력 시,
	 * Memory 사용 위험 알림을 확인할 수 있음
	 */
	public static void checkMemoryUsage() 
	{
		String result = RunPlaybook("checkMemoryUsage.yml");
		assertTrue(checkBuildSuccess(result));
		//System.out.println(result);
		
		System.out.println();
		System.out.println();
		saveVMUsageFromResult(result);
		System.out.println();
		
		//System.out.println("checkpoint - VMSIZE" + VMList.size() );
		if(VMList.size() > 0 && getMemoryLimit() != null)
		{
			Iterator<String> keys = VMList.keySet().iterator();
			while(keys.hasNext())
			{
				HostVM VM = VMList.get(keys.next());
				VM.checkMemory(getMemoryLimit());
			}
		}
	}
	
	/**
	 * 
	 * @param url
	 */
	public static void checkWebPage(String url)
	{
		WebController web = new WebController(url);	// 웹 컨트롤러 생성
		
		//web.setURL(url);		// 'url' 페이지의 정보를 받아옴 
		//web.checkConnection();
		//web.quitDriver();
	}
	
	/**
	 * 각 VM으로 Command 를 보내고 결과를 받음.
	 * 
	 * @param command
	 */
	public static void inputCommand(String command)
	{
		/*
		 * command 명령어 예외처리 
		 * rm -rf 
		 * wget
		 */
		String fatalCmd[] = {"rm -f", "rm", "reboot"};
		for(int i=0; i<fatalCmd.length; i++)
		{
			// 금지 단어가 포함되어 있을 때,
			if(command.contains(fatalCmd[i]))
			{
				System.out.println();
				System.out.println("[SystemLog-ERROR] 실행할 수 없는 키워가 포함되어 있습니다.");
				System.out.println("[SystemLog-ERROR] 실행한 명령어: "+command);
				System.out.println("[SystemLog-ERROR] 발견된 키워드: '"+fatalCmd[i]+"'");
				assertTrue(false);
			}
		}
		
		String result = RunPlaybook("inputCommand.yml --extra-vars '{\"command\": \""+command+"\"}'");
		//String result = RunPlaybook("inputCommand.yml");
		assertTrue(checkBuildSuccess(result));
		
		System.out.println();
		System.out.println();
		//Print( getMsgFromResult(result) );
		//System.out.println(result);
		System.out.println(inputMsg(result));
		System.out.println();
	}
// 곧 삭제할거 
	public static String inputMsg(String msg)
	{
		int start = msg.indexOf("TASK [debug]");
		int end = msg.indexOf("PLAY RECAP");
		
		String subResult = msg.substring(start, end);
		return subResult;
	}
	
	public static void runp(String file)
	{
		String result = RunPlaybook(file);
		assertTrue(checkBuildSuccess(result));

		System.out.println();
		System.out.println();
		Print(getMsgFromResult(result));
		System.out.println();
	}
//----------------------------	
	/**
	 * 
	 * @param SELINUX (enforcing, permissive, disabled)
	 * 
	 * @param SELINUXTYPE (targetted, minimum, mls)
	 * 
	 */
	public static void changeSelinuxOption(String SELINUX, String SELINUXTYPE)
	{
		String result = RunPlaybook("changeSelinuxOption.yml --extra-vars \"SELINUX="+SELINUX+" SELINUXTYPE="+SELINUXTYPE+"\"");
		assertTrue(checkBuildSuccess(result));
	}
	
	/**
	 * 
	 * @param interfaceName
	 * interface 이름 
	 * @param i
	 * mtu 값
	 */
	public static void changeMTU(String interfaceName, String value)
	{
		String result = RunPlaybook("changeMTU.yml --extra-vars \"interface="+interfaceName+" value="+value+"\"");
		Print(result);
		assertTrue(checkBuildSuccess(result));
	}
	
	public static void j1_s1()
	{
		String result = RunPlaybook("job1_s1.yml");
		Print(result);
		assertTrue(checkBuildSuccess(result));
	}
	public static void j1_s2()
	{
		String result = RunPlaybook("job1_s2.yml");
		Print(result);
		assertTrue(checkBuildSuccess(result));
	}
	public static void j1_s3() 
	{
		String result = RunPlaybook("job1_s3.yml");
		Print(result);
		assertTrue(checkBuildSuccess(result));
	}
	public static void j1_s5()
	{
		String result = RunPlaybook("job1_s5.yml");
		Print(result);
		assertTrue(checkBuildSuccess(result));
	}
	
	
	/**
	 * Path 에 파일을 생성
	 * @param path
	 */
	public static void createFile(String path)
	{
		String result = RunPlaybook("createFile.yml --extra-vars \"path="+path+"\"");
		Print(result);
		assertTrue(checkBuildSuccess(result));
	}
	
	/**
	 * 
	 * @param path
	 */
	public static void createDirectory(String path)
	{
		String result = RunPlaybook("createDirectory.yml --extra-vars \"path="+path+"\"" );
		Print(result);
		assertTrue(checkBuildSuccess(result));
	}
	
	// change line in file 
	public static void changeLineInFile(String path, String regexp, String line)
	{
		String result = RunPlaybook("changeLineInFile.yml --extra-vars \"path="+path+
				" regexp=\\\""+regexp+"\\\""+
				" line=\\\""+line+"\\\"\"" );
		Print(result);
		assertTrue(checkBuildSuccess(result));
	}
	
	// insert line in file 
	public static void insertBeforeLineInFile(String path, String regexp, String line)
	{
		String result = RunPlaybook("insertBeforeLineInFile.yml --extra-vars \"path="+path+
				" regexp=\\\""+regexp+"\\\""+
				" line=\\\""+line+"\\\"\"" );
		Print(result);
		assertTrue(checkBuildSuccess(result));
	}
	
	// add line a file 
	public static void addLineInFile(String path, String line)
	{
		String result = RunPlaybook("addLineInFile.yml --extra-vars \"path="+path
				+ " line=\\\""+line+"\\\"\"");
		Print(result);
		assertTrue(checkBuildSuccess(result));
				
	}
	/**
	 * 원하는 폴더에 SSK-KEY 생성 ( RSA방식 ) 
	 * @param path
	 */
	public static void sshKeygen(String path)
	{
		String result = RunPlaybook("ssh-keygen.yml --extra-vars \"path="+path+"\"");
		Print(result);
		assertTrue(checkBuildSuccess(result));
	}
	
	public static void sshCopyId(String user, String hostname, String passwd, String pub_path)
	{
		String user_hostname = user+"@"+hostname;
		String result = RunPlaybook("ssh-copy-id.yml --extra-vars \"passwd="+passwd+
				" pub_path="+pub_path+ 
				" user_hostname="+user_hostname+"\"");
	}
	
}
