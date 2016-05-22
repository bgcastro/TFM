package es.uvigo.esei.bgcastro.tfm.entities;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;

import es.uvigo.esei.bgcastro.tfm.DAO.VehiculosSQLite;
import es.uvigo.esei.bgcastro.tfm.content_provider.MantenimientosContentProvider;

/**
 * Created by braisgallegocastro on 7/5/16.
 */
public class Reparacion implements Parcelable {
    private int id;
    private String nombreReparacion;
    private String descripcion;
    private String referencia;
    private String taller;
    private float precio;

    private Mantenimiento mantenimiento;

    public Reparacion() {
        this.id = -1;
        this.nombreReparacion = "";
        this.descripcion = "";
        this.referencia = "";
        this.taller = "";
        this.precio = 0;
        this.mantenimiento = null;
    }

    public Reparacion(String nombreReparacion, String descripcion, String referencia, String taller, float precio, Mantenimiento mantenimiento) {
        this.nombreReparacion = nombreReparacion;
        this.descripcion = descripcion;
        this.referencia = referencia;
        this.precio = precio;
        this.taller = taller;
        this.mantenimiento = mantenimiento;
    }

    public Reparacion(Parcel source){
        this.id = source.readInt();
        this.nombreReparacion = source.readString();
        this.descripcion = source.readString();
        this.referencia = source.readString();
        this.taller = source.readString();
        this.precio = source.readFloat();
        this.mantenimiento = source.readParcelable(Mantenimiento.class.getClassLoader());
    }

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

    public String getTaller() {
        return taller;
    }

    public void setTaller(String taller) {
        this.taller = taller;
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

    @Override
    public int describeContents() {
        return 0;
    }

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

    public static final Creator<Reparacion> CREATOR = new Creator<Reparacion>() {
        @Override
        public Reparacion createFromParcel(Parcel source) { return new Reparacion(source);
        }

        @Override
        public Reparacion[] newArray(int size) {
            return new Reparacion[0];
        }
    };

}
