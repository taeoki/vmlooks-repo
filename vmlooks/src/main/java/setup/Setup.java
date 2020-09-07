package setup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import controller.Command;
import controller.ResultController;
import controller.StringController;
import dto.HostVM;



public class Setup {

	StringController stringController;
	public static HashMap<String, HostVM> VMList;
	
	private static String currentPath;
	private static String playbookPath;
	private static String hostsPath;
	private static Command cmd;
	private static ResultController resultCtrl;
	
	/*
	 * config.json의 설정값들이 담길 변수
	 */
	static String username;
	static String passwd;
	static long timeout;
	static long diskUsageLimit;
	static long cpuUsageLimit;
	static long memoryUsageLimit;
	static ArrayList<String> serviceList;			// 확인해야할 서비스 리스트 
	static String ServiceList_String;
	static ArrayList<String> arrangeFolderList;		// 정리해야할 폴더 리스트 
	static ArrayList<String> triggerList;			// 확인해야할 시스템 로그
	static String backupFolder;						// 백업본이 저장될 변수 
	
	
	public Setup()
	{
		init();
		readConfiguration();
	}
	
	/**
	 * 현재 파일 경로와 플레이북 파일 경로 정
	 */
	public void init()
	{
		/*
		 * 빌드되고 있는 프로젝트의 현재위치 알아오기 
		 */
		File file = new File("");
		currentPath = file.getAbsolutePath();
		hostsPath = currentPath + "/setting/";
		playbookPath = currentPath + "/playbook/";
		
		/*
		 * 클래스 초기화
		 */
		cmd = new Command();
		
		VMList = new HashMap<String, HostVM>();
		resultCtrl = new ResultController(VMList);
		stringController = new StringController();
	}
	/**
	 * ./setting/config.json 파일로 부터 설정 값을 읽어
	 */
	public static void readConfiguration()
	{
		JSONParser parser = new JSONParser();

		try {
			/*
			 * ../setting/config.json 에서 설정값을 불러옴 
			 */
			Object obj = parser.parse(new FileReader(getCurrentPath()+"/setting/config.json"));
			JSONObject jsonObject = (JSONObject) obj;
			
			username = (String) jsonObject.get("Username");
			passwd = (String) jsonObject.get("Password");
			timeout = (Long)jsonObject.get("Timeout");
			diskUsageLimit = (Long) jsonObject.get("DiskUsageLimit");
			cpuUsageLimit = (Long) jsonObject.get("CpuUsageLimit");
			memoryUsageLimit = (Long) jsonObject.get("MemoryUsageLimit");
			// arrangeFolderList 
			arrangeFolderList = new ArrayList<String>();
			JSONArray folder = (JSONArray) jsonObject.get("TargetFolder");
			Iterator<String> iter = folder.iterator();
			while ( iter.hasNext() )
			{
				arrangeFolderList.add(iter.next());
			}
			
			// Array ServiceList
			serviceList = new ArrayList<String>();
			JSONArray service = (JSONArray) jsonObject.get("Service");
			iter = service.iterator();
			ServiceList_String = service.toString();
			while ( iter.hasNext() )
			{
				serviceList.add(iter.next());
			}
			
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("[SystemError] '../setting/config.json' 파일이 존재하지 않습니다.");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * ${workspace}/src/playbook에 있는 플레이북을 실행시키고 
	 * 결과값을 리턴함
	 * @param playbookName
	 * @return
	 */
	public static String RunPlaybook(String playbookName)
	{
		String command = "ansible-playbook -i "+hostsPath+"hosts"+" "+getPlaybookPath()+playbookName;
		//System.out.println(command);
		String result = cmd.execute(command);
		return result;
	}
	public static String RunPlaybook(String inventory, String playbook)
	{
		String command = "ansible-playbook -i "+hostsPath+inventory +" "+getPlaybookPath()+playbook;
		String result = cmd.execute(command);
		return result;	
	}
	
	public static String getCurrentPath()
	{
		return currentPath;
	}
	public static String getPlaybookPath() 
	{
		return playbookPath;
	}
	
	public static Object getService(String returnValue)
	{
		if( returnValue.contains("String") )
			return ServiceList_String;
		else if( returnValue.contains("ArrayList"))
			return serviceList;
		
		return null;
	}
	
	public static Long getCpuLimit()
	{
		return cpuUsageLimit;
	}
	
	public static Long getMemoryLimit()
	{
		return memoryUsageLimit;
	}
	public static Long getDiskLimit()
	{
		return diskUsageLimit;
	}
	
	/**
	 * 콘솔에 출력
	 * @param str
	 */
	public static void Print(String str)
	{
		System.out.println();
		System.out.print("[Console-Print] "+str+"\n");
		System.out.println();
	}
	
	
	
	public static void PrintResult(String result)
	{
		System.out.println();
		System.out.print("[Console-Print] "+getMsgFromResult(result)+"\n");
	}
	
	/**
	 * ansible 결과 값에서 'Msg'항목만 출력
	 * @param result
	 * @return
	 */
	public static String getMsgFromResult(String result)
	{
		String msg = resultCtrl.parseResult(result);
		return msg;
	}
	
	/**
	 * ansible 배포에서 문제되는 결과 확인
	 * @param result
	 * @return
	 */
	public static boolean checkBuildSuccess(String result)
	{
		return resultCtrl.checkBuildSuccess(result);
	}
	
	public static void saveVMUsageFromResult(String result)
	{
		resultCtrl.saveVMUsageFromResult(result);
	}
	
	
}
