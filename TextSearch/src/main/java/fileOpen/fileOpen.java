package fileOpen;

import java.io.File;
import java.io.IOException;

public class fileOpen {
  public static void openFile(String path){
    try {
        java.awt.Desktop.getDesktop().open(new File(path));
    }catch (IOException e) {
        e.printStackTrace();
      }
  }
  public static void openDir(String path){
    try {
      if(!path.endsWith("\\")){
        path=path.substring(0,path.lastIndexOf("\\"));
      }
      else if(!path.endsWith("/")){
        path=path.substring(0,path.lastIndexOf("/")-1);
      }
      java.awt.Desktop.getDesktop().open(new File(path));
    }catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    String path="D:\\desktop\\test.exe";
    openDir(path);
    System.out.println(path);
  }
}
