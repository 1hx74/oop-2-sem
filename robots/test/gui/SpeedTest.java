package gui;

public class SpeedTest {

    private static final int ITERATIONS = 1_000_000;

    // фиктивные
    private static final double X       = 1.11;
    private static final double Y       = 2.22;
    private static final double DIR_DEG = Math.toDegrees(Math.PI / 3);

    private final Localize localize;

    public SpeedTest(String language) {
        this.localize = new Localize(language);
    }

    public static void main(String[] args) {
        new SpeedTest("en").run();
    }

    private void run() {
        // warmup
        runIters(FormatCombo.Mode.FORMATTER);
        runIters(FormatCombo.Mode.MESSAGE_FORMAT_NO_CACHE);
        runIters(FormatCombo.Mode.MESSAGE_FORMAT_CACHE);
        runIters(FormatCombo.Mode.STRING_BUILDER);

        // tests
        long t1 = measure(() -> runIters(FormatCombo.Mode.FORMATTER));
        long t2 = measure(() -> runIters(FormatCombo.Mode.MESSAGE_FORMAT_NO_CACHE));
        long t3 = measure(() -> runIters(FormatCombo.Mode.MESSAGE_FORMAT_CACHE));
        long t4 = measure(() -> runIters(FormatCombo.Mode.STRING_BUILDER));

        // print
        System.out.println("for iterations: " + ITERATIONS);

        System.out.println(cifirkiFormat(t1, 16) + " formater");
        System.out.println(cifirkiFormat(t2, 16) + " format_no_cache");
        System.out.println(cifirkiFormat(t3, 16) + " format_cache");
        System.out.println(cifirkiFormat(t4, 16) + " string_builder");
    }

    private void runIters(FormatCombo.Mode mode) {
        FormatCombo formatCombo = new FormatCombo(localize, new RobotState(X, Y, DIR_DEG), mode);
        for (int i = 0; i < ITERATIONS; i++) {
            formatCombo.get();
        }
    }

    private long measure(Runnable r) {
        long start = System.nanoTime();
        r.run();
        return System.nanoTime() - start;
    }

    private String cifirkiFormat(long x, int len) {
        String result = "";
        String str = String.valueOf(x);
        int count = 0;

        for (int i = str.length() - 1; i >= 0; i--) {
            result = str.charAt(i) + result;
            count++;
            if (count % 3 == 0 && i != 0) {
                result = "_" + result;
            }
        }

        int lenResult = result.length();
        for (int i = 0; i < len - lenResult; i++) {
            result = " " + result;
        }
        return result;
    }
}