import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by LiangTian on 4/2/15.
 */
public class AttrWithResult {

    public double attrValue;
    public String result;

    public AttrWithResult() {
        this.attrValue = 0;
        this.result = null;
    }

    public static AttrWithResult[] genOrderedArray(String attr, String label, List<Map<String, String>> dataSet) {
        AttrWithResult[] results = new AttrWithResult[dataSet.size()];
        for (int i = 0; i < dataSet.size(); i++) {
            results[i] = new AttrWithResult();
        }
        int i = 0;
        for (Map<String, String> data : dataSet) {
            results[i].attrValue = Double.parseDouble(data.get(attr));
            results[i].result = data.get(label);
            i++;
        }
        Arrays.sort(results, new attrComparator());
        return results;
    }

    static class attrComparator implements Comparator<AttrWithResult> {
        @Override
        public int compare(AttrWithResult a, AttrWithResult b) {
            if (a.attrValue > b.attrValue) {
                return 1;
            } else if (a.attrValue == b.attrValue) {
                return 0;
            } else {
                return -1;
            }
        }
    }

}
