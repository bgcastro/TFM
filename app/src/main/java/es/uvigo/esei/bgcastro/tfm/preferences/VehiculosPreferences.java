package es.uvigo.esei.bgcastro.tfm.preferences;

/**
 * Created by braisgallegocastro on 15/5/16.
 */
public class VehiculosPreferences {
    public static final String PREFERENCES_FILE = "vehiculos_preferences";

    public static final String MEASURE_UNIT = "measure_unit";
    public static final boolean MEASURE_UNIT_DEFAULT = true;

    public static final String NOTIFICATION_HOUR = "hora_notificacion";
    public static final String NOTIFICATION_HOUR_DEFAULT = "16:00";

    public static final String ALERT_ACTUALIZAR = "alert_actualizar";
    public static final boolean ALERT_ACTUALIZAR_DEFAULT = true;

    public static final String SERVER_ADDRESS = "server_address";
    public static final String SERVER_ADDRESS_DEFAULT = "ec2-52-39-253-121.us-west-2.compute.amazonaws.com";

    public static final String SERVER_PORT = "server_port";
    public static final int SERVER_PORT_DEFAULT = 22291;
}
