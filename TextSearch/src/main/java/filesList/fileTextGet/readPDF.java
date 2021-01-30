package filesList.fileTextGet;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;

public class readPDF {
  public static String getText(String path) {
    /**
     * @param path pdf文件的路径
     * @return pdf中的文本信息，但是由于pdf本身格式的问题，导致文本的顺序可能会错乱。
     */
    String result = "";
    try {
      PDDocument document = PDDocument.load(new File(path));
      PDFTextStripper stripper = new PDFTextStripper();
      stripper.setSortByPosition(true);
      for (int p = 1; p <= document.getNumberOfPages(); ++p) {
        // Set the page interval to extract. If you don't, then all pages would be extracted.
        stripper.setStartPage(p);
        stripper.setEndPage(p);
        // let the magic happen
        String text = stripper.getText(document);
        result += text;
      }
      document.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  public static void main(String[] args) {
    String path="C:\\Users\\zk\\iCloudDrive\\计算机网络\\计算机网络（第7版）-谢希仁.pdf";
    String s=getText(path);
    System.out.println(s);
  }
}
