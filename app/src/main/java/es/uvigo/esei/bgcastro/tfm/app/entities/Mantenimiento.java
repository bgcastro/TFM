package es.uvigo.esei.bgcastro.tfm.app.entities;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import es.uvigo.esei.bgcastro.tfm.app.DAO.VehiculosSQLite;
import es.uvigo.esei.bgcastro.tfm.app.content_provider.VehiculoContentProvider;

/**
 * Created by braisgallegocastro on 20/2/16.
 * Entidad que representa un mantenimiento
 */
public class Mantenimiento implements Parcelable{
    /**
     * Creator de Parcelable
     */
    public static final Parcelable.Creator<Mantenimiento> CREATOR = new Parcelable.Creator<Mantenimiento>() {
        @Override
        public Mantenimiento createFromParcel(Parcel source) {
            return new Mantenimiento(source);
        }

        @Override
        public Mantenimiento[] newArray(int size) {
            return new Mantenimiento[0];
        }
    };
    private int id = -1;
    //Estado del mantenimiento
    private String estado;
    //Nombre del mantenimiento
    private String nombre;
    //Descripcion del mantenimiento
    private String descripcion;
    //Kilometraje del manteniminiento
    private float kilometrajeReparacion;
    //Fecha del mantenimiento
    private Date fecha;
    //Vehiculo asociado
    private Vehiculo vehiculo;

    /**
     * Constructor
     */
    public Mantenimiento() {
    }

    /**
     * Constructor
     *
     * @param estado
     * @param nombre
     * @param descripcion
     * @param kilometrajeReparacion
     * @param fecha
     * @param vehiculo
     */
    public Mantenimiento(String estado, String nombre, String descripcion, float kilometrajeReparacion, Date fecha, Vehiculo vehiculo) {
        this.estado = estado;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.kilometrajeReparacion = kilometrajeReparacion;
        this.fecha = fecha;
        this.vehiculo = vehiculo;
    }

    /**
     * Constructor
     * @param id
     * @param estado
     * @param nombre
     * @param descripcion
     * @param kilometrajeReparacion
     * @param fecha
     * @param estadoSincronizacion
     * @param vehiculo
     */
    public Mantenimiento(int id, String estado, String nombre, String descripcion, float kilometrajeReparacion, Date fecha, String estadoSincronizacion, Vehiculo vehiculo) {
        this.id = id;
        this.estado = estado;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.kilometrajeReparacion = kilometrajeReparacion;
        this.fecha = fecha;
        this.vehiculo = vehiculo;
    }

    /**
     * Constructor a partir de un Parcel
     * @param source Parcel
     */
    public Mantenimiento(Parcel source) {
        this.id = source.readInt();
        this.estado = source.readString();
        this.nombre = source.readString();
        this.descripcion = source.readString();
        this.kilometrajeReparacion = source.readFloat();
        this.fecha = (Date) source.readSerializable();
        this.vehiculo = source.readParcelable(Vehiculo.class.getClassLoader());
    }

    /**
     * Constructor a partir de un cursor
     * @param cursor Cursor
     * @param context Contexto
     */
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

            int idVehiculo = cursor.getInt(cursor.getColumnIndex(VehiculosSQLite.COL_ID_VEHICULO));
            String[] projection = null;
            String where = null;
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
     * Metodo que devuelve el nombre
     * @return Nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Metodo para cambiar el nombre
     * @param nombre Nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Metodo que devuelve la descripcion
     * @return Descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Metodo para cambiar la descripcion
     * @param descripcion Descripcion
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Metodo que devuelve el kilometraje
     * @return Kilometraje
     */
    public float getKilometrajeReparacion() {
        return kilometrajeReparacion;
    }

    /**
     * Metodo para cambiar el kilometraje
     * @param kilometrajeReparacion Kilometraje
     */
    public void setKilometrajeReparacion(float kilometrajeReparacion) {
        this.kilometrajeReparacion = kilometrajeReparacion;
    }

    /**
     * Metodo que devuelve el estado
     * @return Estado
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Metodo para cambiar el estado
     * @param estado Estado
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Metodo que devuelve la fecha
     * @return Fecha
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * Metodo para cambiar la fecha
     * @param fecha Fecha
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * Metodo que devuelve el vehiculo al que esta asociado el mantenimiento
     * @return Vehiculo
     */
    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    /**
     * Metodo para cambiar el vehiculos
     * @param vehiculo Vehiculo
     */
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
                ", vehiculo=" + vehiculo +
                '}';
    }

    /**
     * Metodo para describir los contenidos
     * @return No usado
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Metodo que construye un parcel
     * @param dest Parcel
     * @param flags No usado
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(estado);
        dest.writeString(nombre);
        dest.writeString(descripcion);
        dest.writeFloat(kilometrajeReparacion);
        dest.writeSerializable(fecha);
        dest.writeParcelable(vehiculo,flags);
    }
}
