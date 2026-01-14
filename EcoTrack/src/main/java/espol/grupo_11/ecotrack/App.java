package espol.grupo_11.ecotrack;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;
import java.util.Comparator;

import espol.grupo_11.ecotrack.Controlador.ControladorCentroRecoleccion;
import espol.grupo_11.ecotrack.Modelo.CentroRecoleccion;
import espol.grupo_11.ecotrack.Modelo.Residuo;
import espol.grupo_11.ecotrack.Modelo.Residuo.TipoResiduo;
import espol.grupo_11.ecotrack.Modelo.Zona;
import espol.grupo_11.ecotrack.Utilitarios.*; 

public class App {
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
        Scanner scanner = new Scanner(System.in);
        int opcion = -1;

        do {
            System.out.println("\n===== Menú =====");
            System.out.println("1. Iniciar Carrito");
            System.out.println("2. Generar Residuo");
            System.out.println("3. Ver información de zonas");
            System.out.println("4. Ver estadísticas");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine().trim());
            } catch (Exception e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1:
                    controlador.iniciarRecoleccionCarrito();
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
                        System.out.println("Zona no encontrada.");
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

                case 3:
                    System.out.print("Seleccione zona para ver residuos (número ): ");
                    int contadorZonas = 1;
                    for(String zona: nombres){
                        System.out.println(contadorZonas + ". " + zona);
                        contadorZonas++;
                    }
                    int seleccionZona = scanner.nextInt();
                    Zona z = SerializarEcoTrack.cargarZona(nombres[seleccionZona -1]);
                        if(z != null){
                            System.out.println("\n--- Zona: " + z.getNombre() + " (" + z.getCodigo() + ") ---");
                            CircularDoubleLinkedList<Residuo> listaResiduos = z.getListaResiduos();
                            int count = 1;
                            for (Residuo r : listaResiduos) {
                                System.out.println(count + ". " + r);
                                count++;
                            }
                        } else {
                            System.out.println("No se pudo cargar la zona: " + nombres[seleccionZona -1]);
                        }
                    break;
                case 4:
                    System.out.print("¿Qué estadística desea ver?\nPeso (1) \nTipo (2) \nZona (3) \nPrioridad Ambiental (4) \nSeleccione opción: ");
                    int estadisticaOpcion = scanner.nextInt();
                    switch (estadisticaOpcion) {
                        case 1:
                            PriorityQueue<Residuo> estadisticaPeso = SerializarEcoTrack.cargarEstadisticaPeso();
                            System.out.println("Estadística por peso:");
                            int countPeso = 1;
                            for (Residuo r : estadisticaPeso) {
                                System.out.println(countPeso + ". " + r);
                                countPeso++;
                            }
                            break;
                        case 2: 
                            TreeMap<TipoResiduo, LinkedList<Residuo>> estadisticaTipo = SerializarEcoTrack.cargarEstadisticaTipo();
                            System.out.println("Seleccione el tipo de residuo: \n1. Orgánico \n2. Plástico \n3. Vidrio \n4. Electrónico \n5. Metal \n6. Papel y/o Cartón");
                            ArrayList<String> tiposResiduo = new ArrayList<>();
                            tiposResiduo.addLast("Orgánico");
                            tiposResiduo.addLast("Plástico");
                            tiposResiduo.addLast("Vidrio");
                            tiposResiduo.addLast("Electrónico");
                            tiposResiduo.addLast("Metal");
                            tiposResiduo.addLast("Papel y/o Cartón");
                            int tipoResiduoSeleccionado = scanner.nextInt();
                            LinkedList<Residuo> listaPorTipo = estadisticaTipo.get(TipoResiduo.fromValor(tipoResiduoSeleccionado));
                            System.out.println("Estadística por tipo - " + tiposResiduo.get(tipoResiduoSeleccionado -1) + ":");
                            int countTipo = 1;
                            Iterator<Residuo> iterator = listaPorTipo.iterator();
                            while(iterator.hasNext()) {
                                Residuo r = iterator.next();
                                System.out.println(countTipo + ". " + r);
                                countTipo++;
                            }
                            break;
                        case 3:
                            TreeMap<String, LinkedList<Residuo>> estadisticaZona = SerializarEcoTrack.cargarEstadisticaZona();
                            System.out.println("Seleccione la zona: ");
                            int contadorZonasLista = 1;
                            for(String zona: nombres){
                                System.out.println(contadorZonasLista + ". " + zona);
                                contadorZonasLista++;
                            }

                            int zonaSeleccionadaEst = scanner.nextInt();
                            LinkedList<Residuo> listaPorZona = estadisticaZona.get(nombres[zonaSeleccionadaEst - 1]);
                            System.out.println("Estadística por zona - " + nombres[zonaSeleccionadaEst - 1] + ":");
                            int countZona = 1;
                            iterator = listaPorZona.iterator();
                            while(iterator.hasNext()) {
                                Residuo r = iterator.next();
                                System.out.println(countZona + ". " + r);
                                countZona++;
                            }
                            break;
                        case 4:
                            TreeMap<String, LinkedList<Residuo>> estadisticaPrioridad = SerializarEcoTrack.cargarEstadisticaPrioridad();
                            System.out.println("Seleccione la prioridad ambiental (1-5): ");
                            String prioridadSeleccionada = scanner.nextLine().trim();
                            LinkedList<Residuo> listaPorPrioridad = estadisticaPrioridad.get("Nivel-"+prioridadSeleccionada);
                            System.out.println("Estadística por prioridad ambiental - " + prioridadSeleccionada + ":");
                            int countPrioridad = 1;
                            iterator = listaPorPrioridad.iterator();
                            while(iterator.hasNext()) {
                                Residuo r = iterator.next();
                                System.out.println(countPrioridad + ". " + r);
                                countPrioridad++;
                            }
                            break;
                        default:
                            System.out.println("Opción inválida");
                    }

                    break;
                case 0:
                    System.out.println("Saliendo...");
                    break;

                default:
                    System.out.println("Opción inválida");
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
}
