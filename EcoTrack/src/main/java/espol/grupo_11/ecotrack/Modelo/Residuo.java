package espol.grupo_11.ecotrack.Modelo;
import java.io.Serializable;
import java.time.LocalDateTime;
public class Residuo implements Serializable {
    public enum TipoResiduo {
        ORGANICO("Orgánico",1),
        PLASTICO("Plástico",2),
        VIDRIO("Vidrio",3),
        ELECTRONICO("Electrónico",4),
        METAL("Metal",5),
        PAPEL_CARTON("Papel y/o Cartón",6);
        private final String nombre;
        private final int valor;
        TipoResiduo(String nombre, int valor) {
            this.nombre = nombre;
            this.valor = valor;
        }
        public String getNombre() { return nombre; }
        public int getValor() { return valor; }
    }
    private String id;
    private String nombre;
    private TipoResiduo tipo; 
    private double peso;
    private LocalDateTime fechaRecoleccion;
    private String zona;
    private int prioridadAmbiental; 

    public Residuo(String id, String nombre, TipoResiduo tipo, double peso, String zona, int prioridad) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.peso = peso;
        this.fechaRecoleccion = LocalDateTime.now();
        this.zona = zona;
        this.prioridadAmbiental = prioridad;
    }

    // Getters 
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public TipoResiduo getTipo() { return tipo; }
    public double getPeso() { return peso; }
    public LocalDateTime getFechaRecoleccion() { return fechaRecoleccion;}
    public String getZona() { return zona;}
    public int getPrioridadAmbiental() { return prioridadAmbiental; }

    
    @Override
    public String toString() {
        return "Residuo{" + "nombre=" + nombre + ", tipo= "  + tipo.getNombre() +
         ", peso=" + peso + ", fechaRecoleccion=" + fechaRecoleccion +
         ", zona=" + zona + ", prioridad=" + prioridadAmbiental + '}';
    }
}
