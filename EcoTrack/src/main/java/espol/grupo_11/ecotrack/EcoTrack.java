/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package espol.grupo_11.ecotrack;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
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

        String[] nombres = new String[] {"Alborada","Bastión Popular","Cerro Colorado","El Fortín","El Guasmo","La Alborada","La Merced","Los Esteros","Los Samanes","Miraflores","Octava","Samborondón","San Eduardo","San Francisco","Tarqui","Urdesa"};
        Random random = new Random(12345);

        int idx = 1;
        for (String nombreZona : nombres) {
            String codigo = String.format("GYE-0%02d", idx);
            int cantidad = 15 + random.nextInt(16); // 15..30
            CircularDoubleLinkedList<Residuo> lista = crearListaResiduos(codigo, cantidad, idx * 1000, random);
            Zona z = new Zona(lista, nombreZona, codigo);
            if (idx <= 8) {
                z.setUltimaRecoleccion(LocalDateTime.of(2026, 1, 9, 8, 0));
            } else {
                z.setUltimaRecoleccion(LocalDateTime.of(2026, 1, 10, 8, 0));
            }
            zonasUrbanas.add(z);
            System.out.println("Zona agregada: " + codigo + " | nombre=" + nombreZona + " | residuos=" + cantidad + " | ultimaRecoleccion=" + z.getUltimaRecoleccion());
            idx++;
        }

        System.out.println("=== PriorityQueue (antes de iniciar recolección) ===");
        imprimirZonasPorPrioridad(zonasUrbanas);
        Deque<Residuo> pilaResiduos = new ArrayDeque<>();
        CentroRecoleccion centro = new CentroRecoleccion(zonasUrbanas, pilaResiduos);
        ControladorCentroRecoleccion controladorCentroRecoleccion = new ControladorCentroRecoleccion(centro);
        controladorCentroRecoleccion.iniciarRecoleccionCarrito();
    }

    private static CircularDoubleLinkedList<Residuo> crearListaResiduos(String codigoZona, int cantidad, int baseId, Random random) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("Cantidad debe ser positiva.");
        }

        CircularDoubleLinkedList<Residuo> lista = new CircularDoubleLinkedList<>();
        TipoResiduo[] tipos = TipoResiduo.values();

        for (int i = 1; i <= cantidad; i++) {
            TipoResiduo tipo = tipos[random.nextInt(tipos.length)];
            double peso = Math.round((0.05 + (random.nextDouble() * 4.95)) * 100.0) / 100.0; // 0.05 - 5.0 kg, 2 decimales
            int prioridadAmbiental = 1 + random.nextInt(5); // 1-5
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

