package espol.grupo_11.ecotrack.Modelo;

import espol.grupo_11.ecotrack.Utilitarios.CircularDoubleLinkedList;

import java.io.Serializable;
import java.time.LocalDateTime;


public class Zona implements Serializable{
    private CircularDoubleLinkedList<Residuo> listaResiduos;
    private String codigo; //Definir si usamos el codigo postal de la parroquia o el codigo postal de cada zona de guayaquil.
    private int cantidadResiduosRecolectados;
    private LocalDateTime ultimaRecoleccion; 
    /*Definir si se usa por dias con hilos con varios procesos de recoleccion o si 
    se reinicia por semanas con hilos(lo veo viable, es lo mas realista, 
    ya que un camion de basura pasa por toda la ciudad en la semana).  */

    public Zona(CircularDoubleLinkedList<Residuo> listaResiduos, String codigo){
        this.listaResiduos = listaResiduos;
        this.codigo = codigo;
        this.cantidadResiduosRecolectados = 0;
        this.ultimaRecoleccion = null;
    }
    public CircularDoubleLinkedList<Residuo> getListaResiduos(){return listaResiduos;}
    public String getCodigo(){return codigo;}
    public int getCantidadResiduosRecolectados(){return cantidadResiduosRecolectados;}
    public LocalDateTime getUltimaRecoleccion(){return ultimaRecoleccion;}

    public int getPrioridadAtencion(){
        return listaResiduos.size();
    }

    //Metodo Auxiliar para pruebas
    public void setUltimaRecoleccion(LocalDateTime ultimaRecoleccion){
        this.ultimaRecoleccion = ultimaRecoleccion;
    }

   
    public void actualizarRecoleccion(){
        this.cantidadResiduosRecolectados ++;
        this.ultimaRecoleccion = LocalDateTime.now();
    }
    public Residuo removeFirstListaResiduos(){
        return this.listaResiduos.removeFirst();
    }

    public String toString(){
        return "Zona{" + "codigo=" + codigo + ", cantidadResiduosRecolectados=" + cantidadResiduosRecolectados +
         ", ultimaRecoleccion=" + ultimaRecoleccion + '}';
    }
    /*public boolean botarResiduo(Residuo nuevoResiduo){
        if(nuevoResiduo == null){return false;}
        listaResiduos.addLast(nuevoResiduo);
        return true;
    } */
    

    /*Cuando el metodo de reloeccion se implemente, 
    se debe actualizar la ultimaRecoleccion y la cantidadResiduosRecolectados
    a su vez que se vacia la pila del centro de recoleccion.
    */
}
