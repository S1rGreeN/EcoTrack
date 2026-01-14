package espol.grupo_11.ecotrack.Utilitarios;

import java.io.Serializable;
import java.util.Comparator;

public class PriorityQueue<E> extends Heap<E> implements Serializable {
    private static final long serialVersionUID = 1L;

    public PriorityQueue(Comparator<E> comp) { // constructor con comparador
         super(comp); 
    }

    public PriorityQueue(Iterable<? extends E> c) {  // constructor con coleccion (ArrayList, LinkedList, etc...)
        super(c); 
    }

    public PriorityQueue(Iterable<? extends E> c, Comparator<E> comp) { // constructor con coleccion y comparador
        super(c, comp); 
    }

    public Comparator<E> comparator() { //Retorna el comparador utilizado
        return super.comparator(); 
    }

    public boolean add(E e) { //Agregar elemento a la cola de prioridad
        return super.add(e); 
    }

    public E poll() { //Eliminar y retornar el elemento con mayor prioridad
        return super.poll(); 
    }

    public E peek() { //Retornar el elemento con mayor prioridad sin eliminarlo
        return super.peek(); 
    }

    public boolean isEmpty() { //Verificar si la cola de prioridad esta vacia
        return super.isEmpty(); 
    }

    public int size() { //Retornar el tamaño de la cola de prioridad
        return super.size(); 
    }

    public boolean remove(E e) { //Eliminar un elemento específico de la cola de prioridad
        return super.remove(e); 
    }

    public boolean contains(E e) { //Verificar si la cola de prioridad contiene un elemento específico
        return super.contains(e); 
    }
}
