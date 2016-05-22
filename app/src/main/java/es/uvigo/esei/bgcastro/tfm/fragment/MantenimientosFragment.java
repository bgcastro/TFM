package es.uvigo.esei.bgcastro.tfm.fragment;

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

import es.uvigo.esei.bgcastro.tfm.DAO.VehiculosSQLite;
import es.uvigo.esei.bgcastro.tfm.R;
import es.uvigo.esei.bgcastro.tfm.activitys.GestionMantenimientosActivity;
import es.uvigo.esei.bgcastro.tfm.activitys.GestionVehiculosActivity;
import es.uvigo.esei.bgcastro.tfm.activitys.VehiculosActivity;
import es.uvigo.esei.bgcastro.tfm.content_provider.MantenimientosContentProvider;
import es.uvigo.esei.bgcastro.tfm.entities.Mantenimiento;
import es.uvigo.esei.bgcastro.tfm.entities.Vehiculo;

/**
 * Created by braisgallegocastro on 17/5/16.
 */
public class MantenimientosFragment extends ListFragment {
    private static final String TAG = "MantenimientosFragment";
    private SimpleCursorAdapter simpleCursorAdapter;
    private Cursor cursor;

    private Vehiculo vehiculo;

    private Typeface font;

    private static String[] projection = {};
    private static String where = MantenimientosContentProvider.ID_VEHICULO + "=" + "?" +
            " AND " + MantenimientosContentProvider.ESTADO_REPARACION +"!=" + "?";
    private String[] whereArgs;
    private static String sortOrder = MantenimientosContentProvider.ID_MANTENIMIENTO + " ASC LIMIT 5";

    private static String[] fromColumns = new String[]{
            VehiculosSQLite.COL_NOMBRE,
            VehiculosSQLite.COL_ESTADO_REPARACION};

    private static int[] into = new int[]{
            R.id.nombreMantenimientoItemFragment,
            R.id.estadoMantenimientoItemFragment};

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        vehiculo = ((GestionVehiculosActivity)getActivity()).getVehiculo();
        font = Typeface.createFromAsset(getActivity().getAssets(), "fontawesome-webfont.ttf");

        whereArgs = new String[]{Integer.toString(vehiculo.getId()), getString(R.string.fa_check)};

        cursor = getActivity().getContentResolver().query(MantenimientosContentProvider.CONTENT_URI,projection,where,whereArgs,sortOrder);

        simpleCursorAdapter = new SimpleCursorAdapter(getActivity(), R.layout.mantenimiento_item_fragment,cursor,fromColumns,into,SimpleCursorAdapter.NO_SELECTION);

        simpleCursorAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                switch (view.getId()) {
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

    @Override
    public void onResume() {
        super.onResume();

        cursor = getActivity().getContentResolver().query(MantenimientosContentProvider.CONTENT_URI,projection,where,whereArgs,sortOrder);

        simpleCursorAdapter.swapCursor(cursor);
    }

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

        startActivity(intentModificarMantenimiento);
    }
}
