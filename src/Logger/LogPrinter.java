/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logger;

/**
 *
 * @author Srikanth
 */
public class LogPrinter {
    public static boolean print = true;
    public static void printLog(String message) {
        if(print)
        {
            System.out.println(message);
        }
        }
    }

