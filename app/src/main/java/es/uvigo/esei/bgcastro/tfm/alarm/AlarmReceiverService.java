package es.uvigo.esei.bgcastro.tfm.alarm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import es.uvigo.esei.bgcastro.tfm.R;
import es.uvigo.esei.bgcastro.tfm.activitys.GestionMantenimientosActivity;
import es.uvigo.esei.bgcastro.tfm.activitys.VehiculosActivity;
import es.uvigo.esei.bgcastro.tfm.entitys.Mantenimiento;
import es.uvigo.esei.bgcastro.tfm.entitys.Vehiculo;

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

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(titulo);
        builder.setContentText(contenido);
        builder.setAutoCancel(true);

        Intent resultIntent = new Intent(this,GestionMantenimientosActivity.class);
        resultIntent.putExtra(GestionMantenimientosActivity.MANTENIMIENTO, mantenimiento);
        resultIntent.putExtra(VehiculosActivity.VEHICULO, vehiculo);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(resultPendingIntent);
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
