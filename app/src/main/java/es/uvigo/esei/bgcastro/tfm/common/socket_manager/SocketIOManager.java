package es.uvigo.esei.bgcastro.tfm.common.socket_manager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import es.uvigo.esei.bgcastro.tfm.app.entities.Opinion;

/**
 * Created by braisgallegocastro on 19/5/16.
 * Implementacion de IOManager que permite la comunicacion entre la aplicacion y el servidor
 */
public class SocketIOManager implements IOManager {
    //Comandos
    public static final String SEND_OPINION = "send_opinion";
    public static final String FIND_OPINIONES_TALLER = "find_opiones_taller";
    //ACKs Y NACKs
    public static final String ACK = "ACK";
    public static final String NACK = "NACK";
    private final Socket socket;
    //Flujos
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;

    /**
     * Constructor
     *
     * @param socket Socket
     * @throws IOException
     */
    public SocketIOManager(Socket socket) throws IOException {
        this.socket = socket;

        this.inputStream = new DataInputStream(socket.getInputStream());
        this.outputStream = new DataOutputStream(socket.getOutputStream());
    }

    /**
     * Metodo para leer una linea de texto
     * @return Linea de texto
     * @throws IOException
     */
    @Override
    public String readline() throws IOException {
        return this.inputStream.readUTF();
    }

    /**
     * Metodo para enviar una linea de texto
     * @param line Texto
     * @throws IOException
     */
    @Override
    public void println(String line) throws IOException {
        this.outputStream.writeUTF(line);
        this.outputStream.flush();
    }

    /**
     * Metodo para leer una Opinion
     * @return Opinion
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public Opinion readOpinion() throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

        return (Opinion) objectInputStream.readObject();
    }

    /**
     * Metodo para enviar un opinion
     * @param opinion Opinion
     * @throws IOException
     */
    @Override
    public void sendOpinion(Opinion opinion) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(opinion);
    }

    /**
     * Metodo para enviar uno de los comandos disponibles
     * @param command Comando
     * @throws IOException
     */
    @Override
    public void sendCommand(String command) throws IOException {
        this.outputStream.writeUTF(command);
        this.outputStream.flush();
    }

    /**
     * Metodo para recivir comadnos
     * @return Comando
     * @throws IOException
     */
    @Override
    public String receiveCommand() throws IOException {
        return this.inputStream.readUTF();
    }

    /**
     * Metodo para enviar un ACK
     * @throws IOException
     */
    @Override
    public void sendACK() throws IOException {
        this.outputStream.writeUTF(ACK);
        this.outputStream.flush();
    }

    /**
     * Metodo para leer un ACK
     * @return ACK
     * @throws IOException
     */
    @Override
    public String readACK() throws IOException {
        return this.inputStream.readUTF();
    }

    /**
     * Metodo para enviar NACK
     * @throws IOException
     */
    @Override
    public void sendNACK() throws IOException {
        this.outputStream.writeUTF(NACK);
        this.outputStream.flush();
    }

    /**
     * Metodo para leer un NACK
     * @return
     * @throws IOException
     */
    @Override
    public String readNACK() throws IOException {
        return this.inputStream.readUTF();
    }

    /**
     * Metodo para enviar el nombre del taller
     * @param taller Taller
     * @throws IOException
     */
    @Override
    public void sendTaller(String taller) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(taller);
    }

    /**
     * Metodo para leer el nombre del taller
     * @return Nombre del taller
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public String readTaller() throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

        return (String) objectInputStream.readObject();
    }

    /**
     * Metodo para enviar una lista de opiniones
     * @param opinionList Lista de opiniones
     * @throws IOException
     */
    @Override
    public void sendOpinionList(List<Opinion> opinionList) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(opinionList);
    }

    /**
     * Metodo para leer una lista de opiniones
     * @return Lista de opiniones
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public List<Opinion> readOpinionList() throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

        return (List<Opinion>) objectInputStream.readObject();
    }

    /**
     * Metodo para saber si se puede leer
     * @return True si se puede leer
     * @throws IOException
     */
    @Override
    public boolean canRead() throws IOException {
        return !this.socket.isClosed() && this.socket.getInputStream().available() > 0;
    }

    /**
     * Metodo que devuelve el socket
     * @return Socket
     */
    public Socket getSocket() {
        return socket;
    }
}
