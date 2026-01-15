package espol.grupo_11.ecotrack;

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

public class EcoTrack {

    private static CircularDoubleLinkedList<Residuo> crearListaResiduos(
            String codigoZona, int cantidad, int baseId, Random random) {

        if (cantidad <= 0)
            throw new IllegalArgumentException("Cantidad debe ser positiva.");

        CircularDoubleLinkedList<Residuo> lista = new CircularDoubleLinkedList<>();
        TipoResiduo[] tipos = TipoResiduo.values();

        for (int i = 1; i <= cantidad; i++) {
            TipoResiduo tipo = tipos[random.nextInt(tipos.length)];
            double peso = Math.round((0.05 + (random.nextDouble() * 4.95)) * 100.0) / 100.0;
            int prioridadAmbiental = 1 + random.nextInt(5);
            String id = "R-" + codigoZona + "-" + (baseId + i);
            String nombre = "Residuo-" + i + "-" + tipo.getNombre();

            lista.addLast(new Residuo(
                    id, nombre, tipo, peso, codigoZona, prioridadAmbiental));
        }
        return lista;
    }

    public static void main(String[] args) {

        PriorityQueue<Zona> zonasUrbanas =
                new PriorityQueue<>(new Comparator<Zona>() {
                    @Override
                    public int compare(Zona z1, Zona z2) {
                        return (z1.getCantidadResiduosRecolectados()
                                - z1.getListaResiduos().size())
                                - (z2.getCantidadResiduosRecolectados()
                                - z2.getListaResiduos().size());
                    }
                });

        String[] nombres = {
                "Alborada", "Bastión Popular", "Cerro Colorado", "El Fortín",
                "El Guasmo", "La Alborada", "La Merced", "Los Esteros",
                "Los Samanes", "Miraflores", "Octava", "Samborondón",
                "San Eduardo", "San Francisco", "Tarqui", "Urdesa"
        };

        Random random = new Random(12345);
        int idx = 1;

        for (String nombreZona : nombres) {

            Zona zonaCargada = SerializarEcoTrack.cargarZona(nombreZona);

            if (zonaCargada != null) {
                zonasUrbanas.add(zonaCargada);
                continue;
            }

            String codigo = String.format("GYE-0%02d", idx);
            int cantidad = 15 + random.nextInt(16);

            CircularDoubleLinkedList<Residuo> lista =
                    crearListaResiduos(codigo, cantidad, idx * 1000, random);

            Zona z = new Zona(lista, nombreZona, codigo);

            z.setUltimaRecoleccion(
                    idx <= 8
                            ? LocalDateTime.of(2026, 1, 10, 14, 30)
                            : LocalDateTime.of(2026, 1, 9, 14, 30)
            );

            zonasUrbanas.add(z);
            SerializarEcoTrack.guardarZona(z);
            idx++;
        }

        Deque<Residuo> pilaResiduos = new ArrayDeque<>();
        CentroRecoleccion centro = new CentroRecoleccion(zonasUrbanas, pilaResiduos);
        ControladorCentroRecoleccion controlador =
                new ControladorCentroRecoleccion(centro);

        Thread hiloGenerador = new Thread(() -> {
            Random rnd = new Random();
            while (true) {
                for (int g = 0; g < 2; g++) {

                    LinkedList<Zona> listaZonas = new LinkedList<>();
                    synchronized (centro.getZonasUrbanas()) {
                        listaZonas.addAll(centro.getZonasUrbanas());
                    }

                    if (listaZonas.isEmpty()) break;

                    Zona zonaRandom =
                            listaZonas.get(rnd.nextInt(listaZonas.size()));

                    TipoResiduo tipoRand =
                            TipoResiduo.values()[rnd.nextInt(TipoResiduo.values().length)];

                    double pesoRand =
                            Math.round((0.05 + (rnd.nextDouble() * 4.95)) * 100.0) / 100.0;

                    int prioRand = 1 + rnd.nextInt(5);

                    String idRand =
                            "R-" + zonaRandom.getCodigo() + "-AUTO-"
                                    + Math.abs(java.util.UUID.randomUUID().toString().hashCode());

                    controlador.nuevoResiduo(
                            idRand,
                            "AutoResiduo-" + idRand,
                            tipoRand,
                            pesoRand,
                            zonaRandom.getCodigo(),
                            prioRand
                    );
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
        int opcion;

        do {
            System.out.println("\n===== Menú =====");
            System.out.println("1. Iniciar Carrito");
            System.out.println("2. Generar Residuo");
            System.out.println("3. Ver información de zonas");
            System.out.println("4. Ver estadísticas");
            System.out.println("5. Generar gráficas de estadísticas");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                opcion = -1;
            }

            switch (opcion) {

                case 1:
                    controlador.iniciarRecoleccionCarrito();
                    SerializarEcoTrack.guardarEstadisticas(centro);
                    break;

                case 3:
                    System.out.print("Seleccione zona (1-" + nombres.length + "): ");
                    int zidx = Integer.parseInt(scanner.nextLine());
                    Zona z = SerializarEcoTrack.cargarZona(nombres[zidx - 1]);
                    if (z != null) {
                        System.out.println(z);
                        for (Residuo r : z.getListaResiduos())
                            System.out.println(r);
                    }
                    break;

                case 5:
                    System.out.println("Generando gráficas...");
                    SerializarEcoTrack.generarGraficaPeso(
                            SerializarEcoTrack.cargarEstadisticaPeso());
                    SerializarEcoTrack.generarGraficaTipoResiduo(
                            SerializarEcoTrack.cargarEstadisticaTipo());
                    SerializarEcoTrack.generarGraficaZona(
                            SerializarEcoTrack.cargarEstadisticaZona());
                    SerializarEcoTrack.generarGraficaPrioridad(
                            SerializarEcoTrack.cargarEstadisticaPrioridad());
                    System.out.println("Gráficas listas.");
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
}
