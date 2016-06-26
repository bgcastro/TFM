package es.uvigo.esei.bgcastro.tfm.common.socket_manager;

import java.io.IOException;
import java.util.List;

import es.uvigo.esei.bgcastro.tfm.app.entities.Opinion;

/**
 * Created by braisgallegocastro on 19/5/16.
 * Interfaz para la gestion del envio de mensajes entre el servidor y la aplicacion
 */
public interface IOManager {
    boolean canRead() throws IOException;

    void println(String line) throws IOException;
    String readline() throws IOException;

    void sendOpinion(Opinion opinion) throws IOException;
    Opinion readOpinion() throws IOException, ClassNotFoundException;

    void sendCommand(String command) throws IOException;
    String receiveCommand() throws IOException;

    void sendACK() throws IOException;
    String readACK() throws IOException;

    void sendNACK() throws IOException;
    String readNACK() throws IOException;

    void sendTaller(String taller) throws IOException;
    String readTaller() throws IOException, ClassNotFoundException;

    void sendOpinionList(List<Opinion> opinionList) throws IOException;
    List<Opinion> readOpinionList() throws IOException, ClassNotFoundException;
}
