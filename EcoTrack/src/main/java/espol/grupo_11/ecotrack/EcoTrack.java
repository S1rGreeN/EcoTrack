/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package espol.grupo_11.ecotrack;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.PriorityQueue;
import java.util.Random;
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

        java.util.Scanner scanner = new java.util.Scanner(System.in);
        int opcion = -1;
        do {
            System.out.println("\n===== Menú =====");
            System.out.println("1. Iniciar Carrito");
            System.out.println("2. Generar Residuo");
            System.out.println("3. Ver info zonas (imprimirZonasPorPrioridad)");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            String linea = scanner.nextLine().trim();
            try {
                opcion = Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1:
                    controladorCentroRecoleccion.iniciarRecoleccionCarrito();
                    System.out.println("Carrito iniciado.");
                    break;
                case 2:
                    // Generar residuo interactivo — elegir por nombre o índice
                    System.out.println("Zonas disponibles:");
                    int listIndex = 1;
                    for (Zona z : zonasUrbanas) {
                        System.out.println(listIndex + ". " + z.getNombre() + " (" + z.getCodigo() + ")");
                        listIndex++;
                    }
                    System.out.print("Seleccione zona (número o nombre): ");
                    String seleccion = scanner.nextLine().trim();

                    Zona zonaSel = null;
                    // Intentar parsear como número
                    try {
                        int selIdx = Integer.parseInt(seleccion);
                        if (selIdx >= 1 && selIdx < listIndex) {
                            int i = 1;
                            for (Zona z : zonasUrbanas) {
                                if (i == selIdx) { zonaSel = z; break; }
                                i++;
                            }
                        }
                    } catch (NumberFormatException e) {
                        // no es número: buscar por nombre (case-insensitive, allow contains)
                        for (Zona z : zonasUrbanas) {
                            if (z.getNombre().equalsIgnoreCase(seleccion) || z.getNombre().toLowerCase().contains(seleccion.toLowerCase())) {
                                zonaSel = z; break;
                            }
                        }
                    }

                    if (zonaSel == null) {
                        System.out.println("Zona no encontrada: " + seleccion);
                        break;
                    }

                    System.out.print("ID del residuo: ");
                    String id = scanner.nextLine().trim();
                    System.out.print("Nombre del residuo: ");
                    String nombreResiduo = scanner.nextLine().trim();

                    System.out.println("Tipos disponibles:");
                    TipoResiduo[] tipos = TipoResiduo.values();
                    for (int i = 0; i < tipos.length; i++) {
                        System.out.println((i+1) + ". " + tipos[i].getNombre());
                    }
                    System.out.print("Seleccione tipo (número): ");
                    int tipoIdx = 1;
                    try { tipoIdx = Integer.parseInt(scanner.nextLine().trim()); } catch (Exception e) { tipoIdx = 1; }
                    if (tipoIdx < 1 || tipoIdx > tipos.length) tipoIdx = 1;
                    TipoResiduo tipoSel = tipos[tipoIdx-1];

                    System.out.print("Peso (kg): ");
                    double peso = 0.1;
                    try { peso = Double.parseDouble(scanner.nextLine().trim()); } catch (Exception e) { peso = 0.1; }

                    System.out.print("Prioridad (1-5): ");
                    int prioridad = 1;
                    try { prioridad = Integer.parseInt(scanner.nextLine().trim()); } catch (Exception e) { prioridad = 1; }

                    boolean agregado = controladorCentroRecoleccion.nuevoResiduo(id, nombreResiduo, tipoSel, peso, zonaSel.getCodigo(), prioridad);
                    System.out.println("Residuo agregado a zona " + zonaSel.getNombre() + " (" + zonaSel.getCodigo() + "): " + agregado);
                    break;
                case 3:
                    imprimirZonasPorPrioridad(zonasUrbanas);
                    break;
                case 0:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción inválida.");
            }

        } while (opcion != 0);
        scanner.close();
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

