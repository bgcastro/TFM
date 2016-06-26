package es.uvigo.esei.bgcastro.tfm.app.entities;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;

import es.uvigo.esei.bgcastro.tfm.app.DAO.VehiculosSQLite;
import es.uvigo.esei.bgcastro.tfm.app.content_provider.MantenimientosContentProvider;

/**
 * Created by braisgallegocastro on 7/5/16.
 * Clase que representa a una repacion
 */
public class Reparacion implements Parcelable {
    /**
     * Creator de Parcelable
     */
    public static final Creator<Reparacion> CREATOR = new Creator<Reparacion>() {
        @Override
        public Reparacion createFromParcel(Parcel source) {
            return new Reparacion(source);
        }

        @Override
        public Reparacion[] newArray(int size) {
            return new Reparacion[0];
        }
    };
    private int id;
    //Nombre
    private String nombreReparacion;
    //Descripcion
    private String descripcion;
    //Referencia
    private String referencia;
    //Taller
    private String taller;
    //Precio
    private float precio;
    //Mantenimiento asociado
    private Mantenimiento mantenimiento;

    /**
     * Constructor
     */
    public Reparacion() {
        this.id = -1;
        this.nombreReparacion = "";
        this.descripcion = "";
        this.referencia = "";
        this.taller = "";
        this.precio = 0;
        this.mantenimiento = null;
    }

    /**
     * Constructor
     *
     * @param nombreReparacion
     * @param descripcion
     * @param referencia
     * @param taller
     * @param precio
     * @param mantenimiento
     */
    public Reparacion(String nombreReparacion, String descripcion, String referencia, String taller, float precio, Mantenimiento mantenimiento) {
        this.nombreReparacion = nombreReparacion;
        this.descripcion = descripcion;
        this.referencia = referencia;
        this.precio = precio;
        this.taller = taller;
        this.mantenimiento = mantenimiento;
    }

    /**
     * Constructor a partir de un Parcel
     * @param source Parcel
     */
    public Reparacion(Parcel source){
        this.id = source.readInt();
        this.nombreReparacion = source.readString();
        this.descripcion = source.readString();
        this.referencia = source.readString();
        this.taller = source.readString();
        this.precio = source.readFloat();
        this.mantenimiento = source.readParcelable(Mantenimiento.class.getClassLoader());
    }

    /**
     * Constructo a partir de un cursor
     * @param cursor Cursor con datos
     * @param context Contexto
     */
    public Reparacion(Cursor cursor, Context context) {
        if (cursor.getCount() != 0)
        {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MM yyyy");

            id = cursor.getInt(cursor.getColumnIndex(VehiculosSQLite.COL_ID_REPARACION));
            nombreReparacion = cursor.getString(cursor.getColumnIndex(VehiculosSQLite.COL_NOMBRE_REPARACION));
            descripcion = cursor.getString(cursor.getColumnIndex(VehiculosSQLite.COL_DESCRIPCION_REPARACION));
            referencia = cursor.getString(cursor.getColumnIndex(VehiculosSQLite.COL_REFERENCIA));
            taller = cursor.getString(cursor.getColumnIndex(VehiculosSQLite.COL_TALLER));
            precio = cursor.getFloat(cursor.getColumnIndex(VehiculosSQLite.COL_PRECIO));

            int idMantenimiento = cursor.getInt(cursor.getColumnIndex(VehiculosSQLite.COL_ID_MANTENIMIENTO_REPARACION));
            String[] projection = null;
            String where = null;
            String[] whereArgs = null;
            String sortOrder = null;

            // Query URI
            Uri queryUri = MantenimientosContentProvider.CONTENT_URI;
            queryUri = Uri.withAppendedPath(queryUri,Integer.toString(idMantenimiento));

            Cursor c = context.getContentResolver().query( queryUri, projection, where, whereArgs, sortOrder);
            c.moveToFirst();

            mantenimiento = new Mantenimiento(c,context);

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
     * Metodo que devuelve el nombe
     * @return Nombre
     */
    public String getNombreReparacion() {
        return nombreReparacion;
    }

    /**
     * Metodo para cambiar el nombre
     * @param nombreReparacion Nombre
     */
    public void setNombreReparacion(String nombreReparacion) {
        this.nombreReparacion = nombreReparacion;
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
     * Metodo que devuelve la referencia
     * @return
     */
    public String getReferencia() {
        return referencia;
    }

    /**
     * Metodo para cambiar la referencia
     * @param referencia Referencia
     */
    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    /**
     * Metodo que devuelve el taller
     * @return Taller
     */
    public String getTaller() {
        return taller;
    }

    /**
     * Metodo para cambiar el taller
     * @param taller Taller
     */
    public void setTaller(String taller) {
        this.taller = taller;
    }

    /**
     * Metodo que devuelve el precio
     * @return Precio
     */
    public float getPrecio() {
        return precio;
    }

    /**
     * Metodo para cambiar el precio
     * @param precio Precio
     */
    public void setPrecio(float precio) {
        this.precio = precio;
    }

    /**
     * Metodo que devuelve el mantenimiento asociado
     * @return Mantenimiento
     */
    public Mantenimiento getMantenimiento() {
        return mantenimiento;
    }

    /**
     * Metodo para cambiar el mantenimiento asociado
     * @param mantenimiento Mantenimiento
     */
    public void setMantenimiento(Mantenimiento mantenimiento) {
        this.mantenimiento = mantenimiento;
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
        dest.writeString(nombreReparacion);
        dest.writeString(descripcion);
        dest.writeString(referencia);
        dest.writeString(taller);
        dest.writeFloat(precio);
        dest.writeParcelable(mantenimiento,flags);
    }

}
