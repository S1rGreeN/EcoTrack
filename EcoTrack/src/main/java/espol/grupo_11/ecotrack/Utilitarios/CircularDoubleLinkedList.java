package espol.grupo_11.ecotrack.Utilitarios;
import java.util.Iterator;

public class CircularDoubleLinkedList<T> implements Iterable<T> {
    private DoublyNodeList<T> head; 
    public CircularDoubleLinkedList(){
        this.head = null;
    }

    public boolean isEmpty(){
        return head == null;
    }

    public int size(){
        if(isEmpty()){return 0;}
        int contador = 0;
        DoublyNodeList<T> current = head;
        do{
            contador++;
            current = current.getNext();
        } while(current!=head);
        return contador;
    }

    public boolean addLast(T content){
        if(content == null){ throw new IllegalArgumentException();}
        DoublyNodeList<T> nuevoNodo = new DoublyNodeList<>(content);
        if(isEmpty()){
            head = nuevoNodo;
            head.setPrevious(nuevoNodo);
            head.setNext(nuevoNodo);
            return true;
        } else {
            DoublyNodeList<T> last = head.getPrevious();
            nuevoNodo.setNext(head);
            nuevoNodo.setPrevious(last);
            last.setNext(nuevoNodo);
            head.setPrevious(nuevoNodo);
            return true;
        }
    }


    public boolean addFirst(T content){
        if(content == null){ throw new IllegalArgumentException();}
        addLast(content);
        head = head.getPrevious();
        return true;
    }

    public boolean remove(T content){
        if (isEmpty()) {return false;}

        DoublyNodeList<T> current = head;
        do {
            if (current.getContent().equals(content)) {
                if (current.getNext() == current) {
                    head = null;
                } else {
                    DoublyNodeList<T> prevNode = current.getPrevious();
                    DoublyNodeList<T> nextNode = current.getNext();
                    prevNode.setNext(nextNode);
                    nextNode.setPrevious(prevNode);
                    if (current == head) {
                        head = nextNode;
                    }
                }
                return true;
            }
            current = current.getNext();
        } while (current != head);

        return false;
    }
    //Definir si agregamos los metodos remove y add por indices

    @Override
    public Iterator<T> iterator() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'iterator'");
    }

}
