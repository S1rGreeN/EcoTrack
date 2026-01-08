package espol.grupo_11.ecotrack.Modelo;
import java.io.Serializable;
import java.time.LocalDateTime;
public class Residuo implements Serializable {
    private String id;
    private String nombre;
    private String tipo; 
    private double peso;
    private LocalDateTime fechaRecoleccion;
    private String zona;
    private int prioridadAmbiental; 

    public Residuo(String id, String nombre, String tipo, double peso, String zona, int prioridad) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.peso = peso;
        this.fechaRecoleccion = LocalDateTime.now();
        this.zona = zona;
        this.prioridadAmbiental = prioridad;
    }

    // Getters 
    public String getTipo() { return tipo; }
    public double getPeso() { return peso; }
    public LocalDateTime getFechaRecoleccion() { return fechaRecoleccion;}
    public String getZona() { return zona;}
    public int getPrioridadAmbiental() { return prioridadAmbiental; }

    
    @Override
    public String toString() {
        return "Residuo{" + "nombre=" + nombre + ", tipo= "  + tipo +
         ", peso=" + peso + ", fechaRecoleccion=" + fechaRecoleccion +
         ", zona=" + zona + ", prioridad=" + prioridadAmbiental + '}';
    }
}
