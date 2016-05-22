package es.uvigo.esei.bgcastro.tfm.entities;

import java.io.Serializable;

/**
 * Created by braisgallegocastro on 19/5/16.
 */
public class Opinion implements Serializable{
    private static final long serialVersionUID = 22291L;

    private int id;
    private float valoracion;
    private float precioReparacion;
    private String nombreTaller;
    private String opinion;

    public Opinion() {
    }

    public Opinion(float valoracion, float precioReparacion, String nombreTaller, String opinion) {
        this.valoracion = valoracion;
        this.precioReparacion = precioReparacion;
        this.nombreTaller = nombreTaller;
        this.opinion = opinion;
    }

    public Opinion(int id, float valoracion, float precioReparacion, String nombreTaller, String opinion) {
        this.id = id;
        this.valoracion = valoracion;
        this.precioReparacion = precioReparacion;
        this.nombreTaller = nombreTaller;
        this.opinion = opinion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getValoracion() {
        return valoracion;
    }

    public void setValoracion(float valoracion) {
        this.valoracion = valoracion;
    }

    public float getPrecioReparacion() {
        return precioReparacion;
    }

    public void setPrecioReparacion(float precioReparacion) {
        this.precioReparacion = precioReparacion;
    }

    public String getNombreTaller() {
        return nombreTaller;
    }

    public void setNombreTaller(String nombreTaller) {
        this.nombreTaller = nombreTaller;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    @Override
    public String toString() {
        return "Opinion{" +
                "id=" + id +
                ", valoracion=" + valoracion +
                ", precioReparacion=" + precioReparacion +
                ", nombreTaller='" + nombreTaller + '\'' +
                ", opinion='" + opinion + '\'' +
                '}';
    }
}
