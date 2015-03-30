import java.util.HashMap;
import java.util.Map;

/**
 * Created by LiangTian on 3/29/15.
 */
public class TreeNode {

    private Map<Integer, TreeNode> children;
    private String attr;

    public TreeNode(String s) {
        this.attr = s;
        children = new HashMap<>();
    }

    public void addChildren(int i, TreeNode node) {
        this.children.put(i, node);
    }

    public Map<Integer, TreeNode> getChildren() {
        return children;
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }
}
