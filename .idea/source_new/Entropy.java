import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LiangTian on 4/2/15.
 */
public class Entropy {

    public static double info(double[] dis) {
        double result = 0;
        double sum = 0;
        for (double i : dis) {
            sum += i;
        }
        for (double i : dis) {
            result += -(i / sum) * (Math.log(i / sum) / Math.log(2));
        }
        return result;
    }

    public static double infoGainCont(double entro, double a, double b, double[] partA, double[] partB) {
        double infoA = info(partA);
        double infoB = info(partB);
        double ratioA = a / (a + b);
        double ratioB = b / (a + b);

        return entro - (ratioA * infoA + ratioB * infoB);
    }

    public static double infoGainRatioCont(double entro, double a, double b, double[] partA, double[] partB) {
        double infoGain = infoGainCont(entro, a, b, partA, partB);
        double[] attrDis = {a, b};
        double HV = info(attrDis);

        return infoGain / HV;
    }

    public static double[] infoGainContWhole(double entro, String attr, String label, List<Map<String, String>> dataSet) {
        AttrWithResult[] attrs = AttrWithResult.genOrderedArray(attr, label, dataSet);
        double[] attrsValue = new double[attrs.length];
        String[] attrsLabel = new String[attrs.length];
        double infoGainMax = 0;
        double splitPt = 0;
        for (int i = 0; i < attrs.length; i++) {
            attrsValue[i] = attrs[i].attrValue;
            attrsLabel[i] = attrs[i].result;
        }
        for (int i = 1; i < attrs.length; i++) {
            if (attrsValue[i] != attrsValue[i - 1] && !attrsLabel[i].equals(attrsLabel[i - 1])) {
                String[] partA = Arrays.copyOfRange(attrsLabel, 0, i);
                String[] partB = Arrays.copyOfRange(attrsLabel, i, attrs.length);
                double[] a = getDis(partA);
                double[] b = getDis(partB);
                double infoGain = infoGainCont(entro, (double)i, (double)(attrs.length - i), a, b);
                if (infoGain > infoGainMax) {
                    infoGainMax = infoGain;
                    splitPt = attrsValue[i];
                }
            }
        }
        return new double[]{infoGainMax, splitPt};
    }

    public static double[] infoGainRatioContWhole(double entro, String attr, String label, List<Map<String, String>> dataSet) {
        AttrWithResult[] attrs = AttrWithResult.genOrderedArray(attr, label, dataSet);
        double[] attrsValue = new double[attrs.length];
        String[] attrsLabel = new String[attrs.length];
        double infoGainRatioMax = 0;
        double splitPt = 0;
        for (int i = 0; i < attrs.length; i++) {
            attrsValue[i] = attrs[i].attrValue;
            attrsLabel[i] = attrs[i].result;
        }
        for (int i = 1; i < attrs.length; i++) {
            if (attrsValue[i] != attrsValue[i - 1] && !attrsLabel[i].equals(attrsLabel[i - 1])) {
                String[] partA = Arrays.copyOfRange(attrsLabel, 0, i);
                String[] partB = Arrays.copyOfRange(attrsLabel, i, attrs.length);
                double[] a = getDis(partA);
                double[] b = getDis(partB);
                double infoGain = infoGainRatioCont(entro, (double) i, (double) (attrs.length - i), a, b);
                if (infoGain > infoGainRatioMax) {
                    infoGainRatioMax = infoGain;
                    splitPt = attrsValue[i];
                }
            }
        }
        return new double[]{infoGainRatioMax, splitPt};
    }

    public static double infoGainNonCont(double entro, String attr, String label, List<Map<String, String>> dataSet) {
        AttrWithResult[] attrs = AttrWithResult.genOrderedArray(attr, label, dataSet);
        double[] attrsValue = new double[attrs.length];
        String[] attrsLabel = new String[attrs.length];
        double infoGain = 0;
        double infoWhole = 0;
        int start = 0;
        for (int i = 0; i < attrs.length; i++) {
            attrsValue[i] = attrs[i].attrValue;
            attrsLabel[i] = attrs[i].result;
        }
        for (int i = 1; i < attrs.length; i++) {
            if (attrsValue[i] != attrsValue[i - 1]) {
                String[] part = Arrays.copyOfRange(attrsLabel, start, i);
                double[] a = getDis(part);
                infoWhole += ((double)(i - start) / (double)attrs.length) * info(a);
                start = i;
            }
        }
        String[] part = Arrays.copyOfRange(attrsLabel, start, attrs.length);
        double[] a = getDis(part);
        infoWhole += ((double)(attrs.length - start) / (double)attrs.length) * info(a);
        infoGain = entro - infoWhole;
        return infoGain;
    }

    public static double infoGainRatioNonCont(double entro, String attr, String label, List<Map<String, String>> dataSet) {
        AttrWithResult[] attrs = AttrWithResult.genOrderedArray(attr, label, dataSet);
        double[] attrsValue = new double[attrs.length];
        String[] attrsLabel = new String[attrs.length];
        double infoGain = 0;
        double infoWhole = 0;
        int start = 0;
        for (int i = 0; i < attrs.length; i++) {
            attrsValue[i] = attrs[i].attrValue;
            attrsLabel[i] = attrs[i].result;
        }
        for (int i = 1; i < attrs.length; i++) {
            if (attrsValue[i] != attrsValue[i - 1]) {
                String[] part = Arrays.copyOfRange(attrsLabel, start, i);
                double[] a = getDis(part);
                infoWhole += ((double)(i - start) / (double)attrs.length) * info(a);
                start = i;
            }
        }
        String[] part = Arrays.copyOfRange(attrsLabel, start, attrs.length);
        double[] a = getDis(part);
        infoWhole += ((double)(attrs.length - start) / (double)attrs.length) * info(a);
        infoGain = entro - infoWhole;

        String[] attrsValueS = new String[attrsValue.length];
        for (int i = 0; i < attrsValue.length; i++) {
            attrsValueS[i] = String.valueOf(attrsLabel[i]);
        }
        double HV = info(getDis(attrsValueS));

        return infoGain / HV;
    }

    public static double getTotalEntro(String label, List<Map<String, String>> set) {
        String[] strs = new String[set.size()];
        int i = 0;
        for (Map<String, String> data : set) {
            strs[i] = data.get(label);
            i++;
        }
        return info(getDis(strs));
    }

    public static double[] getDis(String[] strs) {
        Map<String, Integer> map = new HashMap<>();
        for (String s : strs) {
            map.put(s, map.get(s) == null ? 1 : map.get(s) + 1);
        }
        double[] dis = new double[map.size()];
        int i = 0;
        for (String s : map.keySet()) {
            dis[i] = map.get(s);
            i++;
        }
        return dis;
    }

}
