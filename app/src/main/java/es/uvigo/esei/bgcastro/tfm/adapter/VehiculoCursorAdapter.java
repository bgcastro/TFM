package es.uvigo.esei.bgcastro.tfm.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import es.uvigo.esei.bgcastro.tfm.R;
import es.uvigo.esei.bgcastro.tfm.entities.Vehiculo;
import es.uvigo.esei.bgcastro.tfm.viewholder.VehiculoViewHolder;

/**
 * Created by braisgallegocastro on 13/4/16.
 */
public class VehiculoCursorAdapter extends CursorAdapter {
    private int vistaItem;

    public VehiculoCursorAdapter(Context context, Cursor c, int flags, int vistaItem) {
        super(context, c, flags);
        this.vistaItem = vistaItem;
    }

    @Override
    public View newView(Context contexto, Cursor cursor, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(vistaItem,parent,false);

        VehiculoViewHolder holder;
        holder = new VehiculoViewHolder();
        holder.setImagenVehiculo((ImageView) view.findViewById(R.id.imagenVehiculo));
        holder.setNombreVehiculo((TextView) view.findViewById(R.id.nombreMarca));
        holder.setMatricula((TextView) view.findViewById(R.id.matricula));
        holder.setKilometraje((TextView) view.findViewById(R.id.kilometraje));
        holder.setEstadoVehiculo((TextView) view.findViewById(R.id.estadoVehiculo));

        view.setTag(holder);

        final Vehiculo vehiculo = new Vehiculo(cursor);
        if(vehiculo != null){

            //asociamos los valores del objeto vehiculo
            byte[] img = vehiculo.getImagenVehiculo();

            holder.getImagenVehiculo().setImageBitmap(BitmapFactory.decodeByteArray(img, 0, img.length));
            holder.getNombreVehiculo().setText(vehiculo.getModelo());
            holder.getMatricula().setText(vehiculo.getMatricula());
            holder.getKilometraje().setText(Float.toString(vehiculo.getKilometraje()));
            holder.getEstadoVehiculo().setText(vehiculo.getEstado());

        }

        final Typeface font = Typeface.createFromAsset(view.getContext().getAssets(), "fontawesome-webfont.ttf");
        holder.getEstadoVehiculo().setTypeface(font);
        holder.getEstadoVehiculo().setTextColor(view.getResources().getColor(R.color.grisClaro));
        holder.getEstadoVehiculo().setTextSize(TypedValue.COMPLEX_UNIT_SP,22);

        return view;
    }



    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        VehiculoViewHolder holder = (VehiculoViewHolder) view.getTag();
        final Vehiculo vehiculo = new Vehiculo(cursor);
        if(vehiculo != null){

            //asociamos los valores del objeto vehiculo
            byte[] img = vehiculo.getImagenVehiculo();

            holder.getImagenVehiculo().setImageBitmap(BitmapFactory.decodeByteArray(img, 0, img.length));
            holder.getNombreVehiculo().setText(vehiculo.getModelo());
            holder.getMatricula().setText(vehiculo.getMatricula());
            holder.getKilometraje().setText(Float.toString(vehiculo.getKilometraje()));
            holder.getEstadoVehiculo().setText(vehiculo.getEstado());

        }

        final Typeface font = Typeface.createFromAsset(view.getContext().getAssets(), "fontawesome-webfont.ttf");
        holder.getEstadoVehiculo().setTypeface(font);
        holder.getEstadoVehiculo().setTextColor(view.getResources().getColor(R.color.grisClaro));
        holder.getEstadoVehiculo().setTextSize(TypedValue.COMPLEX_UNIT_SP,22);
    }
}
