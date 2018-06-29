package com.example.simon.androidnetworktesting;

import android.os.Build;
import android.os.Process;
import android.util.Log;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Locale;

public class Logger {


    private static final String DATE_FORMAT = "MM-dd HH:mm:ss.SSS Z";
    private static final ThreadLocal<EnhancementDateFormat> sdf =
            new ThreadLocal<EnhancementDateFormat>() {
                @Override
                protected EnhancementDateFormat initialValue() {
                    return new EnhancementDateFormat(DATE_FORMAT, Locale.US);
            }
        };
    private volatile static int LOG_LVL = LogLvl.FINE_TRACE.lvl;
    private String tag;
    @SuppressWarnings("rawtypes")
    private Class clazz;
    private String fullClassName;
    private int sdkNumber;


    private Logger(@SuppressWarnings("rawtypes") Class clazz) {
        super();
        this.clazz = clazz;
        this.tag = "[JAVA]" + this.clazz.getSimpleName();
        this.fullClassName = this.clazz.getName();
        this.sdkNumber = Build.VERSION.SDK_INT;
    }
    
    public static Logger getLogger(
            @SuppressWarnings("rawtypes") Class clazz) {
        return new Logger(clazz);
    }

    public static int getLogLevel() {
        return LOG_LVL;
    }

    public static void setLogLevel(int log_lvl) {
        LOG_LVL = log_lvl;
    }

    public static boolean isFatal(){
        return LOG_LVL >= LogLvl.FATAL.lvl;
    }        

    public static boolean isError(){
        return LOG_LVL >= LogLvl.ERROR.lvl;
    }
    
    public static boolean isWarn(){
        return LOG_LVL >= LogLvl.WARN.lvl;
    }    
    
    public static boolean isInfo(){
        return LOG_LVL >= LogLvl.INFO.lvl;
    }
    
    public static boolean isDebug(){
        return LOG_LVL >= LogLvl.DEBUG.lvl;
    }
    
    public static boolean isTrace(){
        return LOG_LVL >= LogLvl.TRACE.lvl;
    }       
    
    public static boolean isFineTrace(){
        return LOG_LVL >= LogLvl.FINE_TRACE.lvl;  
    }
  
    public void fatal(String message, Exception ex) {
        if (isFatal()) {
            Log.println(Log.ASSERT, tag, toLogMsg(message, LogLvl.FATAL) + '\n' + getStackTraceString(ex));
        }

    }

    public void error(String message, Throwable throwable) {
        if (isError()) {
            Log.println(Log.ERROR, tag, toLogMsg(message, LogLvl.ERROR) + '\n' + getStackTraceString(throwable));
        }
    }
    
    public void error(Throwable e) {
        if (isError()) {
            Log.println(Log.ERROR, tag, toLogMsg(e.getMessage(), LogLvl.ERROR) + '\n' + getStackTraceString(e));
        }

    }
    
    public void error(String message) {
        if (isError()) {
            Log.println(Log.ERROR, tag, toLogMsg(message, LogLvl.ERROR));
        }

    }
    
    public void warn(String message) {
        if (isWarn()) {
            Log.println(Log.WARN, tag, toLogMsg(message, LogLvl.WARN));
        }

    }

    public void warn(String message, Throwable e) {
        Log.println(Log.WARN, tag, toLogMsg(message, LogLvl.WARN) + '\n' + getStackTraceString(e));
    }
    
    public void info(String message) {
        if (isInfo()) {
            Log.println(Log.INFO, tag, toLogMsg(message, LogLvl.INFO));
        }
    }
    
    public void info(String message, Throwable e) {
        if (isInfo()) {
            Log.println(Log.INFO, tag, toLogMsg(message, LogLvl.INFO) + '\n' + getStackTraceString(e));
        }
    }
    
    public void debug(String message) {
        if (isDebug()) {
            Log.println(Log.DEBUG, tag, toLogMsg(message, LogLvl.DEBUG));
        }

    }
    
    public void debug(String message, Exception e) {
        if (isDebug()) {
            Log.println(Log.DEBUG, tag, toLogMsg(message, LogLvl.DEBUG)+ '\n' + getStackTraceString(e));
        }

    }

    public void trace(String message, Exception e) {
        if (isTrace()) {
            Log.println(Log.VERBOSE, tag, toLogMsg(message, LogLvl.TRACE)+ '\n' + getStackTraceString(e));
        }

    }

    public void trace(String message) {
        if (isTrace()) {
            Log.println(Log.VERBOSE, tag, toLogMsg(message, LogLvl.TRACE));
        }

    }
    
    public void finetrace(String message) {
        if (isFineTrace()) {
            Log.println(Log.VERBOSE, tag, toLogMsg(message, LogLvl.FINE_TRACE));
        }

    }

    private String toLogMsg(String message, LogLvl lvl) {
        StringBuilder sb = new StringBuilder();
        sb.append(sdf.get().enhFormat(new Date())).append(" ");
        sb.append(Process.myTid());
        sb.append(" [");
        sb.append(lvl.label);
        sb.append("] ");
        sb.append(message);
        return sb.toString();
    }

    private enum LogLvl {
        FATAL(0, "FL"), ERROR(1, "E"),
        WARN(2, "W"), INFO(3, "I"),
        DEBUG(4, "D"), TRACE(5, "T"),
        FINE_TRACE(6, "FT");


        private int lvl;
        private String label;

        LogLvl(int lvl, String label) {
            this.lvl = lvl;
            this.label = label;
        }
    }

    private String getStackTraceString(Throwable t) {
        StringWriter sw = new StringWriter(256);
        PrintWriter pw = new PrintWriter(sw, false);
        t.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }
}
