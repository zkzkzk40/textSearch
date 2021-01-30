package filesList.fileTextGet;


import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.hslf.usermodel.HSLFTextShape;
import org.apache.poi.sl.usermodel.Slide;
import org.apache.poi.sl.usermodel.SlideShow;
import org.apache.poi.xslf.usermodel.*;

import java.io.*;
import java.util.List;

public class readPPT {
  public static String getText(String path){
    File file=new File(path);
    StringBuilder content=new StringBuilder();

    try {
      SlideShow slideShow=null;
      InputStream is=new FileInputStream(file);
      if(path.endsWith(".ppt")) {
        slideShow=new HSLFSlideShow(is);
      }
      else if(path.endsWith(".pptx")){
        slideShow=new XMLSlideShow(is);
      }
      if(slideShow==null) {
        return "";
      }
      for(Slide slide: (List<Slide>) slideShow.getSlides()){
        List shapes=slide.getShapes();
        if(shapes!=null){
          for(Object shape:shapes){

            if(shape==null) {
              continue;
            }
            if (shape instanceof HSLFTextShape) {// 文本框
              String text = ((HSLFTextShape) shape).getText();

              content.append(text);
            }
            if (shape instanceof XSLFTextShape) {// 文本框
              String text = ((XSLFTextShape) shape).getText();

              content.append(text);
            }
          }
        }
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return content.toString();
  }

  public static void main(String[] args) {
    //String t=getText("src/main/resources/6.1  6.2  6.3.ppt");
    String t=getText("src/main/resources/09图形用户界面编程.pptx");
    System.out.println(t);
    t=getText("src/main/resources/6.1  6.2  6.3.ppt");
    System.out.println(t);
  }
}