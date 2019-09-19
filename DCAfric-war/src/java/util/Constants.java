/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 *
 * @author PC
 */
public interface Constants {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final String SERVICE_STATUS_TERMINATED = "Términé";
    public static final String SERVICE_STATUS_CANCELED = "Annulé";
    public static final String SERVICE_STATUS_IN_PROGRESS = "En cours...";
    public static final String SERVICE_STATUS_STARTED = "Lancé";
    public static final String AIRTEL = "AIRTEL";
    public static final String NEW_CLIENT = "NEW_CLIENT";
    public static final String ELIGIBLE_CLIENT = "ELIGIBLE";
    public static final String NON_ELIGIBLE_CLIENT = "NON_ELIGIBLE";
    public static final String ORANGE = "ORANGE";
    public static final String VODACOM = "VODACOM";
    public static final String UNITE = "UNITE";
    public static final String MEGA = "MEGA";
    public static final int TIMEOUT_USSD = 1000;
    public static final String GENERAL_ACCOUNT_KIOSQUE = "445A412C901";

    public static class Datetime {

        public static Date todayTime() {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd à HH:mm:ss");
            LocalDateTime d1 = LocalDateTime.now();
            d1.format(df);
            Date d = java.util.Date.from(d1.atZone(ZoneId.systemDefault()).toInstant());
            return d;
        }
    }

}
