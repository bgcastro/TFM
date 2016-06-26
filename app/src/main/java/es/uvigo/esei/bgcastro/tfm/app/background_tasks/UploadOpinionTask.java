package es.uvigo.esei.bgcastro.tfm.app.background_tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import es.uvigo.esei.bgcastro.tfm.R;
import es.uvigo.esei.bgcastro.tfm.app.entities.Opinion;
import es.uvigo.esei.bgcastro.tfm.common.socket_manager.SocketIOManager;

/**
 * Created by braisgallegocastro on 22/5/16.
 * Tarea en segundo plano que se encarga de subir una opinion al servidor
 */
public class UploadOpinionTask extends AsyncTask<Opinion,Void,Boolean> {
    Context context;
    String serverAddres;
    int puerto;

    /**
     * Constructor
     */
    public UploadOpinionTask() {
    }

    /**
     * Constructor
     *
     * @param context      Contexto
     * @param serverAddres Direccion del servidor
     * @param puerto       Numero de puerto
     */
    public UploadOpinionTask(Context context, String serverAddres, int puerto) {
        this.context = context;
        this.serverAddres = serverAddres;
        this.puerto = puerto;
    }

    /**
     * Metodo que realiza la subida al servidor
     * @param params
     * @return
     */
    @Override
    protected Boolean doInBackground(Opinion... params) {
        Socket socket = null;

        //Conexion y subida de datos
        try {
            socket = new Socket(InetAddress.getByName(serverAddres), puerto);

            SocketIOManager socketIOManager = new SocketIOManager(socket);

            socketIOManager.sendCommand(SocketIOManager.SEND_OPINION);
            String s = socketIOManager.readACK();
            if (s.equals(SocketIOManager.ACK)){
                socketIOManager.sendOpinion(params[0]);
                String s2 = socketIOManager.readACK();
                if (!s2.equals(SocketIOManager.ACK)){
                    return false;
                }
            }else {
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }finally {
            if (socket!=null && !socket.isClosed()){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //Se ha tenido exito al subir los datos
        return true;
    }

    /**
     * Metodo llamado al realizarse la subida
     * @param aBoolean Exito de la subida
     */
    @Override
    protected void onPostExecute(Boolean aBoolean) {
        //Si exito
        if (aBoolean){
            Toast.makeText(context, R.string.upload_succes, Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, R.string.upload_fail, Toast.LENGTH_SHORT).show();
        }

        super.onPostExecute(aBoolean);
    }
}
