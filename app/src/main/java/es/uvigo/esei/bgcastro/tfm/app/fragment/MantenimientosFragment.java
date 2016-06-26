package es.uvigo.esei.bgcastro.tfm.app.fragment;

import android.app.ListFragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import es.uvigo.esei.bgcastro.tfm.R;
import es.uvigo.esei.bgcastro.tfm.app.DAO.VehiculosSQLite;
import es.uvigo.esei.bgcastro.tfm.app.activitys.GestionMantenimientosActivity;
import es.uvigo.esei.bgcastro.tfm.app.activitys.GestionVehiculosActivity;
import es.uvigo.esei.bgcastro.tfm.app.activitys.VehiculosActivity;
import es.uvigo.esei.bgcastro.tfm.app.content_provider.MantenimientosContentProvider;
import es.uvigo.esei.bgcastro.tfm.app.entities.Mantenimiento;
import es.uvigo.esei.bgcastro.tfm.app.entities.Vehiculo;

/**
 * Created by braisgallegocastro on 17/5/16.
 * Fragment que lista mantenimientos pendientes
 */
public class MantenimientosFragment extends ListFragment {
    private static final String TAG = "MantenimientosFragment";
    private static String[] projection = {};
    //Se buscan los estado no reparados
    private static String where = MantenimientosContentProvider.ID_VEHICULO + "=" + "?" +
            " AND " + MantenimientosContentProvider.ESTADO_REPARACION +"!=" + "?";
    private static String sortOrder = MantenimientosContentProvider.ID_MANTENIMIENTO + " ASC LIMIT 5";
    private static String[] fromColumns = new String[]{
            VehiculosSQLite.COL_NOMBRE,
            VehiculosSQLite.COL_ESTADO_REPARACION};
    private static int[] into = new int[]{
            R.id.nombreMantenimientoItemFragment,
            R.id.estadoMantenimientoItemFragment};
    private SimpleCursorAdapter simpleCursorAdapter;
    private Cursor cursor;
    private Vehiculo vehiculo;
    private Typeface font;
    private String[] whereArgs;

    /**
     * Metodo que implementa la logica del fragment
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        vehiculo = ((GestionVehiculosActivity)getActivity()).getVehiculo();
        font = Typeface.createFromAsset(getActivity().getAssets(), "fontawesome-webfont.ttf");

        whereArgs = new String[]{Integer.toString(vehiculo.getId()), getString(R.string.fa_check)};

        cursor = getActivity().getContentResolver().query(MantenimientosContentProvider.CONTENT_URI,projection,where,whereArgs,sortOrder);

        simpleCursorAdapter = new SimpleCursorAdapter(getActivity(), R.layout.mantenimiento_item_fragment,cursor,fromColumns,into,SimpleCursorAdapter.NO_SELECTION);

        //Modificamos algunos elementos de la vista del item
        simpleCursorAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                switch (view.getId()) {
                    //Estado del mantenimiento
                    case R.id.estadoMantenimientoItemFragment: {
                        ((TextView) view).setText(cursor.getString(cursor.getColumnIndex(VehiculosSQLite.COL_ESTADO)));

                        ((TextView) view).setTypeface(font);
                        ((TextView) view).setTextColor(view.getResources().getColor(R.color.grisClaro));
                        ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);

                        return true;
                    }

                    default: return false;
                }
            }
        });


        setListAdapter(simpleCursorAdapter);
        setEmptyText(getString(R.string.empty_mantenimientos_fragment));

        ((TextView) getListView().getEmptyView()).setTextSize(TypedValue.COMPLEX_UNIT_SP,11);
    }

    /**
     * Metodo llamado cuando el fragment pasa a resumed
     */
    @Override
    public void onResume() {
        super.onResume();

        cursor = getActivity().getContentResolver().query(MantenimientosContentProvider.CONTENT_URI,projection,where,whereArgs,sortOrder);

        simpleCursorAdapter.swapCursor(cursor);
    }

    /**
     * Metodo llamado cuando se hace click en un item
     * @param l No usado
     * @param v No usado
     * @param position Posicion del click en la lista
     * @param id Id de la fila donde se hace click
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Intent intentModificarMantenimiento = new Intent(getActivity(), GestionMantenimientosActivity.class);
        Bundle bundle = new Bundle();
        cursor.move(position);

        Mantenimiento mantenimiento = new Mantenimiento(cursor, getActivity());

        bundle.putParcelable(VehiculosActivity.VEHICULO,vehiculo);
        bundle.putParcelable(GestionMantenimientosActivity.MANTENIMIENTO, mantenimiento);

        intentModificarMantenimiento.putExtras(bundle);

        Log.d(TAG, "onItemClick: position" + position);

        //Se lanza la activity GestionMantenimientosActivity
        startActivity(intentModificarMantenimiento);
    }
}
