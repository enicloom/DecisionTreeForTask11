import java.util.HashMap;
import java.util.Map;

/**
 * Created by LiangTian on 4/2/15.
 */
public class TreeNode {

    public Map<String, TreeNode> children;
    public String attr;
    public double value;
    public String finalResult;
    public boolean isLeaf;

    public TreeNode() {
        children = new HashMap<>();
        value = -1;
        isLeaf = false;
    }

    public TreeNode(String s) {
        isLeaf = true;
        finalResult = s;
    }
}
