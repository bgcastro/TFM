package es.uvigo.esei.bgcastro.tfm.entitys;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by braisgallegocastro on 20/2/16.
 */
public class Mantenimiento implements Parcelable{
    private int id = -1;
    private String estado;
    private String nombre;
    private String descripcion;
    private float kilometrajeReparacion;
    private Date fecha;
    private String estadoSincronizacion;

    private Vehiculo vehiculo;

    public Mantenimiento() {
    }

    public Mantenimiento(String estado, String nombre, String descripcion, float kilometrajeReparacion, Date fecha, String estadoSincronizacion, Vehiculo vehiculo) {
        this.estado = estado;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.kilometrajeReparacion = kilometrajeReparacion;
        this.fecha = fecha;
        this.estadoSincronizacion = estadoSincronizacion;
        this.vehiculo = vehiculo;
    }

    public Mantenimiento(int id, String estado, String nombre, String descripcion, float kilometrajeReparacion, Date fecha, String estadoSincronizacion, Vehiculo vehiculo) {
        this.id = id;
        this.estado = estado;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.kilometrajeReparacion = kilometrajeReparacion;
        this.fecha = fecha;
        this.estadoSincronizacion = estadoSincronizacion;
        this.vehiculo = vehiculo;
    }

    public Mantenimiento(Parcel source) {
        this.estado = source.readString();
        this.nombre = source.readString();
        this.descripcion = source.readString();
        this.kilometrajeReparacion = source.readFloat();
        this.estadoSincronizacion = source.readString();
        this.fecha = (Date) source.readSerializable();
        this.vehiculo = source.readParcelable(Vehiculo.class.getClassLoader());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public float getKilometrajeReparacion() {
        return kilometrajeReparacion;
    }

    public void setKilometrajeReparacion(float kilometrajeReparacion) {
        this.kilometrajeReparacion = kilometrajeReparacion;
    }


    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEstadoSincronizacion() {
        return estadoSincronizacion;
    }

    public void setEstadoSincronizacion(String estadoSincronizacion) {
        this.estadoSincronizacion = estadoSincronizacion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    @Override
    public String toString() {
        return "Mantenimiento{" +
                "id=" + id +
                ", estado='" + estado + '\'' +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", kilometrajeReparacion=" + kilometrajeReparacion +
                ", fecha=" + fecha +
                ", estadoSincronizacion='" + estadoSincronizacion + '\'' +
                ", vehiculo=" + vehiculo +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(estado);
        dest.writeString(nombre);
        dest.writeString(descripcion);
        dest.writeFloat(kilometrajeReparacion);
        dest.writeString(estadoSincronizacion);
        dest.writeSerializable(fecha);
        dest.writeParcelable(vehiculo,flags);
    }

    public static final Parcelable.Creator<Mantenimiento> CREATOR = new Parcelable.Creator<Mantenimiento>() {
        @Override
        public Mantenimiento createFromParcel(Parcel source) { return new Mantenimiento(source);
        }

        @Override
        public Mantenimiento[] newArray(int size) {
            return new Mantenimiento[0];
        }
    };
}
