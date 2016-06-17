package es.uvigo.esei.bgcastro.tfm.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import es.uvigo.esei.bgcastro.tfm.R;
import es.uvigo.esei.bgcastro.tfm.app.entities.Opinion;
import es.uvigo.esei.bgcastro.tfm.app.viewholder.OpinionViewHolder;

/**
 * Created by braisgallegocastro on 23/5/16.
 */
public class OpinionArrayAdapter extends ArrayAdapter<Opinion> {
    private ArrayList<Opinion> opinionArrayList;
    private Context contexto;
    private int vistaItem;

    public OpinionArrayAdapter(Context context, int resource) {
        super(context, resource);

        this.opinionArrayList = new ArrayList<>();
        this.contexto = context;
        vistaItem = resource;
    }

    public OpinionArrayAdapter(Context context, int resource, ArrayList<Opinion> opinionArrayList) {
        super(context, resource, opinionArrayList);

        this.opinionArrayList = opinionArrayList;
        this.contexto = context;
        this.vistaItem = resource;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        OpinionViewHolder holder;
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater)contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(vistaItem,parent,false);

            holder = new OpinionViewHolder();
            holder.setNombreTaller((TextView) view.findViewById(R.id.nombreTallerItem));
            holder.setRatingBar((RatingBar) view.findViewById(R.id.ratingBarItem));
            holder.setComentario((TextView) view.findViewById(R.id.comentarioItem));
            holder.setPrecioReparacion((TextView) view.findViewById(R.id.precioReparacionItem));

            view.setTag(holder);
        }else {
            holder = (OpinionViewHolder) view.getTag();
        }

        final Opinion opinion= opinionArrayList.get(position);
        if(opinion != null){

            //asociamos los valores del objeto opinion
            holder.getNombreTaller().setText(opinion.getNombreTaller());
            holder.getRatingBar().setRating(opinion.getValoracion());
            holder.getComentario().setText(opinion.getOpinion());
            holder.getPrecioReparacion().setText(Float.toString(opinion.getPrecioReparacion()));
        }

        return view;
    }

    @Override
    public int getCount() {
        return opinionArrayList.size();
    }

    public void setOpinionArrayList(ArrayList<Opinion> opinionArrayList) {
        this.opinionArrayList = opinionArrayList;
    }
}

