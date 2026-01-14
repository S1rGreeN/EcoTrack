/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package espol.grupo_11.ecotrack;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;
import java.util.Comparator;
import espol.grupo_11.ecotrack.Utilitarios.PriorityQueue;
import espol.grupo_11.ecotrack.Utilitarios.SerializadorEcoTrack;
import espol.grupo_11.ecotrack.Controlador.ControladorCentroRecoleccion;
import espol.grupo_11.ecotrack.Modelo.CentroRecoleccion;
import espol.grupo_11.ecotrack.Modelo.Residuo;
import espol.grupo_11.ecotrack.Modelo.Residuo.TipoResiduo;
import espol.grupo_11.ecotrack.Modelo.Zona;
import espol.grupo_11.ecotrack.Utilitarios.CircularDoubleLinkedList;
import espol.grupo_11.ecotrack.Utilitarios.LinkedList; 
import espol.grupo_11.ecotrack.Utilitarios.ArrayList;

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
        Comparator<Residuo> compPeso = new Comparator<Residuo>() {
            @Override
            public int compare(Residuo r1, Residuo r2) {
                return Double.compare(r2.getPeso(), r1.getPeso());
            }
        };


        String[] nombres = new String[] {"Alborada","Bastión Popular","Cerro Colorado","El Fortín","El Guasmo","La Alborada","La Merced","Los Esteros","Los Samanes","Miraflores","Octava","Samborondón","San Eduardo","San Francisco","Tarqui","Urdesa"};
        Random random = new Random(12345);

        int idx = 1;
        for (String nombreZona : nombres) {
            String codigo = String.format("GYE-0%02d", idx);
            int cantidad = 15 + random.nextInt(16);
            CircularDoubleLinkedList<Residuo> lista = crearListaResiduos(codigo, cantidad, idx * 1000, random);
            Zona z = new Zona(lista, nombreZona, codigo);
            if (idx <= 8) {
                z.setUltimaRecoleccion(LocalDateTime.of(2026, 1, 10, 14, 30));
            } else {
                z.setUltimaRecoleccion(LocalDateTime.of(2026, 1, 9, 14, 30));
            }
            zonasUrbanas.add(z);
            idx++;
        }

        Deque<Residuo> pilaResiduos = new ArrayDeque<>();
        CentroRecoleccion centro = new CentroRecoleccion(zonasUrbanas, pilaResiduos);
        ControladorCentroRecoleccion controlador = new ControladorCentroRecoleccion(centro);
        

        Thread hiloGenerador = new Thread(() -> {
            java.util.Random rnd = new java.util.Random();
            while (true) {
                for (int g = 0; g < 2; g++) {
                    LinkedList<Zona> listaZonas;
                    synchronized (centro.getZonasUrbanas()) {
                        listaZonas = new LinkedList<Zona>();
                        listaZonas.addAll(centro.getZonasUrbanas());
                    }
                    if (listaZonas.isEmpty()) break;
                    Zona zonaRandom = listaZonas.get(rnd.nextInt(listaZonas.size()));
                    TipoResiduo[] tiposArr = TipoResiduo.values();
                    TipoResiduo tipoRand = tiposArr[rnd.nextInt(tiposArr.length)];
                    double pesoRand = Math.round((0.05 + (rnd.nextDouble() * 4.95)) * 100.0) / 100.0;
                    int prioRand = 1 + rnd.nextInt(5);
                    String idRand = "R-" + zonaRandom.getCodigo() + "-AUTO-" + Math.abs(java.util.UUID.randomUUID().toString().hashCode());
                    String nombreRand = "AutoResiduo-" + idRand;
                    boolean ok = controlador.nuevoResiduo(idRand, nombreRand, tipoRand, pesoRand, zonaRandom.getCodigo(), prioRand);
                    if (!ok) {
                    }
                }
                try {
                    Thread.sleep(15000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        //hiloGenerador.setDaemon(true);//Investigar HiloDemonio en Android
        hiloGenerador.start();

        java.util.Scanner scanner = new java.util.Scanner(System.in);
        int opcion = -1;
        do {
            System.out.println("\n===== Menú =====");
            System.out.println("1. Iniciar Carrito");
            System.out.println("2. Generar Residuo");
            System.out.println("3. Ver informacion de las zonas");
            System.out.println("4. Ver estadisticas");

            System.out.println("5. Residuos generados");
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
                    controlador.iniciarRecoleccionCarrito();
                    System.out.println("Carrito iniciado.");
                    break;
                case 2:
                    System.out.println("Zonas disponibles:");
                    int listaIndices = 1;
                    for (Zona z : zonasUrbanas) {
                        System.out.println(listaIndices + ". " + z.getNombre() + " (" + z.getCodigo() + ")");
                        listaIndices++;
                    }
                    System.out.print("Seleccione zona (número o nombre): ");
                    String seleccion = scanner.nextLine().trim();

                    Zona zonaSeleccionada = null;
                    try {
                        int selIdx = Integer.parseInt(seleccion);
                        if (selIdx >= 1 && selIdx < listaIndices) {
                            int i = 1;
                            for (Zona z : zonasUrbanas) {
                                if (i == selIdx) { zonaSeleccionada = z; break; }
                                i++;
                            }
                        }
                    } catch (NumberFormatException e) {
                        for (Zona z : zonasUrbanas) {
                            if (z.getNombre().equalsIgnoreCase(seleccion) || z.getNombre().toLowerCase().contains(seleccion.toLowerCase())) {
                                zonaSeleccionada = z; break;
                            }
                        }
                    }

                    if (zonaSeleccionada == null) {
                        System.out.println("Zona no encontrada: " + seleccion);
                        break;
                    }

                    String idResiduo = "R-" + zonaSeleccionada.getCodigo() + "-" + (zonaSeleccionada.getListaResiduos().size() + 1);
                    System.out.print("Nombre del residuo: ");
                    String nombreResiduo = scanner.nextLine().trim();

                    System.out.println("Tipos disponibles:");
                    TipoResiduo[] tipos = TipoResiduo.values();
                    for (int i = 0; i < tipos.length; i++) {
                        System.out.println((i+1) + ". " + tipos[i].getNombre());
                    }
                    System.out.print("Seleccione tipo (número): ");
                    int indiceTipo = 1;
                    try { indiceTipo = Integer.parseInt(scanner.nextLine().trim()); } catch (Exception e) { indiceTipo = 1; }
                    if (indiceTipo < 1 || indiceTipo > tipos.length) indiceTipo = 1;
                    TipoResiduo tipoSeleccionado = tipos[indiceTipo-1];

                    System.out.print("Peso (kg): ");
                    double peso = 0.1;
                    try { peso = Double.parseDouble(scanner.nextLine().trim()); } catch (Exception e) { peso = 0.1; }

                    System.out.print("Prioridad (1-5): ");
                    int prioridad = 1;
                    try { prioridad = Integer.parseInt(scanner.nextLine().trim()); } catch (Exception e) { prioridad = 1; }

                    boolean agregado = controlador.nuevoResiduo(idResiduo, nombreResiduo, tipoSeleccionado, peso, zonaSeleccionada.getCodigo(), prioridad);
                    System.out.println("Residuo agregado a zona " + zonaSeleccionada.getNombre() + " (" + zonaSeleccionada.getCodigo() + "): " + agregado);
                    break;
                case 3: {
                    PriorityQueue<Zona> copia = new PriorityQueue<>(zonasUrbanas.comparator());
                    copia.addAll(zonasUrbanas);
                    ArrayList<Zona> listaOrden = new ArrayList<>();
                    int indiceZona = 1;
                    while (!copia.isEmpty()) {
                        Zona z = copia.poll();
                        listaOrden.addLast(z);
                        System.out.println(indiceZona + ". " + z.getNombre() + " (" + z.getCodigo() + ") | residuosEnCalle=" + z.getListaResiduos().size() + " | recolectados=" + z.getCantidadResiduosRecolectados());
                        indiceZona++;
                    }
                    System.out.print("Seleccione zona para ver residuos (número o nombre): ");
                    String seleccionZona = scanner.nextLine().trim();
                    Zona zonaSeleccionada2 = null;
                    int indiceSeleccionZona = -1;
                    try {
                        indiceSeleccionZona = Integer.parseInt(seleccionZona);
                    } catch (NumberFormatException e) {
                        indiceSeleccionZona = -1;
                    }
                    if (indiceSeleccionZona >= 1 && indiceSeleccionZona <= listaOrden.size()) {
                        zonaSeleccionada2 = listaOrden.get(indiceSeleccionZona - 1);
                    } else {
                        for (Zona z : listaOrden) {
                            if (z.getNombre().equalsIgnoreCase(seleccionZona) || z.getNombre().toLowerCase().contains(seleccionZona.toLowerCase()) || z.getCodigo().equalsIgnoreCase(seleccionZona)) {
                                zonaSeleccionada2 = z; break;
                            }
                        }
                    }
                    if (zonaSeleccionada2 == null) {
                        System.out.println("Zona no encontrada: " + seleccionZona);
                        break;
                    }
                    System.out.println("Residuos en zona " + zonaSeleccionada2.getNombre() + " (" + zonaSeleccionada2.getCodigo() + "): ");
                    java.util.Iterator<Residuo> iteradorResiduos = zonaSeleccionada2.getListaResiduos().iterator();
                    if (!iteradorResiduos.hasNext()) {
                        System.out.println("  (sin residuos)");
                    } else {
                        int contador = 1;
                        while (iteradorResiduos.hasNext()) {
                            Residuo r = iteradorResiduos.next();
                            System.out.println("  " + contador + ". " + r.getNombre() + " (" + r.getId() + ") | tipo=" + r.getTipo().getNombre() + " | peso=" + r.getPeso() + "kg | prioridad=" + r.getPrioridadAmbiental());
                            contador++;
                        }
                    }
                }
                break;
                case 4:
                    boolean mapasVacios = true;
                    int totalTipo = 0;
                    for (TipoResiduo t : centro.getMapaListaResiduosPorTipo().keySet()) {
                        totalTipo += centro.getMapaListaResiduosPorTipo().get(t).size();
                    }
                    int totalZona = 0;
                    for (String z : centro.getMapaListaResiduosPorZona().keySet()) {
                        totalZona += centro.getMapaListaResiduosPorZona().get(z).size();
                    }
                    int totalPrioridad = 0;
                    for (String p : centro.getMapaListaResiduosPorPrioridadAmbiental().keySet()) {
                        totalPrioridad += centro.getMapaListaResiduosPorPrioridadAmbiental().get(p).size();
                    }
                    int totalCola = 0;
                    if (centro.getColaResiduosRecolectadosPesos() != null) {
                        totalCola = centro.getColaResiduosRecolectadosPesos().size();
                    }
                    if ( (centro.getPilaResiduos() != null && !centro.getPilaResiduos().isEmpty()) || totalTipo>0 || totalZona>0 || totalPrioridad>0 || totalCola>0) {
                        mapasVacios = false;
                    }

                    if (mapasVacios) {
                        System.out.println("primero realiza una recoleccion");
                        break;
                    }

                    System.out.print("¿Qué estadística desea ver? (peso/tipo/zona/prioridad): ");
                    String estadisticaTipo = scanner.nextLine().trim().toLowerCase();
                    switch (estadisticaTipo) {
                        case "peso": {
                            PriorityQueue<Residuo> copiaCola = new PriorityQueue<>(centro.getColaResiduosRecolectadosPesos(), compPeso);

                            if (copiaCola == null || copiaCola.isEmpty()) {
                                System.out.println("No hay residuos procesados por peso.");
                                break;
                            }
                            int ct = 1;
                            while (!copiaCola.isEmpty()) {
                                Residuo r = copiaCola.poll();
                                System.out.println(ct + ". " + r.getId() + " | " + r.getNombre() + " | peso=" + r.getPeso() + "kg | zona=" + r.getZona() + " | prioridad=" + r.getPrioridadAmbiental());
                                ct++;
                            }
                        }
                        break;
                        case "tipo": {
                            for (TipoResiduo t : centro.getMapaListaResiduosPorTipo().keySet()) {
                                LinkedList<Residuo> lista = centro.getMapaListaResiduosPorTipo().get(t);
                                System.out.println("Tipo " + t.getNombre() + ":");
                                java.util.Iterator<Residuo> it = lista.iterator();
                                if (!it.hasNext()) {
                                    System.out.println("  (sin residuos)");
                                } else {
                                    while (it.hasNext()) {
                                        Residuo r = it.next();
                                        System.out.println("  - " + r.getNombre() + " (" + r.getId() + ")");
                                    }
                                }
                                System.out.println();
                            }
                        }
                        break;
                        case "zona": {
                            for (String z : centro.getMapaListaResiduosPorZona().keySet()) {
                                LinkedList<Residuo> lista = centro.getMapaListaResiduosPorZona().get(z);
                                System.out.println("Zona " + z + ":");
                                java.util.Iterator<Residuo> it = lista.iterator();
                                if (!it.hasNext()) {
                                    System.out.println("  (sin residuos)");
                                } else {
                                    while (it.hasNext()) {
                                        Residuo r = it.next();
                                        System.out.println("  - " + r.getNombre() + " (" + r.getId() + ")");
                                    }
                                }
                                System.out.println();
                            }
                        }
                        break;
                        case "prioridad": {
                            for (String p : centro.getMapaListaResiduosPorPrioridadAmbiental().keySet()) {
                                LinkedList<Residuo> lista = centro.getMapaListaResiduosPorPrioridadAmbiental().get(p);
                                System.out.println("Prioridad " + p + ":");
                                java.util.Iterator<Residuo> it = lista.iterator();
                                if (!it.hasNext()) {
                                    System.out.println("  (sin residuos)");
                                } else {
                                    while (it.hasNext()) {
                                        Residuo r = it.next();
                                        System.out.println("  - " + r.getNombre() + " (" + r.getId() + ")");
                                    }
                                }
                                System.out.println();
                            }
                        }
                        break;
                        default:
                            System.out.println("Opción de estadística inválida.");
                    }
                    break;
                case 5:
                    java.util.List<Residuo> listaResiduosGenerados = centro.getResiduosGenerados();
                    if (listaResiduosGenerados == null || listaResiduosGenerados.isEmpty()) {
                        System.out.println("No hay residuos generados todavía.");
                        break;
                    }
                    System.out.println("Residuos generados (manuales y automáticos):");
                    int indiceGenerado = 1;
                    for (Residuo r : listaResiduosGenerados) {
                        System.out.println("  " + indiceGenerado + ". " + r.getNombre() + " (" + r.getId() + ") | tipo=" + r.getTipo().getNombre() + " | peso=" + r.getPeso() + "kg | zona=" + r.getZona() + " | prioridad=" + r.getPrioridadAmbiental());
                        indiceGenerado++;
                    }
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
            double peso = Math.round((0.05 + (random.nextDouble() * 4.95)) * 100.0) / 100.0;
            int prioridadAmbiental = 1 + random.nextInt(5);
            String id = "R-" + codigoZona + "-" + (baseId + i);
            String nombre = "Residuo-" + i + "-" + tipo.getNombre();

            lista.addLast(new Residuo(id, nombre, tipo, peso, codigoZona, prioridadAmbiental));
        }
        return lista;
    }
/* 
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
    }*/
}

