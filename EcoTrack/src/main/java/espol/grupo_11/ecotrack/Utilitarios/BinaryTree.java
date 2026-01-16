package espol.grupo_11.ecotrack.Utilitarios;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Stack;

public class BinaryTree<T> implements Iterable<T> {

    private NodeBinaryTree<T> root;
    private Comparator<T> comparator;

    // ======================
    // CONSTRUCTORES
    // ======================

    public BinaryTree(Comparator<T> comparator) {
        this.root = null;
        this.comparator = comparator;
    }

    public BinaryTree(T content, Comparator<T> comparator) {
        this.root = new NodeBinaryTree<>(content);
        this.comparator = comparator;
    }

    public BinaryTree(NodeBinaryTree<T> root, Comparator<T> comparator) {
        this.root = root;
        this.comparator = comparator;
    }

    // ======================
    // UTILIDADES
    // ======================

    public boolean isEmpty() {
        return root == null;
    }

    public NodeBinaryTree<T> getRoot() {
        return root;
    }

    public void setRoot(NodeBinaryTree<T> root) {
        this.root = root;
    }

    // ======================
    // AGREGAR (BST con Comparator)
    // ======================
    public void agregar(T dato) {
        if (dato == null) return;
        if (comparator == null)
            throw new IllegalStateException("Comparator no definido");

        if (this.isEmpty()) {
            this.root = new NodeBinaryTree<>(dato);
            return;
        }

        int cmp = comparator.compare(dato, root.getContent());

        if (cmp < 0) {
            if (root.getLeft().isEmpty()) {
                root.setLeft(new BinaryTree<>(dato, comparator));
            } else {
                root.getLeft().agregar(dato);
            }
        } else {
            if (root.getRight().isEmpty()) {
                root.setRight(new BinaryTree<>(dato, comparator));
            } else {
                root.getRight().agregar(dato);
            }
        }
    }

    // ======================
    // RECORRIDO INORDEN
    // ======================
    public void inOrden() {
        if (!this.isEmpty()) {
            root.getLeft().inOrden();
            System.out.println(root.getContent());
            root.getRight().inOrden();
        }
    }

    // ======================
    // ITERABLE INORDEN
    // ======================
    @Override
    public Iterator<T> iterator() {
        return new InOrderIterator();
    }

    private class InOrderIterator implements Iterator<T> {

        private Stack<NodeBinaryTree<T>> stack = new Stack<>();

        public InOrderIterator() {
            pushLeft(root);
        }

        private void pushLeft(NodeBinaryTree<T> node) {
            while (node != null && !node.getLeft().isEmpty()) {
                stack.push(node);
                node = node.getLeft().getRoot();
            }
            if (node != null) stack.push(node);
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public T next() {
            NodeBinaryTree<T> current = stack.pop();
            T result = current.getContent();

            if (!current.getRight().isEmpty()) {
                pushLeft(current.getRight().getRoot());
            }
            return result;
        }
    }
}
