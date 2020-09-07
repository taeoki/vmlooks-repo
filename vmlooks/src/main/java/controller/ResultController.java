package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;

import static org.testng.Assert.assertTrue;

import dto.HostVM;

public class ResultController {
	static HashMap<String, HostVM> VMList;
	
	public ResultController(HashMap<String, HostVM> VMList) 
	{
		this.VMList = VMList;
	}
	
	
	public String parseResult(String result)
	{
		StringBuffer buffer;
		String returnString = "";
		if (result.length() > 0)
		{
			buffer = new StringBuffer(result);
			int start = 0;
			int end = 0;
			// 결과 값 subString
			start = buffer.indexOf("startFile")+"startFile".length();
			end = buffer.indexOf("endFile");
			
			returnString = buffer.substring(start,end);
			
			//System.out.println("checkpoint");
			System.out.println("[Console-Print] ------------------------------------------------------ ");
			System.out.println(returnString);
			System.out.println("------------------------------------------------------------------[END]");

		}
		else 
		{
			assertTrue(false, "[Console-Error] ResultCtrl-parseResult ");
		}
		return returnString;
	}
	
	public static boolean checkBuildSuccess(String result)
	{
		boolean returnValue = true;
		StringBuffer buffer;
		if(result.length() > 0 )
		{
			buffer = new StringBuffer(result);
			
			int start = buffer.indexOf("PLAY RECAP");
			int end = buffer.length()-2;
			
			String parsingResult = buffer.substring(start,end);
			
			BufferedReader br = new BufferedReader(new StringReader(parsingResult));
			
			String line = "";
			String host = null;
			
			try 
			{
				br.readLine();
				while( (line = br.readLine()) != null )
				{
					// 불필요한 띄어쓰기 제거 
					line = line.replaceAll(":", "");
					line = line.replaceAll("( )+", " ");
					
					String temp[] = line.split(" ");
					/* index
					 * 0-ip
					 * 1-ok
					 * 2-unreachable
					 * 3-failed
					 * 4-skipped
					 * 5-rescued
					 * 6-ignored
					 */
					
					// 플레이북 실행에 문제가 발생했을 때,  
					if (temp[3].compareTo("unreachable=0") != 0 || 
							temp[4].compareTo("failed=0") != 0)
					{	
						returnValue = false;
						System.out.println("[Console-Error]"+temp[0]+"에서 문제가 발생했습니다.");
						System.out.println("> "+line);
					}
				}
			} catch (IOException e) {
				System.out.println("[Console-Error](checkBuildSucess) 결과를 읽는 과정에서 문제가 발생했습니다. ");
				e.printStackTrace();
				assertTrue(false);
			}
			assertTrue(returnValue);
			return returnValue;
		}
		return false;
	}
	
	public static void saveVMUsageFromResult(String result)
	{
		if (result.length() > 0)
		{
			StringBuffer buffer = new StringBuffer(result);
			
			// 결과 값만 subString
			int start = buffer.indexOf("TASK [debug]");
			int end = buffer.indexOf("PLAY RECAP");
			String resultSubstring = buffer.substring(start,end-2);
			
			BufferedReader br = new BufferedReader(new StringReader(resultSubstring));
			
			String line = "";
			String ip = null;
			String msg = null;
			String tag = null;
			
			// 결과값에서 ip 정보와 msg(결과) 정보를 subString
			try {
				while( ( line=br.readLine()) != null ) 
				{
					if( line.contains("ok") )
					{
						//split index
						// 0 - ok
						// 1 - [ip]
						// 2 - =>
						// 3 - {
						String temp[] = line.split(" ");
						ip = temp[1].substring(1, temp[1].length()-1);
					}
					else if ( line.contains("msg") )
					{
						line.replaceAll("\"", "");
						//split index 
						// 0 - msg: 
						// 1 - $tag - ["CpuUsage", "DiskUsage", "MemoryUsage"]
						// 2 - ${msg}
						String temp[] = line.trim().split(" ");
						tag = temp[1];
						msg = temp[2].trim().replaceAll("\"", "");
					}
					
					if ( ip != null && msg != null )
					{
						//System.out.println("CheckPoint");
						//System.out.println("ip:"+ip);
						//System.out.println("tag: "+tag);
						//System.out.println("msg: "+msg);
						
						// Host의 정보를 처음 입력하는 것일 때, 
						// HostVM 객체를 하나 생성 
						//System.out.println(VMList.get(ip));
						if(VMList.get(ip) == null)
						{
							//System.out.println("checkpoint - 1");
							VMList.put(ip, new HostVM());
						}
						
						// 특정 IP의 HostVM 객체정보를 불러옴 
						HostVM VM = VMList.get(ip);
						
						// tag에 맞는 데이터를 HostVM객체에 입력
						VM.setName(ip);
						if (tag.contains("CpuUsage"))
						{
							VM.setCpuUsage(msg);
						}
						else if (tag.contains("MemoryUsage"))
						{
							VM.setMemUsage(msg);
						}
						else if (tag.contains("DiskUsage"))
						{
							VM.setdiskUsage(msg);
						}
						else if (tag == null)
						{
							assertTrue(false, "[SystemLog-ERROR](StringController) 데이터를 읽지 못했습니다.");
						}
						ip = null;
						msg = null;
						tag = null;
						//System.out.println("[SystemLog] VMSize : "+VMList.size());
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
