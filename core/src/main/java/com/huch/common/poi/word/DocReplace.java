package com.huch.common.poi.word;

import com.huch.common.io.FileUtil;
import com.huch.common.util.StrUtil;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author huchanghua
 * @create 2019-11-26-01:43
 */
public class DocReplace {

    public static void replaceDoc(String path, Map<String, String> map){
        File file = new File(path);
        String str = "";
        try {
            FileInputStream fis = new FileInputStream(file);
            HWPFDocument doc = new HWPFDocument(fis);
            Range range = doc.getRange();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                range.replaceText(entry.getKey(), entry.getValue());
            }

            fis.close();

            OutputStream os = new FileOutputStream(path);
            doc.write(os);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void replaceDocx(String path, Map<String, String> map){
        File file = new File(path);
        String str = "";
        try {
            FileInputStream fis = new FileInputStream(file);
            XWPFDocument docx = new XWPFDocument(fis);
            XWPFWordExtractor extractor = new XWPFWordExtractor(docx);
            String doc1 = extractor.getText();
            System.out.println(doc1);
            fis.close();


            Iterator<XWPFParagraph> iter = docx.getParagraphsIterator();
            while (iter.hasNext()) {
                XWPFParagraph paragraph = iter.next();
                List<XWPFRun> runs = paragraph.getRuns();
                for (int i = 0; i < runs.size(); i++) {
                    String oneparaString = runs.get(i).getText(runs.get(i).getTextPosition());
                    if (oneparaString != null) {
                        for (Map.Entry<String, String> entry : map.entrySet()) {
                            oneparaString = oneparaString.replace(entry.getKey(), entry.getValue());
                        }
                        runs.get(i).setText(oneparaString, 0);
                    }
                }
            }

            // 替换表格中的指定文字
            Iterator<XWPFTable> itTable = docx.getTablesIterator();
            while (itTable.hasNext()) {
                XWPFTable table = itTable.next();
                int rcount = table.getNumberOfRows();
                for (int i = 0; i < rcount; i++) {
                    XWPFTableRow row = table.getRow(i);
                    List<XWPFTableCell> cells = row.getTableCells();
                    for (XWPFTableCell cell : cells) {
                        String cellTextString = cell.getText();
                        for (Map.Entry<String, String> e : map.entrySet()) {
                            if (cellTextString.contains(e.getKey()))
                                cellTextString = cellTextString.replace(e.getKey(),e.getValue());
                        }
                        cell.removeParagraph(0);
                        cell.setText(cellTextString);
                    }
                }
            }
            FileOutputStream outStream = null;
            outStream = new FileOutputStream(path);
            docx.write(outStream);
            outStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        map.put("三鸟", "毛毛");
        List<File> files = FileUtil.loopFiles(new File("/Users/huchanghua/Documents/000_temp/"));
        for (File file: files){
            String fileName = file.getName();
            if(StrUtil.endWith(fileName, ".doc")){
                // replaceDoc();
            }
            else if(StrUtil.endWith(fileName, ".docx")){

            }
        }
    }
}
