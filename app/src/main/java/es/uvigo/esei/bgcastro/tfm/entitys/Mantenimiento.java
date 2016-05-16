package es.uvigo.esei.bgcastro.tfm.entitys;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import es.uvigo.esei.bgcastro.tfm.DAO.VehiculosSQLite;
import es.uvigo.esei.bgcastro.tfm.content_provider.VehiculoContentProvider;

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
        this.id = source.readInt();
        this.estado = source.readString();
        this.nombre = source.readString();
        this.descripcion = source.readString();
        this.kilometrajeReparacion = source.readFloat();
        this.estadoSincronizacion = source.readString();
        this.fecha = (Date) source.readSerializable();
        this.vehiculo = source.readParcelable(Vehiculo.class.getClassLoader());
    }

    public Mantenimiento(Cursor cursor, Context context) {
        if (cursor.getCount() != 0)
        {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MM yyyy");

            id = cursor.getInt(cursor.getColumnIndex(VehiculosSQLite.COL_ID_MANTENIMIENTO));
            estado = cursor.getString(cursor.getColumnIndex(VehiculosSQLite.COL_ESTADO_REPARACION));
            nombre = cursor.getString(cursor.getColumnIndex(VehiculosSQLite.COL_NOMBRE));
            descripcion = cursor.getString(cursor.getColumnIndex(VehiculosSQLite.COL_DESCRIPCION));
            kilometrajeReparacion = cursor.getFloat(cursor.getColumnIndex(VehiculosSQLite.COL_KILOMETRAJE_REPARACION));
            try {
                fecha = simpleDateFormat.parse(cursor.getString(cursor.getColumnIndex(VehiculosSQLite.COL_FECHA)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            estadoSincronizacion = cursor.getString(cursor.getColumnIndex(VehiculosSQLite.COL_ESTADO_SINCRONIZACION));

            int idVehiculo = cursor.getInt(cursor.getColumnIndex(VehiculosSQLite.COL_ID_VEHICULO));
            String[] projection = null;
            String where = null;
            //String where = VehiculoContentProvider.ID + "=" + "?";
            //String[] whereArgs = {Integer.toString(idVehiculo)};
            String[] whereArgs = null;
            String sortOrder = null;

            // Query URI
            Uri queryUri = VehiculoContentProvider.CONTENT_URI;
            queryUri = Uri.withAppendedPath(queryUri,Integer.toString(idVehiculo));

            Cursor c = context.getContentResolver().query( queryUri, projection, where, whereArgs, sortOrder);
            c.moveToFirst();

            vehiculo = new Vehiculo(c);

            c.close();
        }
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
        dest.writeInt(id);
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
