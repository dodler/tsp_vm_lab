package lian.artem.tree;

/**
 * Created by artem on 23.08.15.
 */
public class BinarySearchTree {
    public static class Elm {
        Elm left, right;
        double value;
    }

    private Elm root;
    private int length;

    public BinarySearchTree() {
        root = null;
        length = 0;
    }

    public void insert(double value) {
        recursiveInsert(root, value);
        length++;
    }

    private void recursiveInsert(Elm elm, double value) {
        if (root == null) { // tree is empty
            root = new Elm();
            root.value = value;
        } else { // tree isn't empty
            if (elm.value > value) { // means that we have to put new value to left
                if (elm.left == null) {
                    // putting new value
                    elm.left = new Elm();
                    elm.left.value = value;
                } else {
                    recursiveInsert(elm.left, value);
                }
            } else { // means that we have to put new value to right
                if (elm.right == null) {
                    elm.right = new Elm();
                    elm.right.value = value;
                } else {
                    recursiveInsert(elm.right, value);
                }
            }
        }
    }

    public void print() {
        if (root != null)
            recursivePrint(root);
    }

    private void recursivePrint(Elm elm) {
        System.out.println(elm.value);
        if (elm.left != null) {
            recursivePrint(elm.left);
        }
        System.out.print("---|");
        if (elm.right != null) {
            recursivePrint(elm.right);
        }
        System.out.print("|---");
    }
}
