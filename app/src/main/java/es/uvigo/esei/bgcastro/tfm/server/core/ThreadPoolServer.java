package es.uvigo.esei.bgcastro.tfm.server.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.uvigo.esei.bgcastro.tfm.app.entities.Opinion;
import es.uvigo.esei.bgcastro.tfm.common.socket_manager.SocketIOManager;
import es.uvigo.esei.bgcastro.tfm.server.DAO.OpinionDAO;
import es.uvigo.esei.bgcastro.tfm.server.database_connection.MySQLConnectionConfiguration;
import es.uvigo.esei.bgcastro.tfm.server.db_controler.DBController;

/**
 * Created by braisgallegocastro on 19/5/16.
 * Clase principal del servidor
 */
public class ThreadPoolServer {
    //Datos BD
    private final static String DB_USER_NAME = "opiniones";
	private final static String DB_PASSWORD = "opiniones";
	private final static String DB_NAME = "opiniones";
	private static final String HOST = "localhost";

	private static DBController dbController;

	public static void main(String[] args) {
		ExecutorService threadPool = Executors.newCachedThreadPool();
		ServerSocket serverSocket = null;

		System.out.println("El servidor está arrancado");

        //Se abre una conexion y se esperan peticiones
        try {
			serverSocket = new ServerSocket(22291);

			dbController = new DBController(new MySQLConnectionConfiguration(DB_USER_NAME, DB_PASSWORD, HOST, DB_NAME, -1));

			while (true) {
				Socket clientSocket = serverSocket.accept();

                //Cuando llega una peticion nueva se atiende
                threadPool.execute(new ServiceThread(clientSocket, dbController));
			}
		} catch (IOException e) {
			System.err.println("No se ha podido crear el server socket");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (serverSocket != null && !serverSocket.isClosed()) {
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			dbController.shutdown();
		}
	}

    /**
     * Hilo de servicio
     */
    private static class ServiceThread implements Runnable {
		private final Socket socket;
		private DBController dbController;

        /**
         * Constructor
         *
         * @param clientSocket Socket
         * @param dbController Contralador de la BD
         */
        public ServiceThread(Socket clientSocket, DBController dbController) {
			this.socket = clientSocket;
			this.dbController = dbController;
        }

        /**
         * Metodo que implementa la logica
         */
        @Override
		public void run() {

			try {
				SocketIOManager ioManager = new SocketIOManager(this.socket);

				String command = ioManager.receiveCommand();
				//Se trata de una subida de datos
				if(command.equals(SocketIOManager.SEND_OPINION)){
					ioManager.sendACK();

					Opinion opinion = ioManager.readOpinion();

					OpinionDAO opinionDAO = new OpinionDAO(dbController.getConnection());

					boolean success = opinionDAO.insertOpinion(opinion);

					if(success){
						ioManager.sendACK();
					}else {
						ioManager.sendNACK();
					}
				}

				//se trata de una consulta de datos
				if(command.equals(SocketIOManager.FIND_OPINIONES_TALLER)){
					ioManager.sendACK();

					String taller = ioManager.readTaller();

					OpinionDAO opinionDAO = new OpinionDAO(dbController.getConnection());
					ArrayList<Opinion> opiniones = (ArrayList<Opinion>) opinionDAO.listOpinionesFromTaller(taller);

					ioManager.sendACK();

					ioManager.sendOpinionList(opiniones);
				}

				ioManager.sendNACK();


			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (socket != null && !socket.isClosed())
						socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
