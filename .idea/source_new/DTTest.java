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
        String s = "trainProdSelection.arff";
        DecisionTree dt = new DecisionTree();
        Double cvr = dt.crossValidation(s);
        System.out.println("the cross validation rate is " + cvr);
//        System.out.println("the first treenode is about " + root.attr);
//        System.out.println("and I have " + root.children.size() + " children");
//        printTree(root, 0);
    }

}
