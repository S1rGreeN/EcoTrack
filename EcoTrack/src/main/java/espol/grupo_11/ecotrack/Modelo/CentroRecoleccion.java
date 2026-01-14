package espol.grupo_11.ecotrack.Modelo;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Deque;
import espol.grupo_11.ecotrack.Utilitarios.PriorityQueue;
import espol.grupo_11.ecotrack.Utilitarios.TreeMap;

import espol.grupo_11.ecotrack.Modelo.Residuo.TipoResiduo;
import espol.grupo_11.ecotrack.Utilitarios.LinkedList;



public class CentroRecoleccion implements Serializable {
    private static final long serialVersionUID = 1L;
    private Deque<Residuo> pilaResiduos;

    private PriorityQueue<Zona> zonasUrbanas;
    private PriorityQueue<Residuo> colaResiduosRecolectadosPesos;
    private TreeMap<TipoResiduo, LinkedList<Residuo>> mapaListaResiduosPorTipo;
    private TreeMap<String, LinkedList<Residuo>> mapaListaResiduosPorZona;
    private TreeMap<String, LinkedList<Residuo>> mapaListaResiduosPorPrioridadAmbiental;
    private java.util.List<Residuo> residuosGenerados;
    public CentroRecoleccion(PriorityQueue<Zona> zonasUrbanas,Deque<Residuo> pilaResiduos){
        this.zonasUrbanas = zonasUrbanas;
        this.pilaResiduos = pilaResiduos;  
        this.colaResiduosRecolectadosPesos = new PriorityQueue<Residuo>(new Comparator<Residuo>(){
            @Override
            public int compare(Residuo r1, Residuo r2){
                return Double.compare(r2.getPeso(), r1.getPeso());
            }
        });
        this.mapaListaResiduosPorTipo = new TreeMap<>();
        for (TipoResiduo tipo : TipoResiduo.values()) {
            this.mapaListaResiduosPorTipo.put(tipo, new LinkedList<Residuo>());
        }
        this.mapaListaResiduosPorZona = new TreeMap<>();
        this.mapaListaResiduosPorPrioridadAmbiental = new TreeMap<>();
        this.residuosGenerados = new java.util.ArrayList<>();
    }
    public Deque<Residuo> getPilaResiduos(){return pilaResiduos;}
    public void addPilaResiduos(Residuo residuo){this.pilaResiduos.push(residuo);}

    // Registra residuos generados (manuales y automáticos)
    public void addResiduoGenerado(Residuo residuo) { if (residuo != null) this.residuosGenerados.add(residuo); }
    public java.util.List<Residuo> getResiduosGenerados() { return this.residuosGenerados; }
    public PriorityQueue<Zona> getZonasUrbanas(){return zonasUrbanas;}
    public void addColaResiduos(Residuo residuo){this.colaResiduosRecolectadosPesos.add(residuo);}

    public PriorityQueue<Residuo> getColaResiduosRecolectadosPesos() {
        return colaResiduosRecolectadosPesos;
    }

    

    public TreeMap<String, LinkedList<Residuo>> getMapaListaResiduosPorZona() {
        return mapaListaResiduosPorZona;
    }

    public TreeMap<TipoResiduo, LinkedList<Residuo>> getMapaListaResiduosPorTipo() {
        return mapaListaResiduosPorTipo;
    }
    public TreeMap<String, LinkedList<Residuo>> getMapaListaResiduosPorPrioridadAmbiental() {
        return mapaListaResiduosPorPrioridadAmbiental;
    }
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

