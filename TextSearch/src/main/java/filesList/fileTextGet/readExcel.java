package filesList.fileTextGet;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class readExcel {

  public static String getText(String path){
    String result="";
    File file;
    try {
      FileInputStream fileInputStream=new FileInputStream(path);
      Workbook workbook;
      if(path.endsWith(".xlsx")) {
        workbook=new XSSFWorkbook(fileInputStream);
      }
      else if(path.endsWith(".xls")) {
        workbook=new HSSFWorkbook(fileInputStream);
      }
      else {
        return "";
      }
      for(Sheet sheet:workbook){
        for(Row row:sheet){
          for(Cell cell:row){
            if(cell!=null){
              DataFormatter formatter=new DataFormatter();
              result=result+formatter.formatCellValue(cell)+"\t";
            }
            else{
              result=result+"\t\t";
            }
          }
          result=result+"\n";
        }
      }
      fileInputStream.close();
      return result;
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }catch (IOException e) {
      e.printStackTrace();
    }
    return result;

  }
  /**
  *
  *测试长度
  *
  *  */
  private static int getExcelLineNumber(String path){
    int result=0;
    File file;
    try {
      FileInputStream fileInputStream=new FileInputStream(path);
      Workbook workbook;
      if(path.endsWith(".xlsx")) {
        workbook=new XSSFWorkbook(fileInputStream);
      }
      else if(path.endsWith(".xls")) {
        workbook=new HSSFWorkbook(fileInputStream);
      }
      else {
        return 0;
      }
      for(Sheet sheet:workbook){
        int end=sheet.getLastRowNum();
        System.out.println(end);
      }
      fileInputStream.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }catch (IOException e) {
      e.printStackTrace();
    }
    return result;
  }
  public static void main(String[] args) {
    String path="src/main/resources/大二排考表.xlsx";

    String text=getText(path);
    //getExcelLineNumber(path);
    System.out.println(text);
  }
}
