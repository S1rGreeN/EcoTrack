package espol.grupo_11.ecotrack.Modelo;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Deque;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;

import espol.grupo_11.ecotrack.Modelo.Residuo.TipoResiduo;



public class CentroRecoleccion implements Serializable {
    private Deque<Residuo> pilaResiduos;

    private PriorityQueue<Zona> zonasUrbanas;
    private PriorityQueue<Residuo> colaResiduosRecolectadosPesos;
    private Map<TipoResiduo,Deque<Residuo>> mapaPilasResiduosPorTipo;
    private Map<String,Deque<Residuo>> mapaPilasResiduosPorZona;
    private Map<String,Deque<Residuo>> mapaPilasResiduosPorPrioridadAmbiental;
    public CentroRecoleccion(PriorityQueue<Zona> zonasUrbanas,Deque<Residuo> pilaResiduos ){
        this.zonasUrbanas = zonasUrbanas;
        this.pilaResiduos = pilaResiduos;  
        this.colaResiduosRecolectadosPesos = new PriorityQueue<>(new Comparator<Residuo>(){
            @Override
            public int compare(Residuo r1, Residuo r2){
                return Double.compare(r2.getPeso(), r1.getPeso());
            }
        });
    }
    public Deque<Residuo> getPilaResiduos(){return pilaResiduos;}
    public void addPilaResiduos(Residuo residuo){this.pilaResiduos.push(residuo);}
    public PriorityQueue<Zona> getZonasUrbanas(){return zonasUrbanas;}
    public void addColaResiduos(Residuo residuo){this.colaResiduosRecolectadosPesos.add(residuo);}
    public void printZonasUrbanas(){
        PriorityQueue<Zona> copia = new PriorityQueue<>(new Comparator<Zona>(){
            @Override
            public int compare(Zona z1, Zona z2){
                return (z1.getCantidadResiduosRecolectados()-z1.getListaResiduos().size()) - (z2.getCantidadResiduosRecolectados()-z2.getListaResiduos().size());
            }
        });
        copia.addAll(zonasUrbanas);

        while (!copia.isEmpty()) {
            Zona z = copia.poll();
            int pendientes = z.getListaResiduos().size();
            int recolectados = z.getCantidadResiduosRecolectados();
            int score = recolectados - pendientes;
            System.out.println("Zona " + z.getCodigo() + " | residuosEnCalle=" + pendientes + " | recolectados=" + recolectados + " | score=" + score);
        }
    }

}
