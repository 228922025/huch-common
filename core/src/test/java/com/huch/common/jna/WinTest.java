package com.huch.common.jna;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.*;
import com.sun.jna.platform.win32.WinUser;
import org.junit.Test;

public class WinTest {
    private User32 user32 = User32.INSTANCE;

    @Test
    public void test1(){
        String winname = "qq";
        HWND hwnd = User32.INSTANCE.FindWindow(null, winname);

        if (hwnd == null){
            System.out.println("Miss!");
            return;
        }

        System.out.println("Hit!");
        boolean showed = User32.INSTANCE.ShowWindow(hwnd, WinUser.SW_RESTORE );
        User32.INSTANCE.CloseWindow(hwnd);
        if (showed){
            System.out.println(showed);
        }
        System.out.println(winname+(showed?"窗口之前可见.":"窗口之前不可见."));
    }

    @Test
    public void test2(){
        HWND hwnd = user32.GetDesktopWindow();
        System.out.println(hwnd);
        hwnd.getPointer();
    }
}
