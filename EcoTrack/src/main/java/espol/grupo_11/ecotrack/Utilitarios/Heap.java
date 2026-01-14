package espol.grupo_11.ecotrack.Utilitarios;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;


public class Heap<E> implements Iterable<E>, Serializable {
    private static final long serialVersionUID = 1L;

    private ArrayList<E> data;
    private transient Comparator<E> comparator;

    public Heap() { // constructor por defecto
        this.data = new ArrayList<>();
        this.comparator = null;
    }

    public Heap(Comparator<E> comp) {// constructor con comparador
        this.data = new ArrayList<>();
        this.comparator = comp;
    }

    public Heap(Iterable<? extends E> c) {// constructor con coleccion(ArrayList, LinkedList, etc...)
        this();
        if (c != null) {
            for (E e : c) add(e);
        }
    }

    public Heap(Iterable<? extends E> c, Comparator<E> comp) {// constructor con coleccion y comparador
        this(comp);
        if (c != null) {
            for (E e : c) add(e);
        }
    }

    public Comparator<E> comparator() { return comparator; } //Retorna el comparador utilizado

    public boolean isEmpty() { return data.isEmpty(); }//Verificar si el heap esta vacio usando el arraylist de data

    public int size() { return data.size(); }//Retornar el tamaño del heap usando el arraylist de data

    public boolean add(E item) {//Agregar elemento al heap
        if (item == null) throw new NullPointerException();//No se permiten elementos nulos
        data.addLast(item);//Agregar al final del arraylist
        swim(data.size() - 1);//Hacer swim en la posicion del nuevo elemento
        return true;
    }

    public void addAll(Iterable<? extends E> it) { //Agregar todos los elementos de una coleccion al heap
        if (it == null) return;
        for (E e : it) add(e);
    }

    public E peek() { //Retornar el elemento con mayor prioridad sin eliminarlo
        if (data.isEmpty()) return null;
        return data.get(0);
    }

    public E poll() {//Eliminar y retornar el elemento con mayor prioridad
        int n = data.size();
        if (n == 0) return null; // heap vacio
        if (n == 1) return data.removeLast();// solo un elemento
        E root = data.get(0);// guardar la raiz
        E last = data.removeLast();// eliminar el ultimo elemento
        data.set(0, last);// mover el ultimo a la raiz
        sink(0);// hacer sink en la raiz
        return root;
    }

    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        if (comparator != null)
            return comparator.compare(a, b);

        if (a instanceof Comparable)
            return ((Comparable<E>) a).compareTo(b);

        throw new IllegalStateException(
            "No comparator provided and elements are not Comparable"
        );
    }

    private void swim(int idx) {// hacer swim para mantener la propiedad del heap
        while (idx > 0) { // mientras no sea la raiz
            int parent = (idx - 1) / 2;
            if (compare(data.get(idx), data.get(parent)) < 0) {// si es menor que el padre
                swap(idx, parent);// intercambiar con el padre
                idx = parent;
            } else break;// no es menor que el padre
        }
    }

    private void sink(int idx) {// hacer sink para mantener la propiedad del heap
        int n = data.size();
        while (true) {// mientras tenga hijos
            int left = 2 * idx + 1;
            int right = left + 1;
            int smallest = idx;
            if (left < n && compare(data.get(left), data.get(smallest)) < 0) smallest = left;// hijo izquierdo es menor
            if (right < n && compare(data.get(right), data.get(smallest)) < 0) smallest = right;// hijo derecho es menor
            if (smallest != idx) {// si alguno de los hijos es menor
                swap(idx, smallest);// intercambiar con el menor
                idx = smallest;// continuar haciendo sink
            } else break;// ya esta en su lugar correcto
        }
    }

    private void swap(int i, int j) {// intercambiar dos elementos en el arraylist
        E tmp = data.get(i);// guardar temporalmente el elemento en i
        data.set(i, data.get(j));// mover el elemento en j a i
        data.set(j, tmp);// mover el temporal a j
    }

    private void heapify() {// reconstruir el heap desde cualquier arreglo
        int n = data.size();// tamaño del heap
        for (int i = (n / 2) - 1; i >= 0; i--) sink(i);// hacer sink desde el ultimo padre hasta la raiz
    }

    public boolean remove(E item) {//Eliminar un elemento específico del heap
        if (item == null) return false;//No se permiten elementos nulos
        int n = data.size();
        for (int i = 0; i < n; i++) {
            E e = data.get(i);
            if (item.equals(e)) {//Elemento encontrado
                data.remove(i);//Eliminar el elemento
                heapify();//Reconstruir el heap
                return true;
            }
        }
        return false;
    }

    public boolean contains(E item) {//Verificar si el heap contiene un elemento específico
        if (item == null) return false;
        int n = data.size();
        for (int i = 0; i < n; i++) {
            if (item.equals(data.get(i))) return true;//Elemento encontrado
        }
        return false;
    }

    public Iterator<E> iterator() {//Retornar un iterador para el heap
        return new Iterator<E>() {//Usamos un iterador simple basado en el arraylist de data
            private int idx = 0;
            @Override public boolean hasNext() { return idx < data.size(); }//Verificar si hay un siguiente elemento
            @Override public E next() {//Retornar el siguiente elemento
                if (!hasNext()) throw new NoSuchElementException();//Si no hay siguiente, lanzar excepcion
                return data.get(idx++);//Retornar el elemento y avanzar el indice
            }
        };
    }
}
