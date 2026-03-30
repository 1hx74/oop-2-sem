package gui;

public class SpeedTest {

    private static final int ITERATIONS = 1000_000;

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

        System.out.println(t1 + " formater");
        System.out.println(t2 + " format_no_cache");
        System.out.println(t3 + " format_cache");
        System.out.println(t4 + " string_builder");
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
}