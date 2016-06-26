package es.uvigo.esei.bgcastro.tfm.server.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import es.uvigo.esei.bgcastro.tfm.app.entities.Opinion;

/**
 * Clase para el acceso a BD
 */
public class OpinionDAO {
    private static final String NOMBRE_TABLA = "Opiniones";

    //Campos BD
    private static final String ID = "_id";
    private static final String VALORACION = "valoracion";
    private static final String PRECIO_VALORACION = "precioReparacion";
    private static final String NOMBRE_TALLER = "nombreTaller";
    private static final String OPINION = "opinion";

    //Querys
    private static final String QUERY_EXISTS_OPINION = "SELECT "+ ID +" FROM " + NOMBRE_TABLA + " WHERE "+ ID +" = ?";
	private static final String QUERY_CREATE_OPINION = "INSERT INTO " + NOMBRE_TABLA + " ("+ ID +", " + VALORACION + ","
            + PRECIO_VALORACION + ", " + NOMBRE_TALLER + ", " + OPINION + ") VALUES (?, ?, ?, ?, ?)";

    //Conexion
    private final Connection connection;

    /**
     * Constructor
     *
     * @param connection Conexion BD
     * @throws SQLException
     */
    public OpinionDAO(Connection connection) throws SQLException {
		this.connection = connection;
    }

    /**
     * Metodo para comprabarsi existe una opinion
     * @param id ID
     * @return True si existe
     * @throws SQLException
     */
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

    /**
     * Metodo para guardar una opinion
     * @param opinion Opinion
     * @return True si se guarda
     * @throws SQLException
     */
    public boolean insertOpinion(Opinion opinion) throws SQLException {
		if (this.existsOpinion(opinion.getId())) {
            // Ya existe
            return false;
		} else {
			final PreparedStatement queryCreateOpinion = this.connection.prepareStatement(OpinionDAO.QUERY_CREATE_OPINION);
			
			queryCreateOpinion.setInt(1, opinion.getId());
			queryCreateOpinion.setFloat(2, opinion.getValoracion());
			queryCreateOpinion.setFloat(3, opinion.getPrecioReparacion());
			queryCreateOpinion.setString(4, opinion.getNombreTaller());
			queryCreateOpinion.setString(5, opinion.getOpinion());

			return queryCreateOpinion.executeUpdate() > 0;
        }
    }

    /**
     * Metodo para buscar opiniones a partir del nombre del taller
     * @param nombreTallerSearch Nombre taller
     * @return Lista de coincidencias
     * @throws SQLException
     */
    public List<Opinion> listOpinionesFromTaller(String nombreTallerSearch) throws SQLException {
        final PreparedStatement queryOpinion = this.connection.prepareStatement("SELECT * FROM " + NOMBRE_TABLA +
                " WHERE " + NOMBRE_TALLER + " LIKE ?");
		
		queryOpinion.setString(1, "%" + nombreTallerSearch + "%");
		
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

    /**
     * Metodo para buscar todas las opiniones
     * @return Lista de opiniones
     * @throws SQLException
     */
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
