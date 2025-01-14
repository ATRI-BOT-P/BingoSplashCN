package cn.bingosplash.loggers;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("static-access")
public final class BSLogger {

    private static String prefix = "[BingoSplashCN] ";

    private static net.minecraftforge.fml.relauncher.FMLRelaunchLog coreLog = net.minecraftforge.fml.relauncher.FMLRelaunchLog.log;

    public static void log(String targetLog, Level level, String format, Object... data) {
        coreLog.log(targetLog, level, prefix + format, data);
    }

    public static void log(Level level, String format, Object... data) {
        coreLog.log(level, prefix + format, data);
    }

    public static void log(String targetLog, Level level, Throwable ex, String format, Object... data) {
        coreLog.log(targetLog, level, ex, prefix + format, data);
    }

    public static void log(Level level, Throwable ex, String format, Object... data) {
        coreLog.log(level, ex, prefix + format, data);
    }

    public static void severe(String format, Object... data) {
        log(Level.ERROR, format, data);
    }

    public static void bigWarning(String format, Object... data) {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        log(Level.WARN, "****************************************");
        log(Level.WARN, "* " + format, data);
        for (int i = 2; i < 8 && i < trace.length; i++) {
            log(Level.WARN, "*  at %s%s", trace[i].toString(), i == 7 ? "..." : "");
        }
        log(Level.WARN, "****************************************");
    }

    public static void warning(String format, Object... data) {
        log(Level.WARN, format, data);
    }

    public static void info(String format, Object... data) {
        log(Level.INFO, format, data);
    }

    public static void fine(String format, Object... data) {
        log(Level.DEBUG, format, data);
    }

    public static void finer(String format, Object... data) {
        log(Level.TRACE, format, data);
    }

    public static Logger getLogger() {
        return coreLog.getLogger();
    }
}