package es.uvigo.esei.bgcastro.tfm.app.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import es.uvigo.esei.bgcastro.tfm.app.entities.Opinion;
import es.uvigo.esei.bgcastro.tfm.common.socket_manager.SocketIOManager;

/**
 * Created by braisgallegocastro on 23/5/16.
 * Clase loader que descarga opiniones del servidor
 */
public class OpinionesListLoader extends AsyncTaskLoader<List<Opinion>>{
    private final String serverAddress;
    private final int port;
    private String nombreTaller;
    private ArrayList<Opinion> opinions;

    /**
     * Constructor
     *
     * @param context       Contexto
     * @param serverAddress Direccion del servidor
     * @param port          Puerto
     * @param nombreTaller  Nobre taller
     */
    public OpinionesListLoader(Context context,String serverAddress, int port, String nombreTaller) {
        super(context);
        this.nombreTaller = nombreTaller;
        this.serverAddress = serverAddress;
        this.port = port;
    }

    /**
     * Tarea en segundo plano que se encarga de cargar en background las opiniones
     * @return
     */
    @Override
    public List<Opinion> loadInBackground() {
        Socket socket = null;
        ArrayList<Opinion> opinionArrayList = null;

        //Conexion y descarga de datos
        try {
            socket = new Socket(InetAddress.getByName(serverAddress), port);

            SocketIOManager socketIOManager = new SocketIOManager(socket);

            socketIOManager.sendCommand(SocketIOManager.FIND_OPINIONES_TALLER);
            if (socketIOManager.readACK().equals(SocketIOManager.ACK)){
                socketIOManager.sendTaller(nombreTaller);
                if (socketIOManager.readACK().equals(SocketIOManager.ACK)){
                    opinionArrayList = (ArrayList<Opinion>) socketIOManager.readOpinionList();
                   return opinionArrayList;
                }else {
                    return opinionArrayList;
                }
            }else{
                return opinionArrayList;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }finally {
            if (socket != null && !socket.isClosed()){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Metodo llamado cuando se acaba de obtener los resultados
     * @param data Datos cargados
     */
    @Override
    public void deliverResult(List<Opinion> data) {
        //para que el recolector de basura no las elimine antes de tiempo
        ArrayList<Opinion> opinionesViejas = opinions;

        opinions = (ArrayList<Opinion>) data;

        super.deliverResult(opinions);

        opinionesViejas = null;
    }

    /**
     * Metodo llamado al inicio de la carga de datos
     */
   @Override
    protected void onStartLoading() {
        if (takeContentChanged())
            forceLoad();
   }

    /**
     * Metodo llamado cuando se cancela la carga de datos
     */
    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    /**
     * Metodo llamado cuando se hace un reset
     */
    @Override
    protected void onReset() {
        // Asegurarse de que esta parado
        onStopLoading();
        opinions = null;
    }

}
