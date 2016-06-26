package es.uvigo.esei.bgcastro.tfm.server.database_connection;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase que representa la conexion con una BD MySQL
 */
public class MySQLConnectionConfiguration extends AbstractConnectionConfiguration implements ConnectionConfiguration {
    //Configuracion
    public static final Integer[] MYSQL_IGNORED_CREATION_ERRORS = new Integer[] { 1050 };
	public static final File OPINIONES_MYSQL_SQL_FILE = new File(DIR_SQL, "opiniones.mysql.sql");
    private final Map<String, String> properties;
    private String serverName;
	private String dbName;
	private int portNumber;

    /**
     * Constructor
     */
    public MySQLConnectionConfiguration() {
		this(null, null, null, null, -1);
	}

    /**
     * Contructor
     *
     * @param userName   Usuario
     * @param password   Contraseña
     * @param serverName Servidor
     * @param dbName     Nombre BD
     * @param portNumber Pueto
     * @throws IllegalArgumentException
     */
    public MySQLConnectionConfiguration(
		String userName, String password,
		String serverName, String dbName, int portNumber
	) throws IllegalArgumentException {
		super(userName, password);
		
		this.setServerName(serverName);
		this.setDbName(dbName);
		this.setPortNumber(portNumber);
		
		this.properties = new HashMap<String, String>();
    }

    /**
     * Metodo que devuelve el servidor
     * @return Servidor
     */
    public String getServerName() {
		return serverName;
    }

    /**
     * Metodo para cambiar el nombre del servidor
     * @param serverName Nombre servidor
     */
    public void setServerName(String serverName) {
		this.serverName = serverName;
    }

    /**
     * Metodo que devuelve el nombre de la BD
     * @return Nombre BD
     */
    public String getDbName() {
		return dbName;
    }

    /**
     * Metodo para cambiar el nombre de la BD
     * @param dbName Nombre de la bd
     */
    public void setDbName(String dbName) {
		this.dbName = dbName;
    }

    /**
     * Metodo que devuelve el numero de puerto
     * @return Numero de puertp
     */
    public int getPortNumber() {
		return portNumber;
    }

    /**
     * Metodo para cambiar el puerto
     *
     * @param portNumber Numero de puerto
     * @throws IllegalArgumentException
     */
    public void setPortNumber(int portNumber)
            throws IllegalArgumentException {
        if (portNumber > 65535)
            throw new IllegalAccessError("Port number must be in range [0, 65535] or negative for disable");

        this.portNumber = portNumber;
    }

    /**
     * Metodo que devuelve el conjunto de propiedades
     * @return Propiedades
     */
    public Map<String, String> getProperties() {
		return Collections.unmodifiableMap(this.properties);
    }

    /**
     * Metodo para cambiar el conjunto de propiedades
     * @param property Propiedades
     * @param value Valores
     * @return
     */
    public String putProperty(String property, String value) {
		return this.properties.put(property, value);
    }

    /**
     * Metodo que devuelve una propiedad
     * @param property Nombre propiedad
     * @return Valor
     */
    public String getProperty(String property) {
		return this.properties.get(property);
    }

    /**
     * Metodo para eliminar una propiedad
     * @param property Nombre de la propiedad
     * @return El valor borrado o null
     */
    public String removeProperty(String property) {
		return this.properties.remove(property);
    }

    /**
     * Metodo que genera un String con la conexion para JDBC
     * @return String de conexion
     */
    @Override
	public String getConnectionString() {
		final StringBuilder sb = new StringBuilder("jdbc:mysql://");
		
		if (this.getServerName() != null)
			sb.append(this.getServerName());
		
		
		if (this.getPortNumber() >= 0)
			sb.append(':').append(this.getPortNumber());
		
		sb.append('/');
		
		if (this.getDbName() != null)
			sb.append(this.getDbName());
		
		boolean isFirst = true;
		for (Map.Entry<String, String> property : this.properties.entrySet()) {
			sb.append(isFirst ? '?' : '&');
			isFirst = false;
			
			sb.append(property.getKey()).append("=")
				.append(property.getValue());
		}
		
		return sb.toString();
	}
}
