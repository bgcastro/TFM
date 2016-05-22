package es.uvigo.esei.bgcastro.tfm.server.database_connection;

import java.util.Properties;

public interface ConnectionConfiguration {
	public String getConnectionString();
	public Properties getConnectionProperties();
}
