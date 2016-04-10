package es.uvigo.esei.bgcastro.tfm.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import es.uvigo.esei.bgcastro.tfm.R;
import es.uvigo.esei.bgcastro.tfm.entitys.Vehiculo;
import es.uvigo.esei.bgcastro.tfm.viewholder.VehiculoViewHolder;

/**
 * Created by braisgallegocastro on 23/12/15.
 */
public class VehiculoAdapter extends ArrayAdapter<Vehiculo> {
    private ArrayList<Vehiculo> vehiculoArrayList;
    private Context contexto;
    private int vistaItem;

    public VehiculoAdapter(Context context, int resource, ArrayList<Vehiculo> vehiculos) {
        super(context, resource, vehiculos);

        this.vehiculoArrayList = vehiculos;
        this.contexto = context;
        this.vistaItem = resource;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        VehiculoViewHolder holder;
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater)contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(vistaItem,parent,false);

            holder = new VehiculoViewHolder();
            holder.setImagenVehiculo((ImageView) view.findViewById(R.id.imagenVehiculo));
            holder.setNombreVehiculo((TextView) view.findViewById(R.id.nombreVehiculo));
            holder.setMatricula((TextView) view.findViewById(R.id.matricula));
            holder.setKilometraje((TextView) view.findViewById(R.id.kilometraje));
            holder.setEstadoVehiculo((TextView) view.findViewById(R.id.estadoVehiculo));

            view.setTag(holder);
        }else {
            holder = (VehiculoViewHolder) view.getTag();
        }

        final Vehiculo vehiculo= vehiculoArrayList.get(position);
        if(vehiculo != null){

            //asociamos los valores del objeto vehiculo
            byte[] img = vehiculo.getImagenVehiculo();

            holder.getImagenVehiculo().setImageBitmap(BitmapFactory.decodeByteArray(img, 0, img.length));
            holder.getNombreVehiculo().setText(vehiculo.getModelo());
            holder.getMatricula().setText(vehiculo.getMatricula());
            holder.getKilometraje().setText(Float.toString(vehiculo.getKilometraje()));
            holder.getEstadoVehiculo().setText(vehiculo.getEstado());

        }

        Typeface font = Typeface.createFromAsset(view.getContext().getAssets(), "fontawesome-webfont.ttf");
        holder.getEstadoVehiculo().setTypeface(font);
        holder.getEstadoVehiculo().setTextColor(view.getResources().getColor(R.color.grisClaro));
        holder.getEstadoVehiculo().setTextSize(TypedValue.COMPLEX_UNIT_SP,22);

        return view;
    }

    @Override
    public int getCount() {
        return vehiculoArrayList.size();
    }
}
