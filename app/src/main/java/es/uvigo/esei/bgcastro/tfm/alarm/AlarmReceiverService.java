package es.uvigo.esei.bgcastro.tfm.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import es.uvigo.esei.bgcastro.tfm.R;
import es.uvigo.esei.bgcastro.tfm.activitys.GestionMantenimientosActivity;
import es.uvigo.esei.bgcastro.tfm.activitys.VehiculosActivity;
import es.uvigo.esei.bgcastro.tfm.content_provider.MantenimientosContentProvider;
import es.uvigo.esei.bgcastro.tfm.content_provider.VehiculoContentProvider;
import es.uvigo.esei.bgcastro.tfm.entities.Mantenimiento;
import es.uvigo.esei.bgcastro.tfm.entities.Vehiculo;

/**
 * Created by braisgallegocastro on 19/3/16.
 */
public class AlarmReceiverService extends Service{
    private static final String TAG = "AlarmReceiverService";

    private String titulo;
    private Mantenimiento mantenimiento;
    private Vehiculo vehiculo;
    private String contenido;

    public static final String TITULO = "titulo";
    public static final String CONTENIDO = "contenido";
    public static final String MANTENIMIENTO = "mantenimiento";

    public static int requestCode = 1;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        titulo = intent.getStringExtra(TITULO);
        contenido = intent.getStringExtra(CONTENIDO);
        mantenimiento = intent.getParcelableExtra(MANTENIMIENTO);

        Log.d(TAG, "onCreate: " + titulo +
        "mantenimiento" + mantenimiento);

        //Se cambia el estado del mantenimiento a necesita reparacion
        ContentValues contentValuesMantenimientos = new ContentValues();
        contentValuesMantenimientos.put(MantenimientosContentProvider.ESTADO_REPARACION,getString(R.string.fa_exclamation_triangle));

        Uri uriMantenimiento = Uri.withAppendedPath(MantenimientosContentProvider.CONTENT_URI,Integer.toString(mantenimiento.getId()));
        getContentResolver().update(uriMantenimiento,contentValuesMantenimientos,null,null);

        //Se cambia el estado del vehiculo a necesita reparacion
        ContentValues contentValuesVehiculos = new ContentValues();
        contentValuesVehiculos.put(VehiculoContentProvider.ESTADO,getString(R.string.fa_exclamation_triangle));

        Uri uriVehiculo = Uri.withAppendedPath(VehiculoContentProvider.CONTENT_URI,Integer.toString(mantenimiento.getVehiculo().getId()));
        getContentResolver().update(uriVehiculo,contentValuesVehiculos,null,null);

        //Se crea la notificacion
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(titulo);
        builder.setContentText(contenido);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setAutoCancel(true);

        //Se crea el intent que se va a lanzar la activity cuando se hace click en la notificacion
        Intent resultIntent = new Intent(this,GestionMantenimientosActivity.class);
        resultIntent.putExtra(GestionMantenimientosActivity.MANTENIMIENTO, mantenimiento);
        resultIntent.putExtra(VehiculosActivity.VEHICULO, vehiculo);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(resultPendingIntent);

        //Se notifica
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(mantenimiento.getId(), builder.build());

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
