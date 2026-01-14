package espol.grupo_11.ecotrack.Utilitarios;

import java.io.Serializable;

public class TreeMap<K extends Comparable<? super K>, V> implements Serializable {
    private static final long serialVersionUID = 1L;

    private class TreeMapNode implements Serializable { //clase interna de Nodo TreeMap
        private static final long serialVersionUID = 1L;
        K key; V value; TreeMapNode left, right;
        TreeMapNode(K k, V v){ key = k; value = v; }
    }

    private TreeMapNode root; //Raiz del arbol
    private int size = 0;

    public TreeMap() {}

    public V get(K key){ //obtener valor asociado a la clave
        if (key == null) return null;
        TreeMapNode cur = root; //igualamos el puntero a la raiz
        while(cur != null){ //Recorremos el arbol y comparamos cada elemento
            int cmp = key.compareTo(cur.key);
            if (cmp == 0) return cur.value;
            cur = (cmp < 0) ? cur.left : cur.right;
        }
        return null;
    }

    public V put(K key, V value){ //Ingresar un par clave-valor
        if (key == null) throw new NullPointerException("TreeMap does not support null keys");//No claves nulas
        if (root == null){ root = new TreeMapNode(key, value); size++; return null; } //Si el arbol esta vacio
        TreeMapNode cur = root; //current como raiz
        TreeMapNode parent = null; 
        int cmp = 0;
        while(cur != null){ //recorremos arbol y comparamos llaves hasta encontrar el elemento
            cmp = key.compareTo(cur.key);
            if (cmp == 0){ V old = cur.value; cur.value = value; return old; }//reemplazamos valor antiguo por argumento
            parent = cur;
            cur = (cmp < 0) ? cur.left : cur.right;
        }
        if (cmp < 0) parent.left = new TreeMapNode(key, value);// seteamos derecha e izquierda
        else parent.right = new TreeMapNode(key, value);
        size++;
        return null;
    }

    public boolean containsKey(K key){ return get(key) != null; } // verifica si existe la clave (tiene valor asociado)

    public int size(){ return size; } //retorna el tamaño del arbol

    public LinkedList<K> keySet(){ //retorna una lista con las claves del arbol
        LinkedList<K> keys = new LinkedList<>();
        inorderKeys(root, keys);
        return keys;
    }

    public LinkedList<V> values(){ //retorna una lista con los valores del arbol
        LinkedList<V> vals = new LinkedList<>();
        inorderValues(root, vals);
        return vals;
    }

    private void inorderKeys(TreeMapNode n, LinkedList<K> acc){ //retorna las claves en In-Orden (menor a mayor)
        if (n == null) return;
        inorderKeys(n.left, acc);
        acc.addLast(n.key);
        inorderKeys(n.right, acc);
    }

    private void inorderValues(TreeMapNode n, LinkedList<V> acc){ //retorna los valores en In-Orden (segun las claves de menor a mayor)
        if (n == null) return;
        inorderValues(n.left, acc);
        acc.addLast(n.value);
        inorderValues(n.right, acc);
    }
}
