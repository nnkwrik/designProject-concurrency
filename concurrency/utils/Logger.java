package concurrency.utils;

public class Logger {
    private static final ThreadLocal<String> LOGGER = new ThreadLocal<>();

    public static void log(String log) {
        log = LOGGER.get() == null ? log : LOGGER.get() + log;
        LOGGER.set(log);
    }

    public static void out(){
        System.out.println(LOGGER.get());
        LOGGER.remove();
    }
}
