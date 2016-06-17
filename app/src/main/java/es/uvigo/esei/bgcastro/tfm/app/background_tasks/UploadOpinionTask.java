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
 */
public class UploadOpinionTask extends AsyncTask<Opinion,Void,Boolean> {
    Context context;
    String serverAddres;
    int puerto;

    public UploadOpinionTask() {
    }

    public UploadOpinionTask(Context context, String serverAddres, int puerto) {
        this.context = context;
        this.serverAddres = serverAddres;
        this.puerto = puerto;
    }

    @Override
    protected Boolean doInBackground(Opinion... params) {
        Socket socket = null;

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

        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if (aBoolean){
            Toast.makeText(context, R.string.upload_succes, Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, R.string.upload_fail, Toast.LENGTH_SHORT).show();
        }

        super.onPostExecute(aBoolean);
    }
}
