package espol.grupo_11.ecotrack.Utilitarios;

import espol.grupo_11.ecotrack.Modelo.Residuo;
import espol.grupo_11.ecotrack.Modelo.Zona;
import espol.grupo_11.ecotrack.Modelo.Residuo.TipoResiduo;

import java.io.*;

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
}
