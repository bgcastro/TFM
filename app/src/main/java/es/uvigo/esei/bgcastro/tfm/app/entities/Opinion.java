package es.uvigo.esei.bgcastro.tfm.app.entities;

import java.io.Serializable;

/**
 * Created by braisgallegocastro on 19/5/16.
 * Clase que representa una opinion
 */
public class Opinion implements Serializable{
    private static final long serialVersionUID = 22291L;

    private int id;
    //Valoracion
    private float valoracion;
    //Precio
    private float precioReparacion;
    //Nonmbre taller
    private String nombreTaller;
    //Opinion
    private String opinion;

    /**
     * Constructor
     */
    public Opinion() {
    }

    /**
     * Constructor
     *
     * @param valoracion
     * @param precioReparacion
     * @param nombreTaller
     * @param opinion
     */
    public Opinion(float valoracion, float precioReparacion, String nombreTaller, String opinion) {
        this.valoracion = valoracion;
        this.precioReparacion = precioReparacion;
        this.nombreTaller = nombreTaller;
        this.opinion = opinion;
    }

    /**
     * Constructor
     * @param id
     * @param valoracion
     * @param precioReparacion
     * @param nombreTaller
     * @param opinion
     */
    public Opinion(int id, float valoracion, float precioReparacion, String nombreTaller, String opinion) {
        this.id = id;
        this.valoracion = valoracion;
        this.precioReparacion = precioReparacion;
        this.nombreTaller = nombreTaller;
        this.opinion = opinion;
    }

    /**
     * Metodo que devuelve el ID
     * @return ID
     */
    public int getId() {
        return id;
    }

    /**
     * Metodo para cambiar el ID
     * @param id ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Metodo que devuelve la valoracion
     * @return Valoracion
     */
    public float getValoracion() {
        return valoracion;
    }

    /**
     * Metodo para cambiar la valoracion
     * @param valoracion
     */
    public void setValoracion(float valoracion) {
        this.valoracion = valoracion;
    }

    /**
     * Metodo que devuelve el precio
     * @return Precio
     */
    public float getPrecioReparacion() {
        return precioReparacion;
    }

    /**
     * Metodo para cambiar el precio de reparacion
     * @param precioReparacion Precio
     */
    public void setPrecioReparacion(float precioReparacion) {
        this.precioReparacion = precioReparacion;
    }

    /**
     * Metodo que devuelve el nombre del taller
     * @return Nombre
     */
    public String getNombreTaller() {
        return nombreTaller;
    }

    /**
     * Metodo para cambiar el nombre del taller
     * @param nombreTaller
     */
    public void setNombreTaller(String nombreTaller) {
        this.nombreTaller = nombreTaller;
    }

    /**
     * Metodo que devuelve la opinion
     * @return Opinion
     */
    public String getOpinion() {
        return opinion;
    }

    /**
     * Metodo para cambiar la opinion
     * @param opinion
     */
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
