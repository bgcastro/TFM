package es.uvigo.esei.bgcastro.tfm.server.socket_manager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import es.uvigo.esei.bgcastro.tfm.entities.Opinion;

/**
 * Created by braisgallegocastro on 19/5/16.
 */
public class SocketIOManager implements IOManager {
    private final Socket socket;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;

    public static final String SEND_OPINION = "send_opinion";
    public static final String FIND_OPINIONES_TALLER = "find_opiones_taller";
    public static final String ACK = "ACK";
    public static final String NACK = "NACK";

    public SocketIOManager(Socket socket) throws IOException {
        this.socket = socket;

        this.inputStream = new DataInputStream(socket.getInputStream());
        this.outputStream = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public String readline() throws IOException {
        return this.inputStream.readUTF();
    }

    @Override
    public void println(String line) throws IOException {
        this.outputStream.writeUTF(line);
        this.outputStream.flush();
    }

    @Override
    public Opinion readOpinion() throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

        return (Opinion) objectInputStream.readObject();
    }

    @Override
    public void sendOpinion(Opinion opinion) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(opinion);
    }

    @Override
    public void sendCommand(String command) throws IOException {
        this.outputStream.writeUTF(command);
        this.outputStream.flush();
    }

    @Override
    public String receiveCommand() throws IOException {
        return this.inputStream.readUTF();
    }

    @Override
    public void sendACK() throws IOException {
        this.outputStream.writeUTF(ACK);
        this.outputStream.flush();
    }

    @Override
    public String readACK() throws IOException {
        return this.inputStream.readUTF();
    }

    @Override
    public void sendNACK() throws IOException {
        this.outputStream.writeUTF(NACK);
        this.outputStream.flush();
    }

    @Override
    public String readNACK() throws IOException {
        return this.inputStream.readUTF();
    }

    @Override
    public void sendTaller(String taller) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(taller);
    }

    @Override
    public String readTaller() throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

        return (String) objectInputStream.readObject();
    }

    @Override
    public void sendOpinionList(List<Opinion> opinionList) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(opinionList);
    }

    @Override
    public List<Opinion> readOpinionList() throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

        return (List<Opinion>) objectInputStream.readObject();
    }

    @Override
    public boolean canRead() throws IOException {
        if (!this.socket.isClosed() && this.socket.getInputStream().available() > 0){
            return true;
        }else {
            return false;
        }
    }

    public Socket getSocket() {
        return socket;
    }
}
