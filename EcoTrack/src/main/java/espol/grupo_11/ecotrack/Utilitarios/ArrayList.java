package espol.grupo_11.ecotrack.Utilitarios;
import java.io.Serializable;
import java.util.Iterator;

public class ArrayList<E> implements List<E>, Serializable{
    private static final long serialVersionUID = 1L;

    private E[] elements = null; //arreglo de elementos genericos
    private int capacity = 100;
    private int effectiveSize = 0;
    
    public ArrayList (){
        
        //int[] a;
        
        //a = new int[100];
        
        //elements = new E[100]; NO FUNCIONA
        
        elements = (E[])(new Object[capacity]); // SI FUNCIONA con Casting permitido con el arrayList
        
    }
    
    private boolean isFull(){
        return effectiveSize == capacity;
    }
   
    @Override
    public boolean addFirst(E e) {
        
        // no se insertan nulos
        if(e==null){
            return false;
        } else if (isEmpty()){
            elements[0] = e;
            effectiveSize++;
            //elements[effectiveSize ++] = e; //otra forma de hacer las 2 lineas anteriores
            return true;
        } else if (isFull()){
            addCapacity();
        }
        
        for (int i=effectiveSize-1; i >=0; i--){
            elements[i+1]=elements[i]; //bit shifting 
            //desplazamiento de valores hacia la derecha, 
            //debe empezar de atras hacia adelante
        }
        elements[0] = e;
        effectiveSize++;
        return true;
    }

    @Override
    public boolean addLast(E e) {
       if (e == null) {
            return false;
        } else if (isFull()) {
            addCapacity();
        }
        elements[effectiveSize] = e;
        effectiveSize++;
        //elements[effectiveSize++] = e; otra forma de hacer las 2 lineas anteriores
        return true;
    }

    @Override
    public E removeFirst() {
        if(isEmpty()){
            return null;
        }
        E primerito = elements[0];
        for(int i=1; i<effectiveSize; i++){
            elements[i-1]=elements[i]; //bit shifting
        }
        effectiveSize--;
        return primerito;
    }

    @Override
    public E removeLast() {
        if(isEmpty()){
            return null;
        }
        E ultimito = elements[effectiveSize-1];
        elements[effectiveSize-1]=null;
        effectiveSize--;
        return ultimito;
    }

    @Override
    public int size() {
        return effectiveSize;
    }

    @Override
    public boolean isEmpty() {
        return effectiveSize == 0;
    }

    @Override
    public void clear() {
        for(int i=0; i<effectiveSize; i++){
            elements[i]=null;
        }
        effectiveSize=0;
    }

    @Override
    public void add(int index, E element) {
        if (index < 0 || index > effectiveSize || element == null) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        } else if (isFull()) {
            addCapacity();
        }
        for (int i = effectiveSize; i > index; i--) {
            elements[i] = elements[i - 1];
            // elements[i+1] = elements[i]; MAL
        }
        elements[index] = element;
        effectiveSize++;
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index >= effectiveSize) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
        E removedElement = elements[index];
        for (int i = index + 1; i < effectiveSize; i++) {
            elements[i - 1] = elements[i];
        }
        elements[effectiveSize - 1] = null;
        effectiveSize--;
        return removedElement;
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= effectiveSize) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
        return elements[index];
    }

    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= effectiveSize || element == null) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
        E viejito = elements[index];
        elements[index] = element;
        return viejito;
    }

    private void addCapacity() {
        E[] tmp = (E[]) new Object[capacity * 2];
        for (int i = 0; i < capacity; i++){
            tmp[i] = elements[i];
        }
        elements = tmp;
        capacity = capacity * 2;
    }
    
    public String toString() {
        String s="";
        for (int i=0; i<effectiveSize; i++) {
            s+=elements[i]+", ";
        }
        return s;
    }
    
    public Iterator<E> iterator(){
            Iterator<E> it=new Iterator<E>() {
                int cursor = 0;
                @Override
                public boolean hasNext() {
                    return cursor < effectiveSize;
                }

                @Override
                public E next() {
                    E e=elements[cursor];
                    cursor++;
                    return e;
                }
            };
        return it;   
    }
}
