import sun.util.resources.cldr.zh.CalendarData_zh_Hans_HK;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LiangTian on 4/2/15.
 */
public class FileIO {

    public List<String> attrs = new ArrayList<>();
    public Map<String, String> attrState = new HashMap<>();
    public Map<String, String[]> nonAttrSplit = new HashMap<>();
    public Map<String, String> nonAttrSplitToNum = new HashMap<>();
    public Map<Double, String> numToAttr = new HashMap<>();

        public List<Map<String, String>> generateData(String s) {
            List<Map<String, String>> result = new ArrayList<>();
            boolean flag = false;
            int num = 1;
            try {
                BufferedReader br = new BufferedReader(new FileReader(s));
                String line = br.readLine();
                while (line != null) {
                    if (flag) {
                        String[] data = line.split(",");
                        Map<String, String> dataSet = new HashMap<>();
                        for (int i = 0; i < data.length; i++) {
                            String dataToPut = nonAttrSplitToNum.get(data[i]) == null ? data[i] : nonAttrSplitToNum.get(data[i]);
                            dataSet.put(attrs.get(i), dataToPut);
                        }
                        result.add(dataSet);
                    }
                    if (line.startsWith("@attribute")) {
                        String[] attr = line.split(" ");
                        attrs.add(attr[1]);
                        if (attr[2].equals("real")) {
                            attrState.put(attr[1], "real");
                        } else {
                            attrState.put(attr[1], "nonReal");
                            String[] splits = attr[2].substring(1, attr[2].length() - 1).split(",");
                            nonAttrSplit.put(attr[1], splits);
                            int i;
                            for (i = num; i < splits.length + num; i++) {
                                nonAttrSplitToNum.put(splits[i - num], String.valueOf(i));
                                numToAttr.put((double)i, splits[i - num]);
                            }
                            num = i;
                        }
                    } else if (line.equals("@data")) {
                        flag = true;
                    }
                    line = br.readLine();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

    public List<String> getAttrs() {
        return attrs;
    }

    public Map<String, String> getAttrState() {
        return attrState;
    }
}
