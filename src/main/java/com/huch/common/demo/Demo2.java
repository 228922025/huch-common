package com.huch.common.demo;
import com.spire.doc.*;

public class Demo2 {


        public static void main(String[] args){

            //加载测试文档
            Document doc = new Document("test.docx");
            //如只需替换文档中的第一个指定文本，替换前先调用以下方法，再进行下一步代码
            doc.setReplaceFirst(true);
            //如需替换文档中的所有指定文本，可省略上一步代码，直接调用以下方法
            doc.replace("员工","公司职员",false,true);
            //保存文档
            doc.saveToFile("result.docx",FileFormat.Docx_2010);

        }

}
