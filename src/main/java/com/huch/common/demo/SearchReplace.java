package com.huch.common.demo;

import java.io.*;

public class SearchReplace {
    private static void searchAndReplace(String filesPath, String aimStr, String resultStr) {
        File root = new File(filesPath);
        if (null == root.listFiles()) {
            transferFile(root, aimStr, resultStr);
        } else {
            getAllFiles(root, aimStr, resultStr);
        }
        System.out.println("Replace Success!");

    }

    private static void getAllFiles(File root, String aimStr, String resultStr) {
        File[] subsFile = root.listFiles();
        for (int i = 0; i < subsFile.length; i++) {
            if (subsFile[i].isDirectory()) {
                try {
                    getAllFiles(subsFile[i], aimStr, resultStr);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else {
                transferFile(subsFile[i], aimStr, resultStr);
            }
        }
    }

    private static void transferFile(File file, String aimStr, String resultStr) {
        try {
            BufferedInputStream bin = new BufferedInputStream(new FileInputStream(file));
            String fileName = file.getName();
            byte[] buff = new byte[(int) file.length()];
            bin.read(buff);
            FileOutputStream fout = new FileOutputStream(file);
            String str = new String(buff);
            String[] lines = str.split("\n");
            for (String line : lines) {
//   String line_changed = getRepalceResult(fileName,line,aimStr,resultStr);
                String line_changed = getPrefix(line) + line.trim();
                fout.write((line_changed + "\n").getBytes());
            }
            fout.flush();
            fout.close();
            bin.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
//replace search content

    private static String getRepalceResult(String fileName, String resourceStr, String aimStr, String resultStr) {
        int l = 0;
        String gRtnStr = resourceStr;
        do {
            l = resourceStr.indexOf(aimStr, l);
            if (l == -1) break;
            gRtnStr = resourceStr.substring(0, l) + resultStr + resourceStr.substring(l + aimStr.length());
            l += resultStr.length();
            resourceStr = gRtnStr;
            System.out.println("FileName:" + fileName);
        } while (true);
        return gRtnStr.substring(0, gRtnStr.length());
    }

    private static String getPrefix(String str) {
        String prefix = "";
        for (int i = 0; i < str.length(); i++) {
            if (' ' != str.charAt(i)) {
                break;
            } else {
                prefix += str.charAt(i);
            }
        }
        return prefix;
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            searchAndReplace(args[0], args[1], args[2]);
        } else {
            searchAndReplace("D:/temp/demo/infragard.xlsx", "Birmingham", ";");
        }

//        String str ="        I am             a man. ";
//        
//        String prefix = "";
//        
//        for(int i=0;i<str.length();i++){
//            if(' '!=str.charAt(i)){
//                break;
//            }else{
//                prefix += str.charAt(i);
//            }
//            
//            
//        }
//        System.out.println(prefix+".");
    }

}
