package es.uvigo.esei.bgcastro.tfm.server.db_controler;

import java.sql.Connection;
import java.sql.SQLException;

import es.uvigo.esei.bgcastro.tfm.server.database_connection.ConnectionConfiguration;
import es.uvigo.esei.bgcastro.tfm.server.database_connection.ConnectionUtils;

public class DBController {
	private final Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public DBController(ConnectionConfiguration configuration) throws SQLException {
		this.connection = ConnectionUtils.getConnection(configuration);
	}

	public void shutdown() {
		try {
			this.connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
