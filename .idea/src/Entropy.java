import java.util.Set;

/**
 * Created by LiangTian on 3/29/15.
 */
public class Entropy {

    public static double infoGain(int a, int b) {
        double x = a / (a + b);
        double y = 1 - x;
        return -1 * x * (Math.log(2)/Math.log(x)) - y * (Math.log(2)/Math.log(y));
    }

    public static double splitGain(int num1, int num2, int[] a, int[] b) {
        double x = infoGain(a[0], a[1]);
        double y = infoGain(b[0], b[1]);
        return num1 * x + num2 * y;
    }

    public static double maxGain(double ent, Set<Double> set) {
        double max = 0;
        double result = 0;
        for (double d : set) {
            if (ent - d > max) {
                max = ent - d;
                result = d;
            }
        }
        return result;
    }

}
