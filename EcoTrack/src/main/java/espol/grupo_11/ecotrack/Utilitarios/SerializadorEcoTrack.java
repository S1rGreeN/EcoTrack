package espol.grupo_11.ecotrack.Utilitarios;

import espol.grupo_11.ecotrack.Modelo.Residuo;
import espol.grupo_11.ecotrack.Modelo.Zona;
import espol.grupo_11.ecotrack.Modelo.Residuo.TipoResiduo;

import java.io.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class SerializadorEcoTrack {

    private static final String BASE = "EcoTrackData";
    private static final String INFO_ZONAS = BASE + "/Informacion de Zonas";
    private static final String EST = BASE + "/Estadisticas";

    static {
        crearEstructura();
    }

    private static void crearEstructura() {
        new File(INFO_ZONAS).mkdirs();
        new File(EST + "/Peso").mkdirs();
        new File(EST + "/Tipo").mkdirs();
        new File(EST + "/Zona").mkdirs();
        new File(EST + "/Nivel de prioridad").mkdirs();
    }


    public static void guardarZona(Zona zona) {
        String archivo = INFO_ZONAS + "/" + zona.getNombre() + ".ser";
        serializar(archivo, zona.getListaResiduos());
    }

    public static void guardarPorPeso(PriorityQueue<Residuo> colaPesos) {
        serializar(EST + "/Peso/Peso.ser", colaPesos);
    }

    public static void guardarPorTipo(
            TreeMap<TipoResiduo, ? extends List<Residuo>> mapa) {

        for (TipoResiduo tipo : mapa.keySet()) {
            String archivo = EST + "/Tipo/" + tipo.name() + ".ser";
            serializar(archivo, mapa.get(tipo));
        }
    }

    public static void guardarPorZona(
            TreeMap<String, ? extends List<Residuo>> mapa) {

        for (String codigoZona : mapa.keySet()) {
            String archivo = EST + "/Zona/" + codigoZona + ".ser";
            serializar(archivo, mapa.get(codigoZona));
        }
    }

    public static void guardarPorPrioridad(
            TreeMap<String, ? extends List<Residuo>> mapa) {

        for (String prioridad : mapa.keySet()) {
            String archivo = EST + "/Nivel de prioridad/" + prioridad + ".ser";
            serializar(archivo, mapa.get(prioridad));
        }
    }


    private static void serializar(String ruta, Object obj) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(ruta))) {
            oos.writeObject(obj);
        } catch (IOException e) {
            System.err.println("Error serializando: " + ruta);
        }
    }

    public static Zona cargarZona(String nombreZona) {
        File archivo = new File(
            INFO_ZONAS + File.separator + nombreZona + ".ser"
        );

        if (!archivo.exists()) return null;

        try (ObjectInputStream ois =
                new ObjectInputStream(new FileInputStream(archivo))) {

            return (Zona) ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Zona> cargarTodasLasZonas() {
        List<Zona> zonas = new ArrayList<>();
        File carpeta = new File(INFO_ZONAS);

        if (!carpeta.exists()) return zonas;

        File[] archivos = carpeta.listFiles((dir, name) -> name.endsWith(".ser"));
        if (archivos == null) return zonas;

        for (File f : archivos) {
            try (ObjectInputStream ois =
                    new ObjectInputStream(new FileInputStream(f))) {

                Zona z = (Zona) ois.readObject();
                zonas.addLast(z);

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return zonas;
    }

    public static PriorityQueue<Residuo> cargarPorPeso() {
        Comparator<Residuo> compPeso = new Comparator<Residuo>() {
            @Override
            public int compare(Residuo r1, Residuo r2) {
                return Double.compare(r2.getPeso(), r1.getPeso());
            }
        };
        File archivo = new File(INFO_ZONAS + "/peso/residuos.ser");
        if (!archivo.exists()) return new PriorityQueue<>(compPeso);

        try (ObjectInputStream ois =
                new ObjectInputStream(new FileInputStream(archivo))) {

            return (PriorityQueue<Residuo>) ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new PriorityQueue<>(compPeso);
        }
    }

    public static TreeMap<String, List<Residuo>> cargarPorTipo() {
        TreeMap<String, List<Residuo>> mapa = new TreeMap<>();
        File carpeta = new File(EST + "/tipo");

        if (!carpeta.exists()) return mapa;

        File[] archivos = carpeta.listFiles((d, n) -> n.endsWith(".ser"));
        if (archivos == null) return mapa;

        for (File f : archivos) {
            String tipo = f.getName().replace(".ser", "");
            try (ObjectInputStream ois =
                    new ObjectInputStream(new FileInputStream(f))) {

                List<Residuo> lista = (List<Residuo>) ois.readObject();
                mapa.put(tipo, lista);

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return mapa;
    }



}
