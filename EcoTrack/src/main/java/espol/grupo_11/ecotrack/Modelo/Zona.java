package espol.grupo_11.ecotrack.Modelo;

import espol.grupo_11.ecotrack.Utilitarios.CircularDoubleLinkedList;
import java.util.Iterator;

public class Zona {
    CircularDoubleLinkedList<Residuo> listaResiduos;
    String codigo; //Definir si usamos el codigo postal de la parroquia o el codigo postal de cada zona de guayaquil.
    int cantidadResiduosRecolectados; 
    /*Definir si se usa por dias con hilos con varios procesos de recoleccion o si 
    se reinicia por semanas con hilos(lo veo viable, es lo mas realista, 
    ya que un camion de basura pasa por toda la ciudad en la semana).  */

    public Zona(CircularDoubleLinkedList<Residuo> listaResiduos, String codigo){
        this.listaResiduos = listaResiduos;
        this.codigo = codigo;
    }
    public CircularDoubleLinkedList<Residuo> getListaResiduos(){return listaResiduos;}
    public String getCodigo(){return codigo;}


    public boolean botarResiduo(Residuo nuevoResiduo){
        if(nuevoResiduo == null){return false;}
        listaResiduos.addLast(nuevoResiduo);
        return true;
    }
    public int getPrioridadAmbiental(){
        int contador = 0;
        Iterator<T> it = listaResiduos.iterator();
        if(!it.hasNext()){
            return 0;
        }
        Residuo prev = it.next();
        contador += prev.getPrioridadAmbiental();
        while(!it.hasNext()){
            Residuo current = it.next();
            contador += current.getPrioridadAmbiental();
        }
        return contador;
    }
}
