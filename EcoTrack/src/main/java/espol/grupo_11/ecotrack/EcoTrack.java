/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package espol.grupo_11.ecotrack;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;
import java.util.Comparator;

import espol.grupo_11.ecotrack.Controlador.ControladorCentroRecoleccion;
import espol.grupo_11.ecotrack.Modelo.CentroRecoleccion;
import espol.grupo_11.ecotrack.Modelo.Residuo;
import espol.grupo_11.ecotrack.Modelo.Residuo.TipoResiduo;
import espol.grupo_11.ecotrack.Modelo.Zona;
import espol.grupo_11.ecotrack.Utilitarios.CircularDoubleLinkedList;
/**
 *
 * @author Usuario
 */
public class EcoTrack {

    public static void main(String[] args) {
        PriorityQueue<Zona> zonasUrbanas = new PriorityQueue<Zona>(new Comparator<Zona>(){
            @Override
            public int compare(Zona z1, Zona z2){
                return (z1.getCantidadResiduosRecolectados()-z1.getListaResiduos().size()) - (z2.getCantidadResiduosRecolectados()-z2.getListaResiduos().size());
            }
        });

        // Ejemplos de zonas con 18-25 residuos cada una (listas para insertar en la PriorityQueue)
        zonasUrbanas.add(new Zona(crearListaResiduos("URB-NORTE-01", 20, 1000), "URB-NORTE-01"));
        zonasUrbanas.add(new Zona(crearListaResiduos("URB-CENTRO-07", 18, 2000), "URB-CENTRO-07"));
        zonasUrbanas.add(new Zona(crearListaResiduos("URB-SUR-12", 25, 3000), "URB-SUR-12"));
        zonasUrbanas.add(new Zona(crearListaResiduos("URB-ESTE-03", 22, 4000), "URB-ESTE-03"));
        int contador = 0;
        for(Zona z : zonasUrbanas){
            if(contador % 2 == 0){
                z.setUltimaRecoleccion(LocalDateTime.of(2026, 1, 8, 21, 14));
                System.out.println("Zona agregada: "+z.getCodigo()+" con "+z.getListaResiduos().size()+" residuos.");
                contador++;
            } else{
                z.setUltimaRecoleccion(LocalDateTime.of(2026, 1, 9, 21, 14));
                System.out.println("Zona agregada: "+z.getCodigo()+" con "+z.getListaResiduos().size()+" residuos.");
                contador++;
            }

        }
        System.out.println("=== PriorityQueue (antes de iniciar recolección) ===");
        imprimirZonasPorPrioridad(zonasUrbanas);

        Deque<Residuo> pilaResiduos = new ArrayDeque<>();
        CentroRecoleccion centro = new CentroRecoleccion(zonasUrbanas, pilaResiduos);
        ControladorCentroRecoleccion controladorCentroRecoleccion = new ControladorCentroRecoleccion(centro);

        // Para iniciar el hilo de recolección: ejecutar con argumento "run"
        // Ejemplo: mvn -q exec:java -Dexec.args="run"
        /*if (args.length > 0 && "run".equalsIgnoreCase(args[0])) {
            controladorCentroRecoleccion.iniciarRecoleccionCarrito();
        } */
        controladorCentroRecoleccion.iniciarRecoleccionCarrito();
        // 
        
    }  
    
    private static CircularDoubleLinkedList<Residuo> crearListaResiduos(String codigoZona, int cantidad, int baseId) {
        if (cantidad < 18 || cantidad > 25) {
            throw new IllegalArgumentException("Cada zona debe tener entre 18 y 25 residuos.");
        }

        CircularDoubleLinkedList<Residuo> lista = new CircularDoubleLinkedList<>();
        TipoResiduo[] tipos = TipoResiduo.values();

        for (int i = 1; i <= cantidad; i++) {
            TipoResiduo tipo = tipos[(i - 1) % tipos.length];
            double peso = 0.4 + ((i % 9) * 0.35); // peso determinístico (evita random)
            int prioridadAmbiental = 1 + (i % 5);
            String id = "R-" + codigoZona + "-" + (baseId + i);
            String nombre = "Residuo-" + i + "-" + tipo.getNombre();

            lista.addLast(new Residuo(id, nombre, tipo, peso, codigoZona, prioridadAmbiental));
        }
        return lista;
    }

    private static void imprimirZonasPorPrioridad(PriorityQueue<Zona> zonasUrbanas) {
        PriorityQueue<Zona> copia = new PriorityQueue<>(zonasUrbanas.comparator());
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

