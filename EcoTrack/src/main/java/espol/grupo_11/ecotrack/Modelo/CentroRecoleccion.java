package espol.grupo_11.ecotrack.Modelo;

import java.io.Serializable;
import java.util.Deque;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;



public class CentroRecoleccion implements Serializable {
    private Deque<Residuo> pilaResiduos;
    private Map<String,TreeMap<Integer,Residuo>> mapas;
    private PriorityQueue<Zona> zonasUrbanas;
    public CentroRecoleccion(PriorityQueue<Zona> zonasUrbanas,Deque<Residuo> pilaResiduos, Map<String,TreeMap<Integer,Residuo>> mapas ){
        this.zonasUrbanas = zonasUrbanas;
        this.pilaResiduos = pilaResiduos;
        this.mapas = mapas;
        
    }
    public Deque<Residuo> getPilaResiduos(){return pilaResiduos;}
    public Map<String,TreeMap<Integer,Residuo>> getMapas(){return mapas;}
    public PriorityQueue<Zona> getZonasUrbanas(){return zonasUrbanas;}

    


}
