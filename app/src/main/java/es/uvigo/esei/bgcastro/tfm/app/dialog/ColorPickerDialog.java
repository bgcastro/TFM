package es.uvigo.esei.bgcastro.tfm.app.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.larswerkman.lobsterpicker.LobsterPicker;

import es.uvigo.esei.bgcastro.tfm.R;

/**
 * Created by braisgallegocastro on 6/2/16.
 * clase que representa un dialog con selector de color
 */
public class ColorPickerDialog extends DialogFragment {
    private static final String TAG = "ColorPickerDialog";
    private static final String COLOR_ACTUAL = "color_actual";
    private LobsterPicker lobsterPicker;
    private int colorActual;

    NoticeDialogListener noticeDialogListener;

    public static ColorPickerDialog newInstace(int color){
        ColorPickerDialog dialog = new ColorPickerDialog();

        Bundle bundle = new Bundle();
        bundle.putInt(COLOR_ACTUAL,color);

        dialog.setArguments(bundle);

        return dialog;
    }


    public int getSelectedColor(){
        colorActual = lobsterPicker.getColor();

        return colorActual;
    }

    public interface NoticeDialogListener{
        /**
         * Metodo para recibir los callbacks del boton postivo
         * @param dialog
         */
        void setPositiveButton(ColorPickerDialog dialog);

        /**
         * Metodo para recibir los callbacks del boton negativo
         * @param dialog
         */
        void setNegativeButton(ColorPickerDialog dialog);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            colorActual = bundle.getInt(COLOR_ACTUAL);
        }
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

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        View view = layoutInflater.inflate(R.layout.color_picker, null);

        builder.setView(view);

        builder.setMessage(R.string.titulo_color_picker);

        builder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "onClick: positiveButton");
                noticeDialogListener.setPositiveButton(ColorPickerDialog.this);
            }
        });

        builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "onClick: negativeButton");
                noticeDialogListener.setNegativeButton(ColorPickerDialog.this);
            }
        });

        lobsterPicker = (LobsterPicker) view.findViewById(R.id.lobsterpicker);
        lobsterPicker.setHistory(colorActual);
        lobsterPicker.setColor(colorActual);

        return builder.create();
    }
}
