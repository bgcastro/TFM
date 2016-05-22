package es.uvigo.esei.bgcastro.tfm.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

import es.uvigo.esei.bgcastro.tfm.R;

/**
 * Created by braisgallegocastro on 22/5/16.
 */
public class OpinionDialog extends DialogFragment {
    public static final String fragmentTag = "OpinionDialog";
    private static final String TAG = "OpinionDialog";

    private EditText editTextComentario;
    private RatingBar ratingBarPuntuacion;

    private float puntuacion;
    private String comentario;

    NoticeDialogListener noticeDialogListener;

    public interface NoticeDialogListener{
        /**
         * Metodo para recibir los callbacks del boton postivo
         * @param dialog
         */
        void setPositiveButton(OpinionDialog dialog);

        /**
         * Metodo para recibir los callbacks del boton negativo
         * @param dialog
         */
        void setNegativeButton(OpinionDialog dialog);
    }

    public static OpinionDialog newInstace(){
        OpinionDialog dialog = new OpinionDialog();

        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.opinion_dialog, null);

        builder.setView(view);

        builder.setTitle(getString(R.string.titulo_dialog_opinion));

        builder.setPositiveButton(R.string.valorar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "onClick: positiveButton");
                noticeDialogListener.setPositiveButton(OpinionDialog.this);
            }
        });

        builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "onClick: negativeButton");
                noticeDialogListener.setNegativeButton(OpinionDialog.this);
            }
        });

        ratingBarPuntuacion = (RatingBar) view.findViewById(R.id.puntuacionReparacion);
        editTextComentario = (EditText) view.findViewById(R.id.comentario);

        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            noticeDialogListener = (NoticeDialogListener) activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString()+ "debe de implementar NoticeDialogListener");
        }
    }

    public float getPuntuacion() {
        puntuacion = ratingBarPuntuacion.getRating();

        return puntuacion;
    }

    public String getComentario() {
        comentario = editTextComentario.getText().toString();

        return comentario;
    }
}
