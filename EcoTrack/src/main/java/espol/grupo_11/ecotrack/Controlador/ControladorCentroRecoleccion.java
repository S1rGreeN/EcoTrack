package espol.grupo_11.ecotrack.Controlador;
import espol.grupo_11.ecotrack.Modelo.CentroRecoleccion;
import espol.grupo_11.ecotrack.Modelo.Residuo;
import espol.grupo_11.ecotrack.Modelo.Residuo.TipoResiduo;
import espol.grupo_11.ecotrack.Modelo.Zona;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Deque;
import java.util.Iterator;
import java.util.TreeMap;

import espol.grupo_11.ecotrack.Utilitarios.LinkedList;



public class ControladorCentroRecoleccion implements Serializable, Runnable  {
    private CentroRecoleccion centroRecoleccion;

    public ControladorCentroRecoleccion(CentroRecoleccion centroRecoleccion){
        this.centroRecoleccion = centroRecoleccion;
    }

    public CentroRecoleccion getCentroRecoleccion(){
        return this.centroRecoleccion;
    }

    /*Cuando el metodo de reloeccion se implemente, 
    se debe actualizar la ultimaRecoleccion y la cantidadResiduosRecolectados
    a su vez que se vacia la pila del centro de recoleccion.
    */
    public void run(){
        //Codigo heavy
        Iterator<Zona> it= centroRecoleccion.getZonasUrbanas().iterator();
        try{
            Thread.sleep(100);
        } catch(InterruptedException e){
            e.printStackTrace();
        }
        
        int contadorIteracionPQ = centroRecoleccion.getZonasUrbanas().size();
        
        while(it.hasNext() && contadorIteracionPQ>=0){
            Zona zonaActual = it.next();
            if(zonaActual.getUltimaRecoleccion() == null){
                --contadorIteracionPQ;
                int contador = 0;
                System.out.println("Recoleccion en zona: "+zonaActual.getCodigo());
                
                while(contador<20){
                    contador++;
                    Residuo copiaResiduo  = zonaActual.removeFirstListaResiduos();
                    if(copiaResiduo != null){
                        centroRecoleccion.addPilaResiduos(copiaResiduo);
                        centroRecoleccion.addColaResiduos(copiaResiduo);
                        zonaActual.actualizarRecoleccion();
                        
                    } else {
                        break;
                    }
                        //centroRecoleccion.addPilaResiduos(zonaActual.removeFirstListaResiduos());
                } 
                //zonaActual = it.next();
                try{
                    Thread.sleep(5);
                } catch(InterruptedException e){
                    System.out.println("Error en el hilo de recoleccion");
                }
            } else if(zonaActual.getUltimaRecoleccion().plusDays(2).isEqual(LocalDateTime.now()) 
                || zonaActual.getUltimaRecoleccion().plusDays(2).isBefore(LocalDateTime.now())){
                    --contadorIteracionPQ;
                int contador = 0;
                System.out.println("Recoleccion en zona: "+zonaActual.getCodigo());
                
                while(contador<20){
                    contador++;
                    Residuo copiaResiduo  = zonaActual.removeFirstListaResiduos();
                    if(copiaResiduo != null){
                        centroRecoleccion.addPilaResiduos(copiaResiduo);
                        centroRecoleccion.addColaResiduos(copiaResiduo);
                        zonaActual.actualizarRecoleccion();
                    } else {
                        break;
                    }
                        //centroRecoleccion.addPilaResiduos(zonaActual.removeFirstListaResiduos());
                } 
                //zonaActual = it.next();
                try{
                    Thread.sleep(50);
                } catch(InterruptedException e){
                    System.out.println("Error en el hilo de recoleccion");
                }
            }
            
        }
        centroRecoleccion.printZonasUrbanas();/*
        int contador =1;
        for(Residuo r : centroRecoleccion.getPilaResiduos()){
            System.out.println(contador + " " + r.toString());
            contador++;
        }*/
       System.out.println("Tamaño de la pila de residuos antes de procesar: " + centroRecoleccion.getPilaResiduos().size());
        boolean validacion = procesarResiduos();
        if(validacion){
            System.out.println("Tamaño de la pila de residuos luego de procesar: " + centroRecoleccion.getPilaResiduos().size());
        } else {
            System.out.println("No se pudo procesar los residuos del centro de recolección.");
        }
        int contadorTipo =0;
        for(TipoResiduo tipo: centroRecoleccion.getMapaListaResiduosPorTipo().keySet()){
            System.out.println("Tipo "+ tipo.getNombre() + " Tamaño lista: "+ centroRecoleccion.getMapaListaResiduosPorTipo().get(tipo).size());
            contadorTipo=contadorTipo+ centroRecoleccion.getMapaListaResiduosPorTipo().get(tipo).size();
        }
        System.out.println("Tama,ño total por tipo: "+ contadorTipo);
        int contadorZona =0;
        for(String zona: centroRecoleccion.getMapaListaResiduosPorZona().keySet()){
            System.out.println("Zona: "+ zona + " Tamaño lista: "+ centroRecoleccion.getMapaListaResiduosPorZona().get(zona).size());
            contadorZona=contadorZona+ centroRecoleccion.getMapaListaResiduosPorZona().get(zona).size();
        }
        System.out.println("Tamaño total por zona: "+ contadorZona);
        int contadorPrioridad =0;
        for(String prioridad: centroRecoleccion.getMapaListaResiduosPorPrioridadAmbiental().keySet()){
            System.out.println("Prioridad: "+ prioridad + " Tamaño lista: "+ centroRecoleccion.getMapaListaResiduosPorPrioridadAmbiental().get(prioridad).size());
            contadorPrioridad=contadorPrioridad+ centroRecoleccion.getMapaListaResiduosPorPrioridadAmbiental().get(prioridad).size();
        }
        System.out.println("Tamaño total por prioridad: "+ contadorPrioridad);
    }
    
    public void iniciarRecoleccionCarrito(){
        Thread hiloRecoleccion = new Thread(this);
        hiloRecoleccion.start();
    }


    public boolean procesarResiduos(){
        Deque<Residuo> pilaResiduos = centroRecoleccion.getPilaResiduos();
        if (pilaResiduos == null || pilaResiduos.isEmpty()) {
            return false;
        }

        TreeMap<TipoResiduo, LinkedList<Residuo>> mapaPorTipo = centroRecoleccion.getMapaListaResiduosPorTipo();
        TreeMap<String, LinkedList<Residuo>> mapaPorZona = centroRecoleccion.getMapaListaResiduosPorZona();
        TreeMap<String, LinkedList<Residuo>> mapaPorPrioridad = centroRecoleccion.getMapaListaResiduosPorPrioridadAmbiental();

        boolean procesado = false;
        Iterator<Residuo> it = pilaResiduos.iterator();
        while (it.hasNext()) {
            Residuo residuo = it.next();
            it.remove();
            procesado = true;

            // 1) Por tipo (las claves son los valores del enum)
            LinkedList<Residuo> pilaPorTipo = mapaPorTipo.get(residuo.getTipo());
            if (pilaPorTipo == null) {
                pilaPorTipo = new LinkedList<Residuo>();
                mapaPorTipo.put(residuo.getTipo(), pilaPorTipo);
            }
            pilaPorTipo.addFirst(residuo);

            // 2) Por zona
            String claveZona = residuo.getZona();
            if (claveZona == null || claveZona.isBlank()) {
                claveZona = "SIN_ZONA";
            }
            LinkedList<Residuo> pilaPorZona = mapaPorZona.get(claveZona);
            if (pilaPorZona == null) {
                pilaPorZona = new LinkedList<Residuo>();
                mapaPorZona.put(claveZona, pilaPorZona);
            }
            pilaPorZona.addFirst(residuo);

            // 3) Por prioridad ambiental (mapa usa String como clave)
            int prioridad = residuo.getPrioridadAmbiental();
            if (prioridad < 1) {
                prioridad = 1;
            } else if (prioridad > 5) {
                prioridad = 5;
            }
            String clavePrioridad = "Nivel-" + prioridad;

            LinkedList<Residuo> pilaPorPrioridad = mapaPorPrioridad.get(clavePrioridad);
            if (pilaPorPrioridad == null) {
                pilaPorPrioridad = new LinkedList<Residuo>();
                mapaPorPrioridad.put(clavePrioridad, pilaPorPrioridad);
            }
            pilaPorPrioridad.addFirst(residuo);
        }

        return procesado;
    } 


    public boolean nuevoResiduo(String id, String nombre, TipoResiduo tipo, double peso, String zona, int prioridad){
        if (id == null || id.isBlank() || nombre == null || nombre.isBlank() || tipo == null || peso <= 0 || zona == null || zona.isBlank()) {
            return false;
        }
        if (prioridad < 1) prioridad = 1;
        if (prioridad > 5) prioridad = 5;

        Residuo nuevo = new Residuo(id, nombre, tipo, peso, zona, prioridad);

        java.util.PriorityQueue<Zona> pq = centroRecoleccion.getZonasUrbanas();
        if (pq == null || pq.isEmpty()) {
            return false;
        }

        Zona zonaEncontrada = null;
        for (Zona z : pq) {
            if (zona.equals(z.getCodigo())) {
                zonaEncontrada = z;
                break;
            }
        }

        if (zonaEncontrada == null) {
            return false;
        }
        try {
            zonaEncontrada.getListaResiduos().addLast(nuevo);
        } catch (Exception e) {
            return false;
        }
        pq.remove(zonaEncontrada);
        pq.add(zonaEncontrada);

        return true;
    }




    
}
