package controller;

import java.util.List;
import java.util.Queue;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.ovirt.engine.sdk4.types.Host;
import org.ovirt.engine.sdk4.types.Vm; 

public class ExcelController {
           String FileName;
           HSSFWorkbook wb;
           HSSFSheet sheet;
           Row row;
           Cell cell;

           static XSSFWorkbook xssfWb;
           static XSSFSheet xssfSheet;
           static XSSFRow xssfRow;
           static XSSFCell xssfCell;
           static int rowNo ;
          
           public CellStyle cellStyle_Body;
        
           public ExcelController()
           {
                     wb = null;
                     sheet = null;
                     row = null;
                     cell = null;
                  
                     xssfWb = null;
                     xssfSheet =null;
                     xssfRow=null;
                     xssfCell=null;
                    
                     rowNo=0;
                     
                     // 기본적으로 생성되어야할 것들 ( 워크시트 , 타이틀 제목 )
                     xssfWb = new XSSFWorkbook();
                     xssfSheet = xssfWb.createSheet("worksheetName");     // 워크시트 이름

                     createTitle("Excel Test file");
                     createBody();
           }

           public void addHostListData(List<Host> hosts)
           {                    
        	   for(Host host : hosts)
        	   {
        		   xssfRow = xssfSheet.createRow(rowNo++);
        		   
        		   xssfCell = xssfRow.createCell((short)0);
        		   xssfCell.setCellStyle(cellStyle_Body);
        		   xssfCell.setCellValue(host.name());
        	   }
           }
           
           public void addVmListData(List<Vm> vms)
           {
        	   for(Vm vm :vms)
        	   {
        		   xssfRow = xssfSheet.createRow(rowNo++);
        		   
        		   xssfCell = xssfRow.createCell((short)0);
        		   xssfCell.setCellStyle(cellStyle_Body);
        		   xssfCell.setCellValue(vm.name());
        		   
        		   xssfCell = xssfRow.createCell((short)1);
        		   xssfCell.setCellStyle(cellStyle_Body);
        		   xssfCell.setCellValue(vm.id());
        		   
        		   xssfCell = xssfRow.createCell((short)2);
        		   xssfCell.setCellStyle(cellStyle_Body);
        		   xssfCell.setCellValue(vm.host().name());
        	   }
           }

           public void createRowTitle(Queue<String> columnTitle)
           {
        	   xssfRow = xssfSheet.createRow(rowNo++);
           }
           
           public void createTitle(String TitleName)
           {
                     CellStyle cellStyle_Title = xssfWb.createCellStyle();
                     xssfSheet.setColumnWidth(3, (xssfSheet.getColumnWidth(3))+(short)2048);    //3번째 컬럼 넓이 조절
                     xssfSheet.setColumnWidth(3, (xssfSheet.getColumnWidth(4))+(short)2048);
                     xssfSheet.setColumnWidth(3, (xssfSheet.getColumnWidth(5))+(short)2048);
                     xssfSheet.setColumnWidth(3, (xssfSheet.getColumnWidth(8))+(short)4096);
                    
                     //cellStyle_Title.setFont(font);   // cellStyle에 font를 적용
                     cellStyle_Title.setAlignment(HorizontalAlignment.CENTER);         // 정렬
                    
                     // 셀 병합
                     xssfSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));         //(첫행, 마지막행, 첫열, 마지막열)병합            

                     // 타이틀 생성
                     xssfRow = xssfSheet.createRow(rowNo++);                //행 객체 추가
                     xssfCell = xssfRow.createCell((short)0);          //추가한 행에 셀 객체 추가
                     xssfCell.setCellStyle(cellStyle_Title);    //셀에 스타일 지정
                     xssfCell.setCellValue(TitleName);                    //데이터 입력
                    
                     xssfRow = xssfSheet.createRow(rowNo++);     // 빈행 추가
           }          

           public void createBody()
           {
                     CellStyle cellStyle_Body = xssfWb.createCellStyle();
                     cellStyle_Body.setBorderTop(BorderStyle.THIN);                     //테두리 위쪽
                     cellStyle_Body.setBorderLeft(BorderStyle.THIN);          //테두리 왼쪽
                     cellStyle_Body.setBorderRight(BorderStyle.THIN);        //테두리 오른쪽
                     cellStyle_Body.setBorderBottom(BorderStyle.THIN);      //테두리 아래쪽
                     cellStyle_Body.setAlignment(HorizontalAlignment.CENTER);
           }
           public void saveExcelFile()
           {
                    

           }
 }