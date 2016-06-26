package es.uvigo.esei.bgcastro.tfm.server.database_connection;

import java.util.Properties;

/**
 * Interfaz para representar los datos de conexion
 */
public interface ConnectionConfiguration {
	String getConnectionString();
	Properties getConnectionProperties();
}
