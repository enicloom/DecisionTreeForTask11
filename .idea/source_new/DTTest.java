/**
 * Created by LiangTian on 4/2/15.
 */
public class DTTest {

    public static void printTree(TreeNode node, int num) {
        if (node.isLeaf) {
            printHelper(num);
            System.out.println(node.finalResult);
            return;
        }
        printHelper(num);
        System.out.print(node.attr);
        if (node.value != -1) {
            for (String child : node.children.keySet()) {
                System.out.println("--" + child + " " + node.value);
                printTree(node.children.get(child), num + 8);
            }
        } else {
            for (String child : node.children.keySet()) {
                System.out.println("--" + child);
                printTree(node.children.get(child), num + 8);
            }
        }
    }

    public static void printHelper(int num) {
        for (int i = 0; i < num; i++) {
            System.out.print("--");
        }
    }

    public static void main(String[] args) {
        String train = "trainProdSelection.arff";
//        String train = "trainProdIntro.binary.arff";
        String test = "testProdSelection.arff";
//        String test = "testProdIntro.binary.arff";
        DecisionTree dt = new DecisionTree();
        Double cvr = dt.crossValidation(train);
        System.out.println("the cross validation rate is " + cvr);
        dt.makePrediction(train, test);

    }

}
