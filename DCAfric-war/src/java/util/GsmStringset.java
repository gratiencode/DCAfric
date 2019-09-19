/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author SERVER
 */
public class GsmStringset {

    private static final String hexStrings[] = {"0030", "0031", "0032", "0033", "0034", "0035", "0036", "0037", "0038", "0039", "002A", "0023", "002B", "0020","002F","0046"};
    private static final String ussdStrings[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "*", "#", "+", " ","/","P"};

    public static String stringToGsmEncoded(String clearString) {
        int datalen = clearString.length();
        StringBuilder sb = new StringBuilder();
        List<String> ls = Arrays.asList(ussdStrings);
        for (int i = 0; i < datalen; i++) {
            String c = String.valueOf(clearString.charAt(i)).trim();
            if (ls.contains(String.valueOf(c))) {
                int index = ls.indexOf(c);//existChar(String.valueOf(c), ls)
                if (index==-1) {
                    throw new IllegalArgumentException("Character " + c + " is not allowed");
                } else {
                    sb.append(hexStrings[index]);
                }
            }
        }
        return sb.toString();
    }
    

    public static String gsmToStringDecoded(String gsm) {
        int len = gsm.length();
        int pos = 0;
        StringBuilder sb = new StringBuilder();
        if (len % 4 != 0) {
            throw new IllegalArgumentException("Padding error : impossible de decoder l'argument");
        } else {
            List<String> ls = Arrays.asList(hexStrings);
            for (int i = 1; i <= len; i++) {
                if (i % 4 == 0) {
                    String s = gsm.substring(pos, i);
                    if (ls.contains(s)) {
                        int index = ls.indexOf(s);
                        if (index == -1) {
                            throw new IllegalArgumentException("Charactere entre inavlide verifier s'il sont compatible avec USSD GSM 7 Bit");
                        } else {
                            sb.append(ussdStrings[index]);
                        }
                    }
                    pos = i;
                }
            }
            return sb.toString();
        }

    }

}
