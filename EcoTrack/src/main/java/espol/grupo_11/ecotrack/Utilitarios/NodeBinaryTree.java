package espol.grupo_11.ecotrack.Utilitarios;

public class NodeBinaryTree<T> {

    private T content;
    private BinaryTree<T> left;
    private BinaryTree<T> right;

    public NodeBinaryTree(T content) {
        this.content = content;
        this.left = new BinaryTree<>(null);
        this.right = new BinaryTree<>(null);
    }

    public T getContent() {
        return content;
    }

    public BinaryTree<T> getLeft() {
        return left;
    }

    public BinaryTree<T> getRight() {
        return right;
    }

    public void setLeft(BinaryTree<T> nuevo) {
        this.left = nuevo;
    }

    public void setRight(BinaryTree<T> nuevo) {
        this.right = nuevo;
    }
}
