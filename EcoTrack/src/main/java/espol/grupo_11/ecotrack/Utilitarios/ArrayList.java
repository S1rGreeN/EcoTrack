package espol.grupo_11.ecotrack.Utilitarios;
import java.util.Iterator;

public class ArrayList<E> implements List<E>{

    private E[] elements = null; //arreglo de elementos genericos
    private int capacity = 100;
    private int effectiveSize;
    
    public ArrayList (){
        
        //int[] a;
        
        //a = new int[100];
        
        //elements = new E[100]; NO FUNCIONA
        
        elements = (E[])(new Object[capacity]); // SI FUNCIONA con Casting permitido con el arrayList
        effectiveSize = 0;
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
        }
        if (isFull()) {
            addCapacity();
        }
        elements[effectiveSize] = e;
        effectiveSize++;
        //elements[effectiveSize++] = e; otra forma de hacer las 2 lineas anteriores
        return true;
    }

    @Override
    public E removeFirst() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public E removeLast() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
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
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void add(int index, E element) {
        if (index < 0 || index > effectiveSize) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
        if (isFull()) {
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
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public E get(int index) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public E set(int index, E element) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
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
