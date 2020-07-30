package org.mini2Dx.core;

public class StdOutLogger implements Logger {

    private int logLevel = Logger.LOG_INFO;
    @Override
    public void info(String tag, String message) {
        if (logLevel <= Logger.LOG_INFO){
            System.out.println("[" + tag + "] " + message);
        }
    }

    @Override
    public void debug(String tag, String message) {
        if (logLevel <= Logger.LOG_DEBUG){
            System.out.println("[" + tag + "] " + message);
        }
    }

    @Override
    public void error(String tag, String message) {
        if (logLevel <= Logger.LOG_ERROR){
            System.out.println("[" + tag + "] " + message);
        }
    }

    @Override
    public void error(String tag, String message, Exception e) {
        if (logLevel <= Logger.LOG_ERROR){
            System.err.println("[" + tag + "] " + message + " " + e.toString());
            e.printStackTrace(System.err);
        }
    }

    @Override
    public void setLoglevel(int loglevel) {
        this.logLevel = loglevel;
    }
}
