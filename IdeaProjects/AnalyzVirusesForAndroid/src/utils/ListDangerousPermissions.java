package utils;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by alex on 19.01.16.
 */
public class ListDangerousPermissions {
    public  static final String READ_PHONE_STATE = "android.permission.READ_PHONE_STATE";
    public static final String SEND_SMS = "android.permission.SEND_SMS";
    public static final String RECEIVE_SMS = "android.permission.RECEIVE_SMS";
    public static final String INTERNET = "android.permission.INTERNET";
    public static final String WAKE_LOCK = "android.permission.WAKE_LOCK";
    public static final String ACCESS_NETWORK_STATE = "android.permission.ACCESS_NETWORK_STATE";
    public static final String RECEIVE_BOOT_COMPLETED = "android.permission.RECEIVE_BOOT_COMPLETED";
    public static final String WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";
    public static final String INSTALL_PACKAGES = "android.permission.INSTALL_PACKAGES";
    public static final String DELETE_PACKAGES = "android.permission.DELETE_PACKAGES";
    public static final String READ_CONTACTS = "android.permission.READ_CONTACTS";
    public static final String CALL_PHONE = "android.permission.CALL_PHONE";
    public static final String CALL_PRIVILEGED = "android.permission.CALL_PRIVILEGED";
    public static final String GET_TASKS = "android.permission.GET_TASKS";
    public static final String SYSTEM_ALERT_WINDOW = "android.permission.SYSTEM_ALERT_WINDOW";
    public static final String RESTART_PACKAGES = "android.permission.RESTART_PACKAGES";
    public static final String KILL_BACKGROUND_PROCESSES = "android.permission.KILL_BACKGROUND_PROCESSES";
    public static final String READ_LOGS = "android.permission.READ_LOGS";

    private enum Dangerous {
        DANGEROUS, NORMAL
    }

    public static Collection<String> checkOnDangerous(Collection<String> listPermission) {
        Collection<String> dangerousPermissions = new ArrayList<String>();
        for (String permission : listPermission) {
            if (READ_PHONE_STATE.equals(permission)) {
                dangerousPermissions.add(permission + " - " + Dangerous.DANGEROUS.name());
            } else if (SEND_SMS.equals(permission)) {
                dangerousPermissions.add(permission + " - " + Dangerous.DANGEROUS.name());
            } else if (RECEIVE_SMS.equals(permission)) {
                dangerousPermissions.add(permission + " - " + Dangerous.DANGEROUS.name());
            } else if (INTERNET.equals(permission)) {
                dangerousPermissions.add(permission + " - " + Dangerous.DANGEROUS.name());
            } else if (WAKE_LOCK.equals(permission)) {
                dangerousPermissions.add(permission + " - " + Dangerous.DANGEROUS.name());
            } else if (ACCESS_NETWORK_STATE.equals(permission)) {
                dangerousPermissions.add(permission + " - " + Dangerous.DANGEROUS.name());
            } else if (RECEIVE_BOOT_COMPLETED.equals(permission)) {
                dangerousPermissions.add(permission + " - " + Dangerous.DANGEROUS.name());
            } else if (WRITE_EXTERNAL_STORAGE.equals(permission)) {
                dangerousPermissions.add(permission + " - " + Dangerous.DANGEROUS.name());
            } else if (INSTALL_PACKAGES.equals(permission)) {
                dangerousPermissions.add(permission + " - " + Dangerous.DANGEROUS.name());
            } else if (DELETE_PACKAGES.equals(permission)) {
                dangerousPermissions.add(permission + " - " + Dangerous.DANGEROUS.name());
            } else if (READ_CONTACTS.equals(permission)) {
                dangerousPermissions.add(permission + " - " + Dangerous.DANGEROUS.name());
            } else if (CALL_PHONE.equals(permission)) {
                dangerousPermissions.add(permission + " - " + Dangerous.DANGEROUS.name());
            } else if (CALL_PRIVILEGED.equals(permission)) {
                dangerousPermissions.add(permission + " - " + Dangerous.DANGEROUS.name());
            } else if (GET_TASKS.equals(permission)) {
                dangerousPermissions.add(permission + " - " + Dangerous.DANGEROUS.name());
            } else if (SYSTEM_ALERT_WINDOW.equals(permission)) {
                dangerousPermissions.add(permission + " - " + Dangerous.DANGEROUS.name());
            } else if (RESTART_PACKAGES.equals(permission)) {
                dangerousPermissions.add(permission + " - " + Dangerous.DANGEROUS.name());
            } else if (KILL_BACKGROUND_PROCESSES.equals(permission)) {
                dangerousPermissions.add(permission + " - " + Dangerous.DANGEROUS.name());
            } else if (READ_LOGS.equals(permission)) {
                dangerousPermissions.add(permission + " - " + Dangerous.DANGEROUS.name());
            } else {
                dangerousPermissions.add(permission + " - " + Dangerous.NORMAL.name());
            }
        }
        return dangerousPermissions;
    }
}
