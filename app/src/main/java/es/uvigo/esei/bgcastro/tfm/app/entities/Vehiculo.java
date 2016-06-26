package es.uvigo.esei.bgcastro.tfm.app.entities;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import es.uvigo.esei.bgcastro.tfm.app.DAO.VehiculosSQLite;

/**
 * Created by braisgallegocastro on 23/12/15.
 * Clase que representa los vehiculos
 */
public class Vehiculo implements Parcelable{
    /**
     * Creator de Parcelable
     */
    public static final Parcelable.Creator<Vehiculo> CREATOR = new Parcelable.Creator<Vehiculo>() {
        @Override
        public Vehiculo createFromParcel(Parcel source) {
            return new Vehiculo(source);
        }

        @Override
        public Vehiculo[] newArray(int size) {
            return new Vehiculo[size];
        }
    };
    private int id;
    //Atributos de un vehiculo
    private byte[] imagenVehiculo;
    private String marca;
    private String modelo;
    private String matricula;
    private float kilometraje;
    private String combustible;
    private int cilindrada;
    private float potencia;
    private int color;
    private int anho;
    private String estado;

    /**
     * constructor por defecto
     */
    public Vehiculo() {
        this.id = -1;
        this.imagenVehiculo = new byte[0];
        this.marca = "";
        this.modelo = "";
        this.matricula = "";
        this.kilometraje = 0;
        this.combustible = "";
        this.cilindrada = 0;
        this.potencia = 0;
        this.color = 0;
        this.anho = 0;
        this.estado = "";
    }

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
    public Vehiculo(byte[] imagenVehiculo, String marca, String modelo, String matricula, float kilometraje, String combustible, int cilindrada, float potencia, int color, int anho, String estado) {
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

    /**
     * Constructor para utilizar cuando se recupera de la base de datos
     * @param id
     * @param imagenVehiculo
     * @param marca
     * @param modelo
     * @param matricula
     * @param kilometraje
     * @param combustible
     * @param cilindrada
     * @param potencia
     * @param color
     * @param anho
     * @param estado
     */
    public Vehiculo(int id, byte[] imagenVehiculo, String marca, String modelo, String matricula, float kilometraje, String combustible, int cilindrada, float potencia, int color, int anho, String estado) {
        this.id = id;
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

    /**
     * Constructor a partir de un parcel para ser reconstruido en el paso entre actividades
     * @param sourceParcel
     */
    public Vehiculo(Parcel sourceParcel){
        this.id = sourceParcel.readInt();
        this.imagenVehiculo = sourceParcel.createByteArray();
        this.marca = sourceParcel.readString();
        this.modelo = sourceParcel.readString();
        this.matricula = sourceParcel.readString();
        this.kilometraje = sourceParcel.readFloat();
        this.combustible = sourceParcel.readString();
        this.cilindrada = sourceParcel.readInt();
        this.potencia = sourceParcel.readFloat();
        this.color = sourceParcel.readInt();
        this.anho = sourceParcel.readInt();
        this.estado = sourceParcel.readString();
    }

    /**
     * Constructor para construir un vehiculos a partir de un cursor
     * @param cursor
     */
    public Vehiculo(Cursor cursor){
        if (cursor.getCount() != 0)
        {
            id = cursor.getInt(cursor.getColumnIndex(VehiculosSQLite.COL_ID));
            imagenVehiculo = cursor.getBlob(cursor.getColumnIndex(VehiculosSQLite.COL_IMAGEN_VEHICULO));
            marca = cursor.getString(cursor.getColumnIndex(VehiculosSQLite.COL_MARCA));
            modelo = cursor.getString(cursor.getColumnIndex(VehiculosSQLite.COL_MODELO));
            matricula =cursor.getString(cursor.getColumnIndex(VehiculosSQLite.COL_MATRICULA));
            kilometraje = cursor.getFloat(cursor.getColumnIndex(VehiculosSQLite.COL_KILOMETRAJE));
            combustible = cursor.getString(cursor.getColumnIndex(VehiculosSQLite.COL_COMBUSTIBLE));
            cilindrada = cursor.getInt(cursor.getColumnIndex(VehiculosSQLite.COL_CILINDRADA));
            potencia = cursor.getFloat(cursor.getColumnIndex(VehiculosSQLite.COL_POTENCIA));
            color = cursor.getInt(cursor.getColumnIndex(VehiculosSQLite.COL_COLOR));
            anho = cursor.getInt(cursor.getColumnIndex(VehiculosSQLite.COL_ANHO));
            estado = cursor.getString(cursor.getColumnIndex(VehiculosSQLite.COL_ESTADO));
        }

    }

    /**
     * Metoto que devuelve la imagen
     *
     * @return Imagen
     */
    public byte[] getImagenVehiculo() {
        return imagenVehiculo;
    }

    /**
     * Metodo para cambiar la imagen
     * @param imagenVehiculo Imagen
     */
    public void setImagenVehiculo(byte[] imagenVehiculo) {
        this.imagenVehiculo = imagenVehiculo;
    }

    /**
     * Metodo que devuelve el año
     * @return Año
     */
    public int getAño() {
        return anho;
    }

    /**
     * Metodo para cambiar el año
     * @param año Año
     */
    public void setAño(int año) {
        this.anho = año;
    }

    /**
     * Metodo que devuelve la marca
     * @return Marca
     */
    public String getMarca() {
        return marca;
    }

    /**
     * Metodo para cambiar la marca
     * @param marca Marca
     */
    public void setMarca(String marca) {
        this.marca = marca;
    }

    /**
     * Metodo que devuelve el modelo
     * @return Modelo
     */
    public String getModelo() {
        return modelo;
    }

    /**
     * Metodo para cambiar el modelo
     * @param modelo Modelo
     */
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    /**
     * Metodo que devuelve la matricula
     * @return Matricula
     */
    public String getMatricula() {
        return matricula;
    }

    /**
     * Metodo para cambiar la matricula
     * @param matricula Matricula
     */
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    /**
     * Metodo que devuelve el kilometraje
     * @return
     */
    public float getKilometraje() {
        return kilometraje;
    }

    /**
     * Metodo para cambiar el kilometraje
     * @param kilometraje Kilometraje
     */
    public void setKilometraje(float kilometraje) {
        this.kilometraje = kilometraje;
    }

    /**
     * Metodo que devuelve el combustible
     * @return Combustible
     */
    public String getCombustible() {
        return combustible;
    }

    /**
     * Metodo para cambiar el combustible
     * @param combustible Combustible
     */
    public void setCombustible(String combustible) {
        this.combustible = combustible;
    }

    /**
     * Metodo que devuelve la cilindrada
     * @return Cilindrada
     */
    public int getCilindrada() {
        return cilindrada;
    }

    /**
     * Metodo para cambiar la cilindrada
     * @param cilindrada Cilindrada
     */
    public void setCilindrada(int cilindrada) {
        this.cilindrada = cilindrada;
    }

    /**
     * Metodo que devuelve la potencia
     * @return Potencia
     */
    public float getPotencia() {
        return potencia;
    }

    /**
     * Metodo para cambiar la potencia
     * @param potencia Potencia
     */
    public void setPotencia(float potencia) {
        this.potencia = potencia;
    }

    /**
     * Metodo que devuelve el color
     * @return Color
     */
    public int getColor() {
        return color;
    }

    /**
     * Metoto para cambiar el color
     * @param color Color
     */
    public void setColor(int color) {
        this.color = color;
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
     * Metodo que deuelve el ID
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

    @Override
    public String toString() {
        return "Vehiculo{" +
                "id=" + id +
                "imagenVehiculo=" + imagenVehiculo + '\'' +
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
        dest.writeInt(this.id);
        dest.writeByteArray(this.imagenVehiculo);
        dest.writeString(this.marca);
        dest.writeString(this.modelo);
        dest.writeString(this.matricula);
        dest.writeFloat(this.kilometraje);
        dest.writeString(this.combustible);
        dest.writeInt(this.cilindrada);
        dest.writeFloat(this.potencia);
        dest.writeInt(this.color);
        dest.writeInt(this.anho);
        dest.writeString(this.estado);
    }
}
