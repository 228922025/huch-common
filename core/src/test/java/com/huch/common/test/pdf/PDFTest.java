package com.huch.common.test.pdf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huch.common.io.FileUtil;
import com.huch.common.util.StrUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.Test;
import technology.tabula.CommandLineApp;

import javax.xml.soap.Text;
import java.io.File;
import java.util.*;

public class PDFTest {

    /**
     * 读取pdf中文字信息(全部)
     */
    @Test
    public void READPDF() {
        String inputFile = "D:\\work\\other\\C04_01004227_101039960_dp2023.pdf";
        //创建文档对象
        PDDocument doc = null;
        String content = "";
        try {
            //加载一个pdf对象
            doc = PDDocument.load(new File(inputFile));
            //获取一个PDFTextStripper文本剥离对象
            PDFTextStripper textStripper = new PDFTextStripper();
            content = textStripper.getText(doc);
            // vo.setContent(content);
            System.out.println("内容:" + content);
            System.out.println("全部页数" + doc.getNumberOfPages());
            //关闭文档
            doc.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    @Test
    public void test1() {
        String inputFile = "D:\\work\\other\\C04_01004227_101039960_dp2023.pdf";
        String[] args = new String[]{"-f=JSON", "-o=d:/output.txt", "-p=all", inputFile};
        CommandLineApp.main(args);
    }

    @Test
    public void paseTest() {
        String inputFile = "D:\\work\\other\\output.txt";
        String content = FileUtil.readFileContent(inputFile);
        JSONArray jsonArray = JSON.parseArray(content);
        int num = 1;
        List<VO> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            JSONArray data = jsonObject.getJSONArray("data");
            for (int j = 0; j < data.size(); j++) {
                JSONArray array = data.getJSONArray(j);

                JSONObject json = array.getJSONObject(0);
                String text1 = json.getString("text");
                String[] arr1 = text1.split(" ");
                if(!arr1[0].equals("") && StringUtils.isNumeric(arr1[0])){
                    String money = array.getJSONObject(1).getString("text");
                    String text3 = array.getJSONObject(2).getString("text");
                    String text4 = array.getJSONObject(3).getString("text");
                    String remark = array.getJSONObject(4).getString("text");
                    String[] arr3 = text3.split(" ");
                    String[] arr4 = text4.split(" ");
                    LinkedList<String> list3 = new LinkedList<>(Arrays.asList(arr3));
                    LinkedList<String> list4 = new LinkedList<>(Arrays.asList(arr4));
                    if(arr3.length == 4){
                        list4.offerFirst(arr3[3]);
                    }
                    if(list4.size() == 1){
                        list4.offerFirst("");
                        list4.offerFirst("");
                    }

                    VO vo = new VO();
                    vo.num = arr1[0];
                    vo.date = arr1[1];
                    vo.money = money;

                    vo.money2 = arr3[0];
                    vo.note = arr3[1];
                    vo.account = arr3.length >= 3 ? arr3[2]: "";

                    vo.name = list4.get(0);
                    vo.bankName = list4.size() >= 2 ? list4.get(1): "";
                    vo.serialNum = list4.size() >= 3 ? list4.get(2): "";

                    vo.remark = remark;

                    list.add(vo);


                }
            }
        }
        //
        for (VO vo : list) {
            System.out.println(StrUtil.format("{}\t{}\t{}\t{}\t{}\t{}\t{}\t{}\t{}\t{}", vo.num, vo.date, vo.money, vo.money2,vo.note, vo.account, vo.name, vo.bankName, vo.serialNum, vo.remark));
        }
    }

    public class VO{
        private String num;
        private String date;
        private String money;
        private String money2;
        private String note;
        private String account;
        private String name;
        private String bankName;
        private String serialNum;
        private String remark;

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getMoney2() {
            return money2;
        }

        public void setMoney2(String money2) {
            this.money2 = money2;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public String getSerialNum() {
            return serialNum;
        }

        public void setSerialNum(String serialNum) {
            this.serialNum = serialNum;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }

}
