package espol.grupo_11.ecotrack.Utilitarios;

import espol.grupo_11.ecotrack.Modelo.Zona;
import espol.grupo_11.ecotrack.Modelo.Residuo;
import espol.grupo_11.ecotrack.Modelo.Residuo.TipoResiduo;
import espol.grupo_11.ecotrack.Modelo.CentroRecoleccion;

import java.io.*;

public class SerializarEcoTrack {

    private static final String DIR_ZONAS = "Informacion Zonas";
    private static final String DIR_ESTADISTICAS = "estadisticas";

    public static void guardarZona(Zona zona) {
        if (zona == null) return;

        crearDirectorio(DIR_ZONAS);

        String ruta = DIR_ZONAS + File.separator + zona.getNombre() + ".ser";

        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(ruta))) {
            oos.writeObject(zona);
        } catch (IOException e) {
            System.err.println("Error al guardar zona: " + zona.getNombre());
        }
    }

    public static Zona cargarZona(String nombreZona) {
        if (nombreZona == null) return null;

        String ruta = DIR_ZONAS + File.separator + nombreZona + ".ser";
        File archivo = new File(ruta);

        if (!archivo.exists()) return null;

        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(archivo))) {
            return (Zona) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar zona: " + nombreZona);
        }
        return null;
    }

    public static ArrayList<Zona> cargarTodasLasZonas() {
        crearDirectorio(DIR_ZONAS);
        ArrayList<Zona> zonas = new ArrayList<>();

        File dir = new File(DIR_ZONAS);
        File[] archivos = dir.listFiles((d, name) -> name.endsWith(".ser"));

        if (archivos == null) return zonas;

        for (File f : archivos) {
            try (ObjectInputStream ois =
                         new ObjectInputStream(new FileInputStream(f))) {
                Zona z = (Zona) ois.readObject();
                zonas.addLast(z);
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error leyendo archivo: " + f.getName());
            }
        }
        return zonas;
    }


    public static void guardarEstadisticas(CentroRecoleccion centro) {
        if (centro == null) return;

        guardarEstadisticaPeso(centro.getColaResiduosRecolectadosPesos());
        guardarEstadisticaTipo(centro.getMapaListaResiduosPorTipo());
        guardarEstadisticaZona(centro.getMapaListaResiduosPorZona());
        guardarEstadisticaPrioridad(centro.getMapaListaResiduosPorPrioridadAmbiental());
    }

    /* ---------- PESO ---------- */
    public static void guardarEstadisticaPeso(PriorityQueue<Residuo> cola) {
        if (cola == null) return;

        String dir = DIR_ESTADISTICAS + File.separator + "peso";
        crearDirectorio(dir);

        String ruta = dir + File.separator + "residuos.ser";

        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(ruta))) {
            oos.writeObject(cola);
        } catch (IOException e) {
            System.err.println("Error guardando estadística por peso");
        }
    }

    public static PriorityQueue<Residuo> cargarEstadisticaPeso() {
        String ruta = DIR_ESTADISTICAS + File.separator + "peso"
                + File.separator + "residuos.ser";
        File archivo = new File(ruta);

        if (!archivo.exists()) return null;

        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(archivo))) {
            return (PriorityQueue<Residuo>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error cargando estadística por peso");
        }
        return null;
    }

    /* ---------- TIPO ---------- */
    public static void guardarEstadisticaTipo(
            TreeMap<TipoResiduo, LinkedList<Residuo>> mapa) {

        if (mapa == null) return;

        String dir = DIR_ESTADISTICAS + File.separator + "tipo";
        crearDirectorio(dir);

        for (TipoResiduo tipo : mapa.keySet()) {
            String ruta = dir + File.separator + tipo.name() + ".ser";
            try (ObjectOutputStream oos =
                         new ObjectOutputStream(new FileOutputStream(ruta))) {
                oos.writeObject(mapa.get(tipo));
            } catch (IOException e) {
                System.err.println("Error guardando tipo: " + tipo.getNombre());
            }
        }
    }

    public static TreeMap<TipoResiduo, LinkedList<Residuo>> cargarEstadisticaTipo() {
        TreeMap<TipoResiduo, LinkedList<Residuo>> mapa = new TreeMap<>();

        String dir = DIR_ESTADISTICAS + File.separator + "tipo";
        crearDirectorio(dir);

        File carpeta = new File(dir);
        File[] archivos = carpeta.listFiles((d, n) -> n.endsWith(".ser"));

        if (archivos == null) return mapa;

        for (File f : archivos) {
            try (ObjectInputStream ois =
                         new ObjectInputStream(new FileInputStream(f))) {

                String nombreTipo = f.getName().replace(".ser", "");
                TipoResiduo tipo = TipoResiduo.valueOf(nombreTipo);
                LinkedList<Residuo> lista =
                        (LinkedList<Residuo>) ois.readObject();

                mapa.put(tipo, lista);

            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error cargando tipo: " + f.getName());
            }
        }
        return mapa;
    }

    /* ---------- ZONA (estadísticas) ---------- */
    public static void guardarEstadisticaZona(
            TreeMap<String, LinkedList<Residuo>> mapa) {

        if (mapa == null) return;

        String dir = DIR_ESTADISTICAS + File.separator + "zona";
        crearDirectorio(dir);

        for (String zona : mapa.keySet()) {
            String ruta = dir + File.separator + zona + ".ser";
            try (ObjectOutputStream oos =
                         new ObjectOutputStream(new FileOutputStream(ruta))) {
                oos.writeObject(mapa.get(zona));
            } catch (IOException e) {
                System.err.println("Error guardando zona estadística: " + zona);
            }
        }
    }

    public static TreeMap<String, LinkedList<Residuo>> cargarEstadisticaZona() {
        TreeMap<String, LinkedList<Residuo>> mapa = new TreeMap<>();

        String dir = DIR_ESTADISTICAS + File.separator + "zona";
        crearDirectorio(dir);

        File carpeta = new File(dir);
        File[] archivos = carpeta.listFiles((d, n) -> n.endsWith(".ser"));

        if (archivos == null) return mapa;

        for (File f : archivos) {
            try (ObjectInputStream ois =
                         new ObjectInputStream(new FileInputStream(f))) {

                String nombreZona = f.getName().replace(".ser", "");
                LinkedList<Residuo> lista =
                        (LinkedList<Residuo>) ois.readObject();
                mapa.put(nombreZona, lista);

            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error cargando zona estadística");
            }
        }
        return mapa;
    }

    /* ---------- PRIORIDAD ---------- */
    public static void guardarEstadisticaPrioridad(
            TreeMap<String, LinkedList<Residuo>> mapa) {

        if (mapa == null) return;

        String dir = DIR_ESTADISTICAS + File.separator + "Nivel de prioridad";
        crearDirectorio(dir);

        for (String p : mapa.keySet()) {
            String ruta = dir + File.separator + p + ".ser";
            try (ObjectOutputStream oos =
                         new ObjectOutputStream(new FileOutputStream(ruta))) {
                oos.writeObject(mapa.get(p));
            } catch (IOException e) {
                System.err.println("Error guardando prioridad: " + p);
            }
        }
    }

    public static TreeMap<String, LinkedList<Residuo>> cargarEstadisticaPrioridad() {
        TreeMap<String, LinkedList<Residuo>> mapa = new TreeMap<>();

        String dir = DIR_ESTADISTICAS + File.separator + "Nivel de prioridad";
        crearDirectorio(dir);

        File carpeta = new File(dir);
        File[] archivos = carpeta.listFiles((d, n) -> n.endsWith(".ser"));

        if (archivos == null) return mapa;
        int nivel = 1;
        for (File f : archivos) {
            try (ObjectInputStream ois =
                         new ObjectInputStream(new FileInputStream(f))) {

                String prioridad = f.getName().replace(".ser", "");
                LinkedList<Residuo> lista =
                        (LinkedList<Residuo>) ois.readObject();
                mapa.put(prioridad, lista);

            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error cargando prioridad");
            }
        }
        return mapa;
    }

    /* =======================
       UTILIDAD
    ======================= */
    private static void crearDirectorio(String ruta) {
        File dir = new File(ruta);
        if (!dir.exists()) dir.mkdirs();
    }
}
