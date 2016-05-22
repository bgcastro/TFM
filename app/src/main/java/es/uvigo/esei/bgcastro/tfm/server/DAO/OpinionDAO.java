package es.uvigo.esei.bgcastro.tfm.server.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import es.uvigo.esei.bgcastro.tfm.entities.Opinion;

public class OpinionDAO {
    private static final String NOMBRE_TABLA = "Opiniones";

    private static final String ID = "_id";
    private static final String VALORACION = "valoracion";
    private static final String PRECIO_VALORACION = "precioReparacion";
    private static final String NOMBRE_TALLER = "nombreTaller";
    private static final String OPINION = "opinion";

	private static final String QUERY_EXISTS_OPINION = "SELECT "+ ID +" FROM " + NOMBRE_TABLA + " WHERE "+ ID +" = ?";
	private static final String QUERY_CREATE_OPINION = "INSERT INTO " + NOMBRE_TABLA + " ("+ ID +", " + VALORACION + ","
            + PRECIO_VALORACION + ", " + NOMBRE_TALLER + ", " + OPINION + ") VALUES (?, ?, ?, ?, ?)";
	
	private final Connection connection;
	
	public OpinionDAO(Connection connection) throws SQLException {
		this.connection = connection;
	}
	
	public boolean existsOpinion(int id) throws SQLException {
		final PreparedStatement queryOpinionExists = this.connection.prepareStatement(QUERY_EXISTS_OPINION);
        
        queryOpinionExists.setInt(1, id);
		
		final ResultSet result = queryOpinionExists.executeQuery();
		try {
			return result.next();
		} finally {
			result.close();
		}
	}
	
	public boolean insertOpinion(Opinion opinion) throws SQLException {
		if (this.existsOpinion(opinion.getId())) {
			// Already exists
			return false;
		} else {
			final PreparedStatement queryCreateOpinion = this.connection.prepareStatement(OpinionDAO.QUERY_CREATE_OPINION);
			
			queryCreateOpinion.setInt(1, opinion.getId());
			queryCreateOpinion.setFloat(2, opinion.getValoracion());
			queryCreateOpinion.setFloat(3, opinion.getPrecioReparacion());
			queryCreateOpinion.setString(4, opinion.getNombreTaller());
			queryCreateOpinion.setString(5, opinion.getOpinion());

			return queryCreateOpinion.executeUpdate() == 1;
		}
	}

	public List<Opinion> listOpinionesFromTaller(String nombreTallerSearch) throws SQLException {
        final PreparedStatement queryOpinion = this.connection.prepareStatement("SELECT * FROM " + NOMBRE_TABLA +
                " WHERE " + NOMBRE_TALLER + " = ?");
		
		queryOpinion.setString(1, nombreTallerSearch);
		
		final ResultSet result = queryOpinion.executeQuery();

		final List<Opinion> opinions = new ArrayList<>();
		while (result.next()) {
            int id = result.getInt(ID);
            int valoracion = result.getInt(VALORACION);
            int precioValoracion = result.getInt(PRECIO_VALORACION);
            String nombreTaller = result.getString(NOMBRE_TALLER);
            String opinion = result.getString(OPINION);

            opinions.add(new Opinion(id, valoracion, precioValoracion, nombreTaller, opinion));
		}
		result.close();
	
		return opinions;
	}

	public List<Opinion> listOpinions() throws SQLException {
		final Statement statement = this.connection.createStatement(); 
		final ResultSet result = statement.executeQuery("SELECT * FROM " + NOMBRE_TABLA);
		
		final List<Opinion> opinions = new ArrayList<>();
		while (result.next()) {
            int id = result.getInt(ID);
            int valoracion = result.getInt(VALORACION);
            int precioValoracion = result.getInt(PRECIO_VALORACION);
            String nombreTaller = result.getString(NOMBRE_TALLER);
            String opinion = result.getString(OPINION);

            opinions.add(new Opinion(id, valoracion, precioValoracion, nombreTaller, opinion));
		}
		result.close();
	
		return opinions;
	}
}
