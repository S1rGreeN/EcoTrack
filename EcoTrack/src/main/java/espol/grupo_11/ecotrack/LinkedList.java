package espol.grupo_11.ecotrack;
import java.util.Iterator;

public class LinkedList<E> implements List<E>{
    private NodeList<E> header;
    private NodeList<E> last;
    //private int size = 0;
    
    public LinkedList(){
        this.header=null;
        this.last=null;
    }
    public NodeList<E> getHeader() {
        return header;
    }

    public void setHeader(NodeList<E> header) {
        this.header = header;
    }

    public NodeList<E> getLast() {
        return last;
    }

    public void setLast(NodeList<E> last) {
        this.last = last;
    }

    @Override
    public boolean addFirst(E e) { //dado un elemento generico
        //size++;
        if(e!=null) {
            NodeList<E> newNode=new NodeList<>(e); // constructor crea un nodo aislado
            newNode.setNext(header); //El siguiente de ese nuevo nodo es Header
            this.setHeader(newNode); //actualizar el header de la lista
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean addLast(E e) {
        //size++;
        if(e!=null){
            NodeList<E> newNode = new NodeList<>(e);
            if(last !=null){
                last.setNext(newNode);
            }
            last=newNode;
            
            if(header == null){
                header = newNode;
            }
            return true;
        } else {
            return false;
        }  
    }
    
    private NodeList<E> getPrevious(NodeList<E> node){
        NodeList<E> previous = null;
        NodeList<E> n;
        
        for(n = header; n!=node; n= n.getNext()){
            previous = n;
        }
        return previous;
    }
    
    private void recorrerHaciAtras(){
        NodeList<E> n;

        for (n = last; n != header; n = this.getPrevious(n)){
            System.out.println(n);
        }
    } 
    
    @Override
    public E removeFirst() {
        if(size() == 0){
            return null;
        }
        E contenido = header.getContent();
        header = header.getNext();
        if(header == null){
            last = null;
        }
        return contenido;
    }

    @Override
    public E removeLast() {
        if(header == null){
            return null;
        }
        if(header == last){
            E contenido = last.getContent();
            header = null;
            last = null;
            return contenido;
        }
        NodeList<E> current = header;
        while(current.getNext() != last){
            current = current.getNext();
        }
        E contenido = last.getContent();
        last = setNext(null);
        return contenido;
    }

    @Override
    public int size() {
        // return size; // O(1)
        // primera parte declaro variables a iterar
        // segunda parte condicion logica
        // tercera parte instrucciones
        //for (int i=0, j=0; i < 10 && j >8; i++){
        //for (NodeList<E> n = header; n ! = null ; n = n.getNext()){
        
        // Complejidad lineal O(n) a pesar de que no es tan buena como O(1) evita que tenga que darle mantenimiento a size
        int size = 0;
        NodeList<E> n; //declaro un nodo viajero
        //declaro un nodo viajero, mientras sea diferente de null, se mueve al siguiente nodo
        
        for (n = header ; n!= null ; n = n.getNext( )) {
            size++;
        }
        return size;
    }

    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void add(int index, E element) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
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
    
    public String toString() {
        String s="";
        for (NodeList<E> n=header; n!=null; n=n.getNext()) {
            s+=n.getContent()+", ";
        }
        return s;
    }
    
    public Iterator<E> iterator(){
        Iterator<E> it=new Iterator<E>() {
                NodeList<E> cursor = header;
                
            @Override
            public boolean hasNext() {
                return cursor != null;
            }

            @Override
            public E next() {
                E e = cursor.getContent();
                cursor = cursor.getNext();
                return e;
            }
        };
        
        
        return it;
    }    
}
