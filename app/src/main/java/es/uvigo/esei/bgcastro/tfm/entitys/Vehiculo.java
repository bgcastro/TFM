package es.uvigo.esei.bgcastro.tfm.entitys;

import java.util.Date;

/**
 * Created by braisgallegocastro on 23/12/15.
 * Clase que representa los vehiculos
 */
public class Vehiculo {
    private byte[] imagenVehiculo;
    private String marca;
    private String modelo;
    private String matricula;
    private float kilometraje;
    private String combustible;
    private int cilindrada;
    private float potencia;
    private String color;
    private Date anho;
    private String estado;

    /**
     *  Constructor de la clase vehiculo
     * @param marca nombre de la marca
     * @param modelo nombre del modelo
     * @param matricula numero de matricula
     * @param kilometraje kilometraje del vehiculo
     * @param combustible combustible
     * @param cilindrada cilindrada del motor
     * @param potencia potencia del motor
     * @param color color del vehiculo
     * @param anho año de matriculacion
     */
    public Vehiculo(byte[] imagenVehiculo, String marca, String modelo, String matricula, float kilometraje, String combustible, int cilindrada, float potencia, String color, Date anho, String estado) {
        this.imagenVehiculo = imagenVehiculo;
        this.marca = marca;
        this.modelo = modelo;
        this.matricula = matricula;
        this.kilometraje = kilometraje;
        this.combustible = combustible;
        this.cilindrada = cilindrada;
        this.potencia = potencia;
        this.color = color;
        this.anho = anho;
        this.estado = estado;
    }

    public byte[] getImagenVehiculo() {
        return imagenVehiculo;
    }

    public void setImagenVehiculo(byte[] imagenVehiculo) {
        this.imagenVehiculo = imagenVehiculo;
    }

    public Date getAño() {
        return anho;
    }

    public void setAño(Date año) {
        this.anho = año;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public float getKilometraje() {
        return kilometraje;
    }

    public void setKilometraje(float kilometraje) {
        this.kilometraje = kilometraje;
    }

    public String getCombustible() {
        return combustible;
    }

    public void setCombustible(String combustible) {
        this.combustible = combustible;
    }

    public float getCilindrada() {
        return cilindrada;
    }

    public void setCilindrada(int cilindrada) {
        this.cilindrada = cilindrada;
    }

    public float getPotencia() {
        return potencia;
    }

    public void setPotencia(float potencia) {
        this.potencia = potencia;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Vehiculo{" +
                "imagenVehiculo=" + imagenVehiculo +
                ", marca='" + marca + '\'' +
                ", modelo='" + modelo + '\'' +
                ", matricula='" + matricula + '\'' +
                ", kilometraje=" + kilometraje +
                ", combustible='" + combustible + '\'' +
                ", cilindrada=" + cilindrada +
                ", potencia=" + potencia +
                ", color='" + color + '\'' +
                ", año=" + anho +
                ", estado='" + estado + '\'' +
                '}';
    }
}
