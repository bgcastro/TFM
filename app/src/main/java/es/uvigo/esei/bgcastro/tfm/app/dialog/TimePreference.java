package es.uvigo.esei.bgcastro.tfm.app.dialog;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

/**
 * Created by braisgallegocastro on 17/5/16.
 * Clase para mostrar un dialogo para seleccionar fechas
 */
public class TimePreference extends DialogPreference {
    private int lastHour=0;
    private int lastMinute=0;
    private TimePicker picker=null;

    /**
     * Constructor
     *
     * @param ctxt  Contexto
     * @param attrs No usado
     */
    public TimePreference(Context ctxt, AttributeSet attrs) {
        super(ctxt, attrs);

        setPositiveButtonText("Set");
        setNegativeButtonText("Cancel");
    }

    /**
     * Metoodo que devuelve la hora
     *
     * @param time Hora
     * @return Entero con la hora
     */
    public static int getHour(String time) {
        String[] pieces=time.split(":");

        return(Integer.parseInt(pieces[0]));
    }

    /**
     * Metodo que devuelve el minuto
     * @param time Minuto
     * @return Entero con el minuto
     */
    public static int getMinute(String time) {
        String[] pieces=time.split(":");

        return(Integer.parseInt(pieces[1]));
    }

    /**
     * Metodo llamado cuando se crea el dialogo
     * @return Dialogo
     */
    @Override
    protected View onCreateDialogView() {
        picker=new TimePicker(getContext());

        return(picker);
    }

    /**
     * Metodo llamado cuando se vincula el dialogo a los datos
     * @param v No usado
     */
    @Override
    protected void onBindDialogView(View v) {
        super.onBindDialogView(v);

        picker.setCurrentHour(lastHour);
        picker.setCurrentMinute(lastMinute);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            lastHour=picker.getCurrentHour();
            lastMinute=picker.getCurrentMinute();

            String time=String.valueOf(lastHour)+":"+String.valueOf(lastMinute);

            if (callChangeListener(time)) {
                persistString(time);
            }
        }
    }

    /**
     * Metodo llamado cuando se recuperan los valores por defecto
     * @param a Array de tipos
     * @param index Indice
     * @return Valor por defecto
     */
    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return(a.getString(index));
    }

    /**
     * Metodo para cambiar el valor por defecto
     * @param restoreValue
     * @param defaultValue
     */
    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        String time=null;

        if (restoreValue) {
            if (defaultValue==null) {
                time=getPersistedString("00:00");
            }
            else {
                time=getPersistedString(defaultValue.toString());
            }
        }
        else {
            time=defaultValue.toString();
        }

        lastHour=getHour(time);
        lastMinute=getMinute(time);
    }
}