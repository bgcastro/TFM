package es.uvigo.esei.bgcastro.tfm.viewholder;

import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by braisgallegocastro on 23/5/16.
 */
public class OpinionViewHolder {
    private TextView nombreTaller;
    private RatingBar ratingBar;
    private TextView comentario;
    private TextView precioReparacion;

    public OpinionViewHolder() {
    }

    public TextView getNombreTaller() {
        return nombreTaller;
    }

    public void setNombreTaller(TextView nombreTaller) {
        this.nombreTaller = nombreTaller;
    }

    public RatingBar getRatingBar() {
        return ratingBar;
    }

    public void setRatingBar(RatingBar ratingBar) {
        this.ratingBar = ratingBar;
    }

    public TextView getComentario() {
        return comentario;
    }

    public void setComentario(TextView comentario) {
        this.comentario = comentario;
    }

    public TextView getPrecioReparacion() {
        return precioReparacion;
    }

    public void setPrecioReparacion(TextView precioReparacion) {
        this.precioReparacion = precioReparacion;
    }
}
