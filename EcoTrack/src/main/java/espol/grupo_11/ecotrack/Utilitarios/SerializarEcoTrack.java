package espol.grupo_11.ecotrack.Utilitarios;

import espol.grupo_11.ecotrack.Modelo.Zona;
import espol.grupo_11.ecotrack.Modelo.Residuo;
import espol.grupo_11.ecotrack.Modelo.Residuo.TipoResiduo;
import espol.grupo_11.ecotrack.Modelo.CentroRecoleccion;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;

import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;

import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.Color;
import java.awt.Paint;

import org.jfree.data.general.DefaultPieDataset;
public class SerializarEcoTrack {
    private static final long serialVersionUID = 1L;
    private static final String BASE = "EcotrackData";
    private static final String DIR_ZONAS = BASE + File.separator + "Informacion Zonas";
    private static final String DIR_ESTADISTICAS = BASE + File.separator + "estadisticas";
    private static final String DIR_GRAFICAS = BASE + File.separator +  "graficas";

    /* =======================
       SERIALIZACIÓN ZONAS
    ======================= */

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

    /* =======================
       SERIALIZACIÓN ESTADÍSTICAS
    ======================= */

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

    /* ---------- ZONA ---------- */

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
    public static void guardarEstadisticaPrioridad( TreeMap<String, LinkedList<Residuo>> mapa) { 
        if (mapa == null) return; 
        String dir = DIR_ESTADISTICAS + File.separator + "Nivel de prioridad"; 
        crearDirectorio(dir); 
        for (String p : mapa.keySet()) { 
            String ruta = dir + File.separator + p + ".ser"; 
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ruta))) { 
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
        if (archivos == null) return mapa; int nivel = 1; for (File f : archivos) { 
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) { 
                String prioridad = f.getName().replace(".ser", ""); 
                LinkedList<Residuo> lista = (LinkedList<Residuo>) ois.readObject(); 
                mapa.put(prioridad, lista); } catch (IOException | ClassNotFoundException e) { 
                    System.err.println("Error cargando prioridad"); 
                } 
            } 
            return mapa; 
        }

    /* =======================
       MÉTODOS DE GRÁFICAS
    ======================= */


    public static void generarGraficaTipoResiduo(TreeMap<TipoResiduo, LinkedList<Residuo>> mapa) {

        if (mapa == null || mapa.isEmpty()) return;

        crearDirectorio(DIR_GRAFICAS);

        // Dataset
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        for (TipoResiduo tipo : mapa.keySet()) {
            dataset.setValue(tipo.getNombre(), mapa.get(tipo).size());
        }

        // Crear gráfico
        JFreeChart chart = ChartFactory.createPieChart(
                "Distribución por Tipo de Residuo",
                dataset,
                true,
                true,
                false
        );

        // Configurar para mostrar porcentaje en cada sección
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
                "{0}: {2}", new DecimalFormat("#"), new DecimalFormat("0.00%")
        ));

        guardarGrafica(chart, "pie_tipo_residuo.png");
    }

    public static void generarGraficaZona(TreeMap<String, LinkedList<Residuo>> mapaZonas) {
        try {
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            int contador = 1;
            for (String zona : mapaZonas.keySet()) {
                dataset.addValue(
                        mapaZonas.get(zona).size(),
                        zona,              
                        "Zona " + contador 
                );
                contador++;
            }

            JFreeChart chart = ChartFactory.createBarChart(
                    "Residuos Recolectados por Zona",
                    "Zonas",
                    "Cantidad de Residuos",
                    dataset,
                    PlotOrientation.VERTICAL,
                    true,   // ← leyenda ACTIVADA
                    true,
                    false
            );

            CategoryPlot plot = chart.getCategoryPlot();

            // 🔹 Render para colores personalizados
            BarRenderer renderer = new BarRenderer();

            Paint[] colores = new Paint[] {
                    Color.BLUE,
                    Color.GREEN,
                    Color.ORANGE,
                    Color.MAGENTA,
                    Color.CYAN,
                    Color.PINK,
                    Color.YELLOW,
                    Color.RED,
                    Color.LIGHT_GRAY,
                    Color.DARK_GRAY,
                    new Color(128, 0, 128), 
                    new Color(0, 128, 128), 
                    new Color(255, 165, 0), 
                    new Color(0, 0, 128),  
                    new Color(128, 128, 0), 
                    new Color(255, 20, 147),
                    new Color(34, 139, 34)  
            };

            for (int i = 0; i < mapaZonas.size(); i++) {
                renderer.setSeriesPaint(i, colores[i % colores.length]);
            }

            plot.setRenderer(renderer);

            // 🔹 Mejora de legibilidad
            plot.setBackgroundPaint(Color.WHITE);
            plot.setRangeGridlinePaint(Color.GRAY);

            // 🔹 Guardar imagen
            guardarGrafica(chart, "barras_zonas.png");

        } catch (Exception e) {
            System.err.println("Error al generar gráfica por zona: " + e.getMessage());
        }
    }


    public static void generarGraficaPeso(PriorityQueue<Residuo> cola) {
        if (cola == null || cola.isEmpty()) return;

        crearDirectorio(DIR_GRAFICAS);

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        int contador = 1;

        for (Residuo r : cola) {
            dataset.addValue(
                    r.getPeso(),
                    "Peso (kg)",
                    "R" + contador
            );
            contador++;
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Residuos Recolectados por Peso",
                "Residuo",
                "Peso (kg)",
                dataset
        );

        guardarGrafica(chart, "estadistica_peso.png");
    }

    
    public static void generarGraficaPrioridad(TreeMap<String, LinkedList<Residuo>> mapa) {

        if (mapa == null || mapa.isEmpty()) return;

        crearDirectorio(DIR_GRAFICAS);

        // Dataset
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        for (String prioridad : mapa.keySet()) {
            dataset.setValue(prioridad, mapa.get(prioridad).size());
        }

        // Crear gráfico
        JFreeChart chart = ChartFactory.createPieChart(
                "Distribución por Prioridad Ambiental",
                dataset,
                true,
                true,
                false
        );

        // Configurar para mostrar porcentaje en cada sección
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
                "{0}: {2}", new DecimalFormat("#"), new DecimalFormat("0.00%")
        ));

        guardarGrafica(chart, "estadistica_prioridad.png");
    }


    private static void guardarGrafica(JFreeChart chart, String nombreArchivo) {
        try {
            File archivo = new File(DIR_GRAFICAS + File.separator + nombreArchivo);
            ChartUtils.saveChartAsPNG(archivo, chart, 800, 600);
        } catch (IOException e) {
            System.err.println("Error guardando gráfica: " + nombreArchivo);
        }
    }

    /* =======================
       UTILIDAD
    ======================= */

    private static void crearDirectorio(String ruta) {
        File dir = new File(ruta);
        if (!dir.exists()) dir.mkdirs();
    }
}
