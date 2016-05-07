package es.uvigo.esei.bgcastro.tfm.entitys;

/**
 * Created by braisgallegocastro on 7/5/16.
 */
public class Reparacion {
    private int id;
    private String nombreReparacion;
    private String descripcion;
    private String referencia;
    private float precio;

    private Mantenimiento mantenimiento;

    public Reparacion() {
    }

    public Reparacion(String nombreReparacion, String descripcion, String referencia, float precio, Mantenimiento mantenimiento) {
        this.nombreReparacion = nombreReparacion;
        this.descripcion = descripcion;
        this.referencia = referencia;
        this.precio = precio;
        this.mantenimiento = mantenimiento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreReparacion() {
        return nombreReparacion;
    }

    public void setNombreReparacion(String nombreReparacion) {
        this.nombreReparacion = nombreReparacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public Mantenimiento getMantenimiento() {
        return mantenimiento;
    }

    public void setMantenimiento(Mantenimiento mantenimiento) {
        this.mantenimiento = mantenimiento;
    }
}
