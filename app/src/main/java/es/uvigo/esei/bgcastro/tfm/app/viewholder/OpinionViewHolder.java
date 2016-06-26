package es.uvigo.esei.bgcastro.tfm.app.viewholder;

import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by braisgallegocastro on 23/5/16.
 * Clase para utilizar el patron viewholder
 */
public class OpinionViewHolder {
    //elementos de  UI
    private TextView nombreTaller;
    private RatingBar ratingBar;
    private TextView comentario;
    private TextView precioReparacion;

    /**
     * Constructor
     */
    public OpinionViewHolder() {
    }

    /**
     * Metodo que devuelve el TextView nombre del taller
     *
     * @return TextView
     */
    public TextView getNombreTaller() {
        return nombreTaller;
    }

    /**
     * Metodo para cambiar el TextView nombre del taller
     * @param nombreTaller TextView
     */
    public void setNombreTaller(TextView nombreTaller) {
        this.nombreTaller = nombreTaller;
    }

    /**
     * Metodo que devuelve la ratingBar
     * @return RatingBar
     */
    public RatingBar getRatingBar() {
        return ratingBar;
    }

    /**
     * Metodo para cambiar la RatingBar
     * @param ratingBar RatingBar
     */
    public void setRatingBar(RatingBar ratingBar) {
        this.ratingBar = ratingBar;
    }

    /**
     * Metodo que devuelve el TextView del comentario
     * @return TextView
     */
    public TextView getComentario() {
        return comentario;
    }

    /**
     * Metodo para cambiar el TextView del comentario
     * @param comentario TextView
     */
    public void setComentario(TextView comentario) {
        this.comentario = comentario;
    }

    /**
     * Metodo que devuelve el TextView del precio
     * @return TextView
     */
    public TextView getPrecioReparacion() {
        return precioReparacion;
    }

    /**
     * Metodo para cambiar el TextView del precio
     * @param precioReparacion TextView
     */
    public void setPrecioReparacion(TextView precioReparacion) {
        this.precioReparacion = precioReparacion;
    }
}
