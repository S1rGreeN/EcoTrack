package espol.grupo_11.ecotrack.Utilitarios;


public class BinaryTree<T> {
    private NodeBinaryTree<T> root;
    
    
    public BinaryTree(){
        this.root = null;
    }
    
    public BinaryTree(NodeBinaryTree<T> root){
        this.root = root;
    }
    
    
    
    public BinaryTree(T content){
        this.root = new NodeBinaryTree<>(content);
    }
    
    public boolean isEmpty(){
        return root == null;
    }
    
    public NodeBinaryTree<T> getRoot(){
        return root;
    }
    public void setRoot(NodeBinaryTree<T> root){
        this.root = root;
    }
    public boolean isLeaf(){
        if(!this.isEmpty()){
            return root.getLeft()==null && root.getRight() == null;
        }
        return false;
    }
    
    
    
    public void inOrden(){
        if(!this.isEmpty()){
            if(root.getLeft() != null){
                root.getLeft().inOrden();
            }
            System.out.println(this.root.getContent());
            if(root.getRight()!= null){
                root.getRight().inOrden();
            }
            
        }
        
    }
    
    
    
    public int countLeavesRecursive() {
    // Caso Base: arbol vacio
    if (this.isEmpty()) {
        return 0;
    }

    //los sub-árboles izquierdo y derecho
    BinaryTree<T> leftTree = this.root.getLeft();
    BinaryTree<T> rightTree = this.root.getRight();

    //caso Base 2: es hoja.
    //  una hoja ocurre si ambos sub-árboles son nulos O están vacíos.
    boolean isLeftEmpty = (leftTree == null || leftTree.isEmpty());
    boolean isRightEmpty = (rightTree == null || rightTree.isEmpty());

    if (isLeftEmpty && isRightEmpty) {
        return 1;
    }

    //sumar hojas de los hijos
    int count = 0;
    if (!isLeftEmpty) {
        count += leftTree.countLeavesRecursive();
    }
    if (!isRightEmpty) {
        count += rightTree.countLeavesRecursive();
    }
    
    return count;
    }
    
}

