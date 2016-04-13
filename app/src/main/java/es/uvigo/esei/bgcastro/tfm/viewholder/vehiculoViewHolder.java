package es.uvigo.esei.bgcastro.tfm.viewholder;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by braisgallegocastro on 24/12/15.
 */
public class VehiculoViewHolder {
    private ImageView imagenVehiculo;
    private TextView nombreVehiculo;
    private TextView matricula;
    private TextView kilometraje;
    private TextView estadoVehiculo;

    public ImageView getImagenVehiculo() {
        return imagenVehiculo;
    }

    public void setImagenVehiculo(ImageView imagenVehiculo) {
        this.imagenVehiculo = imagenVehiculo;
    }

    public TextView getNombreVehiculo() {
        return nombreVehiculo;
    }

    public void setNombreVehiculo(TextView nombreVehiculo) {
        this.nombreVehiculo = nombreVehiculo;
    }

    public TextView getMatricula() {
        return matricula;
    }

    public void setMatricula(TextView matricula) {
        this.matricula = matricula;
    }

    public TextView getKilometraje() {
        return kilometraje;
    }

    public void setKilometraje(TextView kilometraje) {
        this.kilometraje = kilometraje;
    }

    public TextView getEstadoVehiculo() {
        return estadoVehiculo;
    }

    public void setEstadoVehiculo(TextView estadoVehiculo) {
        this.estadoVehiculo = estadoVehiculo;
    }
}
