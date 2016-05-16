package es.uvigo.esei.bgcastro.tfm.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import es.uvigo.esei.bgcastro.tfm.R;
import es.uvigo.esei.bgcastro.tfm.entitys.Mantenimiento;
import es.uvigo.esei.bgcastro.tfm.viewholder.MantenimientoViewHolder;

/**
 * Created by braisgallegocastro on 20/2/16.
 */
public class MantenimientoAdapter extends ArrayAdapter<Mantenimiento> {
    private ArrayList<Mantenimiento> arrayListMantenimientos;
    private Context contexto;
    private int vistaItem;

    public MantenimientoAdapter(Context context, int resource, ArrayList<Mantenimiento> objects) {
        super(context, resource, objects);

        this.arrayListMantenimientos = objects;
        this.contexto = context;
        this.vistaItem = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        MantenimientoViewHolder mantenimientoViewHolder;

        if (view == null){
            LayoutInflater layoutInflater = (LayoutInflater)contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(vistaItem,parent,false);

            mantenimientoViewHolder = new MantenimientoViewHolder();
            mantenimientoViewHolder.setEstadoMantenimientoItem((TextView) view.findViewById(R.id.estadoMantenimientoItem));
            mantenimientoViewHolder.setNombreMantenimientoItem((TextView) view.findViewById(R.id.nombreMantenimientoItem));
            mantenimientoViewHolder.setDescripcionMantenimientoItem((TextView) view.findViewById(R.id.descripcionReparacionItem));
            mantenimientoViewHolder.setKilometrajeMantenimientoItem((TextView) view.findViewById(R.id.kilometrajeMantenimientoItem));
            mantenimientoViewHolder.setEstadoSincronizacion((TextView) view.findViewById(R.id.estadoMantenimientoItem));

            view.setTag(mantenimientoViewHolder);
        }else {
            mantenimientoViewHolder = (MantenimientoViewHolder) view.getTag();
        }

        final Mantenimiento mantenimiento = arrayListMantenimientos.get(position);

        if (mantenimiento != null) {
            mantenimientoViewHolder.getEstadoMantenimientoItem().setText(mantenimiento.getEstado());
            mantenimientoViewHolder.getNombreMantenimientoItem().setText(mantenimiento.getNombre());
            mantenimientoViewHolder.getDescripcionMantenimientoItem().setText(mantenimiento.getDescripcion());
            mantenimientoViewHolder.getKilometrajeMantenimientoItem().setText(Float.toString(mantenimiento.getKilometrajeReparacion()));
        }

        Typeface font = Typeface.createFromAsset(view.getContext().getAssets(), "fontawesome-webfont.ttf");
        mantenimientoViewHolder.getEstadoMantenimientoItem().setTypeface(font);
        mantenimientoViewHolder.getEstadoSincronizacion().setTypeface(font);

        return view;
    }

    @Override
    public int getCount() {
        return arrayListMantenimientos.size();
    }
}
