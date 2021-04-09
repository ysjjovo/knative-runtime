package runtime.log;

public class Logger {
    public static ThreadLocal<String> REQUEST_ID = new ThreadLocal<>();

    public static void println(String s) {
        print(s);
        System.out.println();
    }

    public static void print(String s) {
        String requestId = REQUEST_ID.get();
        if (requestId == null) {
            System.out.print(s);
            return;
        }
        System.out.print(requestId + " " + s);
    }
}
