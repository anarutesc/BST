
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author J-C. Filliatre and L. Castelli Aleardi (INF421, 2013, Ecole
 * Polytechnique) An implementation of an ordered set based on binary search
 * trees
 */
public class TreeNode<E extends Comparable<E>> {

    final E value;
    TreeNode<E> left, right;

    public TreeNode(TreeNode<E> left, E value, TreeNode<E> right) {
        this.value = value;
        this.left = left;
        this.right = right;
    }

    public TreeNode(E value) {
        this.value = value;
        this.left = null;
        this.right = null;
    }

    public boolean contains(E x) {
        if (x.compareTo(this.value) == 0) {
            return true;
        } else if (x.compareTo(this.value) < 0) {
            if (left == null) {
                return false;
            } else {
                return left.contains(x);
            }
        } else {
            if (right == null) {
                return false;
            } else {
                return right.contains(x);
            }
        }
    }

    public TreeNode<E> add(E x) {
        if (x.compareTo(this.value) > 0) {
            if (right != null) {
                return right.add(x);
            } else {
                TreeNode tn = new TreeNode(x);
                right = tn;
                return this;
            }
        } else if (x.compareTo(this.value) < 0) {
            if (left != null) {
                return left.add(x);
            } else {
                TreeNode tn = new TreeNode(x);
                left = tn;
                return this;
            }
        } else {
            return null;
        }
    }

    public E getMin() {
        if (left != null) {
            return left.getMin();
        } else {
            return value;
        }
    }

    static <E extends Comparable<E>> TreeNode<E> ofList(Queue<E> l, int n, int k) {

        if (k == 0) {
            if (n > 0) {
                return new TreeNode(l.poll());
            } else {
                return null;
            }
        } else {
            return new TreeNode<>(ofList(l, (n - 1) / 2, k - 1), l.poll(), ofList(l, n - (n - 1) / 2 - 1, k - 1));
        }

    }

    static <E extends Comparable<E>> TreeNode<E> ofList(Queue<E> l) {
        int n = l.size();
        return ofList(l, n, (int) (Math.log(n) / Math.log(2)));
    }

    /* subset */
    static <E extends Comparable<E>> boolean subset(TreeNode<E> s1, TreeNode<E> s2) {
        if (s1 == null) {
            return true;
        } else if (s2 == null) {
            return false;
        } else {
            if (s1.value.compareTo(s2.value) == 0) {
                return subset(s1.left, s2.left) && subset(s1.right, s2.right);
            } else if (s1.value.compareTo(s2.value) < 0) {
                TreeNode<E> aux = new TreeNode<>(s1.value);
                return subset(aux.union(s1.left), s2.left) && subset(s1.right, s2);
            } else {
                TreeNode<E> aux = new TreeNode<>(s1.value);
                return subset(aux.union(s1.right), s2.right) && subset(s1.left, s2);
            }
        }
    }

    /* split(v,s) returns two trees, containing values
	   * from s smaller and greater than s
     */
    public Pair<TreeNode<E>> split(E x) {

        TreeNode esq = null;
        TreeNode dir = null;

        Queue<TreeNode<E>> fila = new LinkedList<>();
        fila.add(this);

        while (!fila.isEmpty()) {
            TreeNode<E> noAtual = fila.element();
            fila.poll();

            if (x.compareTo(noAtual.value) > 0) {
                if (esq == null) {
                    esq = new TreeNode(noAtual.value);
                } else {
                    esq.add(noAtual.value);
                }
            } else if (x.compareTo(noAtual.value) < 0) {
                if (dir == null) {
                    dir = new TreeNode(noAtual.value);
                } else {
                    dir.add(noAtual.value);
                }
            }

            if (noAtual.left != null) {
                fila.add(noAtual.left);
            }
            if (noAtual.right != null) {
                fila.add(noAtual.right);
            }

        }

        Pair p = new Pair(esq, dir);
//           
        return p;
    }

    /* union */
    public TreeNode<E> union(TreeNode<E> s2) {

        if (s2 == null) {
            return this;
        } else {

            Pair<TreeNode<E>> p;
            TreeNode<E> u = new TreeNode(value);

            p = s2.split(value);

            if (left != null) {
                u.left = left.union(p.a);
            } else {
                u.left = p.a;
            }

            if (right != null) {
                u.right = right.union(p.b);
            } else {
                u.right = p.b;
            }

            return u;
        }
    }

    public String infixOrder() {
        String result = "";
        if (this.left != null) {
            result = result + this.left.infixOrder();
        }
        result = result + " " + this.value;
        if (this.right != null) {
            result = result + this.right.infixOrder();
        }
        return result;
    }

    /**
     * Return the list of elements listed according to infix order
     */
    public LinkedList<E> toList() {
        LinkedList<E> result = null;
        if (this.left != null) {
            result = (this.left.toList());
        }
        if (result == null) {
            result = new LinkedList<E>();
        }
        result.add(this.value);
        if (this.right != null) {
            result.addAll(this.right.toList());
        }
        return result;
    }

    public String toString() {
        return "(" + this.left + "+" + this.value + "+" + this.right + ")";
    }

}
