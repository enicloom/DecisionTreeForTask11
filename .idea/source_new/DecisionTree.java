import java.util.*;

/**
 * Created by LiangTian on 4/2/15.
 */
public class DecisionTree {

    public List<String> attrs = new ArrayList<>();
    public Map<String, String> attrState = new HashMap<>();
    public Map<String, String[]> nonAttrSplit;
    public Map<Double, String> numToAttr;
    public String label;

    public TreeNode generateTree(String s) {
        FileIO newFile = new FileIO();

        List<Map<String, String>> trainSet = newFile.generateData(s);
        attrs = newFile.attrs;
        attrState = newFile.attrState;
        nonAttrSplit = newFile.nonAttrSplit;
        numToAttr = newFile.numToAttr;
        label = attrs.get(attrs.size() - 1);

        Set<String> chosen = new HashSet<>();

        return generateTreeHelper(trainSet, chosen);
    }

    public TreeNode generateTreeHelper(List<Map<String, String>> trainSet, Set<String> chosen) {

        if (isSameLabel(trainSet)) {
            return new TreeNode(voting(trainSet));
        }

        if (trainSet.size() <= 5) {
            return new TreeNode(voting(trainSet));
        }

        Map<Double, String> infoGainMap = new HashMap<>();
        Map<String, Double> conSplitPt = new HashMap<>();

        double entro = Entropy.getTotalEntro(label, trainSet);

        for (int i = 0; i < attrs.size() - 1; i++) {
            if (attrState.get(attrs.get(i)).equals("real")) {
                double[] infos = Entropy.infoGainRatioContWhole(entro, attrs.get(i), label, trainSet);
                infoGainMap.put(infos[0], attrs.get(i));
                conSplitPt.put(attrs.get(i), infos[1]);
            } else if (!chosen.contains(attrs.get(i))) {
                double info = Entropy.infoGainRatioNonCont(entro, attrs.get(i), label, trainSet);
                infoGainMap.put(info, attrs.get(i));
            }
        }

        double maxInfoGainRatio = 0;
        for (double d : infoGainMap.keySet()) {
            if (d > maxInfoGainRatio) {
                maxInfoGainRatio = d;
            }
        }

        String curType = infoGainMap.get(maxInfoGainRatio);
        TreeNode node = new TreeNode();
        node.attr = curType;

        if (attrState.get(curType).equals("real")) {
            node.value = conSplitPt.get(curType);
            List<Map<String, String>> lessThan = new ArrayList<>();
            List<Map<String, String>> noLessThan = new ArrayList<>();
            for (Map<String, String> data : trainSet) {
                if (Double.parseDouble(data.get(curType)) < node.value) {
                    lessThan.add(data);
                } else {
                    noLessThan.add(data);
                }
            }
            if (lessThan.size() > 0) {
                node.children.put("lessThan", generateTreeHelper(lessThan, chosen));
            }
            if (noLessThan.size() > 0) {
                node.children.put("noLessThan", generateTreeHelper(noLessThan, chosen));
            }
            node.finalResult = voting(trainSet);
        } else {
            Map<String, List<Map<String, String>>> mapSet = new HashMap<>();
            for (Map<String, String> data : trainSet) {
                if (mapSet.get(data.get(curType)) == null) {
                    List<Map<String, String>> list = new ArrayList<>();
                    list.add(data);
                    mapSet.put(data.get(curType), list);
                } else {
                    mapSet.get(data.get(curType)).add(data);
                }
            }
            chosen.add(curType);
            for (String s : mapSet.keySet()) {
                node.children.put(s, generateTreeHelper(mapSet.get(s), chosen));
            }
            node.finalResult = voting(trainSet);
        }

        return node;
    }

    public double crossValidation(String s) {
        FileIO newFile = new FileIO();

        List<Map<String, String>> wholeSet = newFile.generateData(s);
        attrs = newFile.attrs;
        attrState = newFile.attrState;
        nonAttrSplit = newFile.nonAttrSplit;
        numToAttr = newFile.numToAttr;
        label = attrs.get(attrs.size() - 1);

        Collections.shuffle(wholeSet);
        int testSize = wholeSet.size() / 10;
        int start = 1;
        int iterate = 0;
        double rate = 0;

        while (start + testSize < wholeSet.size()) {
            List<Map<String, String>> testSet = new ArrayList<>();
            List<Map<String, String>> trainset = new ArrayList<>();
            iterate++;
            for (int i = start - 1; i < start + testSize && i < wholeSet.size(); i++) {
                testSet.add(wholeSet.get(i));
            }

            Set<String> chosen = new HashSet<>();
            trainset.addAll(wholeSet.subList(0, start - 1));
            trainset.addAll(wholeSet.subList(start + testSize, wholeSet.size()));
            start = start + testSize + 1;
            TreeNode node = generateTreeHelper(trainset, chosen);
            List<String> results = determineTests(node, testSet);
            int correct = 0;
            for (int i = 0; i < testSet.size(); i++) {
                if (results.get(i).equals(testSet.get(i).get(attrs.get(attrs.size() - 1)))) {
                    correct++;
                }
            }
            rate += (double) correct / (double) (testSet.size());
        }

        return rate / iterate;
    }

    public void makePrediction(String train, String test) {
        FileIO newTestFile = new FileIO();

        List<Map<String, String>> wholeSet = newTestFile.generateData(train);
        List<Map<String, String>> testSet = newTestFile.generateData(test);
        attrs = newTestFile.attrs;
        attrState = newTestFile.attrState;
        nonAttrSplit = newTestFile.nonAttrSplit;
        numToAttr = newTestFile.numToAttr;
        label = attrs.get(attrs.size() - 1);

        Set<String> chosen = new HashSet<>();
        TreeNode node = generateTreeHelper(wholeSet, chosen);
        List<String> results = determineTests(node, testSet);

        for (int i = 0; i < results.size(); i++) {
            System.out.println("the decision for data " + (i + 1) + " is " + numToAttr.get(Double.parseDouble(results.get(i))));
        }
    }

    public List<String> determineTests(TreeNode node, List<Map<String, String>> testSet) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < testSet.size(); i++) {
            result.add(determine(node, testSet.get(i)));
        }
        return result;
    }

    public String determine(TreeNode node, Map<String, String> data) {
        while (node != null && !node.isLeaf) {
            if (node.value == -1) {
                if (node.children.get(data.get(node.attr)) != null) {
                    node = node.children.get(data.get(node.attr));
                } else return node.finalResult;
            } else {
                if (Double.parseDouble(data.get(node.attr)) < node.value) {
                    if (node.children.get("lessThan") != null) {
                        node = node.children.get("lessThan");
                    } else return node.finalResult;
                } else if (Double.parseDouble(data.get(node.attr)) >= node.value) {
                    if (node.children.get("noLessThan") != null) {
                        node = node.children.get("noLessThan");
                    } else return node.finalResult;
                }
            }
        }
        return node.finalResult;
    }

    public String voting(List<Map<String, String>> list) {
        Map<String, Integer> map = new HashMap<>();
        for (Map<String, String> p : list) {
            map.put(p.get(label), map.get(p.get(label)) == null ? 1 : map.get(p.get(label)) + 1);
        }
        int count = 0;
        String result = null;
        for (String s : map.keySet()) {
            if (map.get(s) > count) {
                count = map.get(s);
                result = s;
            }
        }
        return result;
    }

    public boolean isSameLabel(List<Map<String, String>> list) {
        String[] labels = new String[list.size()];
        int i;
        for (i = 0; i < list.size(); i++) {
            labels[i] = list.get(i).get(label);
        }
        for (i = 1; i < list.size(); i++) {
            if (!labels[i].equals(labels[i - 1])) {
                return false;
            }
        }
        return true;
    }
}
