package es.uvigo.esei.bgcastro.tfm.server.database_connection;

import java.io.File;
import java.util.Properties;

/**
 * Clase que representa los datos de conexion con la BD
 */
public abstract class AbstractConnectionConfiguration implements ConnectionConfiguration {
	public final static File DIR_SQL = new File("sql");
	
	private String userName;
	private String password;

    /**
     * Constructor
     */
    public AbstractConnectionConfiguration() {
		super();
	}

    /**
     * Constructor
     *
     * @param userName Usuario BD
     * @param password Contraseña BD
     */
    public AbstractConnectionConfiguration(String userName, String password) {
		this.setUserName(userName);
		this.setPassword(password);
    }

    /**
     * Metodo que devuelve el usuario
     * @return Nombre de usuario
     */
    public String getUserName() {
		return userName;
    }

    /**
     * Metodo para cambiar el nombre de usuario
     * @param userName Usuario
     */
    public void setUserName(String userName) {
		this.userName = userName;
    }

    /**
     * Metodo que devuelve la contraseña
     * @return Contraseña
     */
    public String getPassword() {
		return password;
    }

    /**
     * Metodo para cambiar la contraseña
     * @param password Contraseña
     */
    public void setPassword(String password) {
		this.password = password;
    }

    /**
     * Metodo que retorna el conjunto de propiedades
     * @return Conjunto de propiedades
     */
    @Override
	public Properties getConnectionProperties() {
		final Properties properties = new Properties();
		
		if (this.userName != null)
			properties.put("user", this.userName);
		
		if (this.password != null)
			properties.put("password", this.password);
		
		return properties;
	}

}