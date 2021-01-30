package filesList.indexGet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Queue;

public class fileList {
  String path;
  ArrayList files=new ArrayList<String>();
  public fileList(String Path)throws IOException{
    path=Path;
    files.add(path);
    getFileList(new File(path));
  }
  public ArrayList<String> getFiles(){
    return files;
  }
  private void getFileList(File f) throws IOException {
    String[] file_list=f.list();
    try{
      for(int i=0;i<file_list.length;i++){
        File cf=new File(f.getPath(),file_list[i]);
        if(cf.isDirectory()) {
          //files.add(cf.getCanonicalPath());
          //System.out.println(cf.getCanonicalPath());
          getFileList(cf);
        }
        if(cf.isFile()){
          files.add(cf.getCanonicalPath());
          //System.out.println(cf.getCanonicalPath());
        }
      }
    }catch(NullPointerException e){//some file cannot read
      System.out.println("Cannot read "+f.getCanonicalPath());
    }
  }

  public static void main(String[] args) {
    try {
      fileList f=new fileList("D:\\desktop");
      ArrayList<String> myfiles=f.getFiles();
     for(String s:myfiles){
       System.out.println(s);
     }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
