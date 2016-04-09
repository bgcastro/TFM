package es.uvigo.esei.bgcastro.tfm.viewholder;

import android.widget.TextView;

/**
 * Created by braisgallegocastro on 20/2/16.
 */
public class MantenimientoViewHolder {
    TextView estadoMantenimientoItem;
    TextView nombreMantenimientoItem;
    TextView descripcionMantenimientoItem;
    TextView kilometrajeMantenimientoItem;
    TextView estadoSincronizacion;

    public TextView getEstadoMantenimientoItem() {
        return estadoMantenimientoItem;
    }

    public void setEstadoMantenimientoItem(TextView estadoMantenimientoItem) {
        this.estadoMantenimientoItem = estadoMantenimientoItem;
    }

    public TextView getNombreMantenimientoItem() {
        return nombreMantenimientoItem;
    }

    public void setNombreMantenimientoItem(TextView nombreMantenimientoItem) {
        this.nombreMantenimientoItem = nombreMantenimientoItem;
    }

    public TextView getDescripcionMantenimientoItem() {
        return descripcionMantenimientoItem;
    }

    public void setDescripcionMantenimientoItem(TextView descripcionMantenimientoItem) {
        this.descripcionMantenimientoItem = descripcionMantenimientoItem;
    }

    public TextView getKilometrajeMantenimientoItem() {
        return kilometrajeMantenimientoItem;
    }

    public void setKilometrajeMantenimientoItem(TextView kilometrajeMantenimientoItem) {
        this.kilometrajeMantenimientoItem = kilometrajeMantenimientoItem;
    }

    public TextView getEstadoSincronizacion() {
        return estadoSincronizacion;
    }

    public void setEstadoSincronizacion(TextView estadoSincronizacion) {
        this.estadoSincronizacion = estadoSincronizacion;
    }
}
