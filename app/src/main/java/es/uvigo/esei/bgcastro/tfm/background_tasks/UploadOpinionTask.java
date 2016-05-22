package es.uvigo.esei.bgcastro.tfm.background_tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import es.uvigo.esei.bgcastro.tfm.R;
import es.uvigo.esei.bgcastro.tfm.entities.Opinion;
import es.uvigo.esei.bgcastro.tfm.server.socket_manager.SocketIOManager;

/**
 * Created by braisgallegocastro on 22/5/16.
 */
public class UploadOpinionTask extends AsyncTask<Opinion,Void,Boolean> {
    Context context;

    public UploadOpinionTask() {
    }

    public UploadOpinionTask(Context context) {
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Opinion... params) {

        try {
            Socket socket = new Socket(InetAddress.getByName("ec2-52-39-253-121.us-west-2.compute.amazonaws.com"), 22291);

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
