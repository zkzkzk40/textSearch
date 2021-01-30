package filesList.fileTextGet;

import java.io.*;

public class ReadTXT {


  public static String getTXT(String path){
    String result="";
    try {
      FileInputStream fileInputStream=new FileInputStream(path);
      InputStreamReader inputStreamReader=new InputStreamReader(fileInputStream,"gb2312");
      BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
      while(bufferedReader.ready()){
        result=result+bufferedReader.readLine()+"\n";
      }
    } catch (FileNotFoundException | UnsupportedEncodingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return result;
  }

  public static void main(String[] args) {
    String path="D:\\desktop\\test\\四书五经\\说明.txt",
            text=getTXT(path);
    System.out.println(text);

  }
}
