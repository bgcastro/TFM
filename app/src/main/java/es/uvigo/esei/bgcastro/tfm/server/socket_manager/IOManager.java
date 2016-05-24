package es.uvigo.esei.bgcastro.tfm.server.socket_manager;

import java.io.IOException;
import java.util.List;

import es.uvigo.esei.bgcastro.tfm.entities.Opinion;

/**
 * Created by braisgallegocastro on 19/5/16.
 */
public interface IOManager {
    public boolean canRead() throws IOException;

    public void println(String line) throws IOException;
    public String readline() throws IOException;

    public void sendOpinion(Opinion opinion) throws IOException;
    public Opinion readOpinion() throws IOException, ClassNotFoundException;

    public void sendCommand(String command) throws IOException;
    public String receiveCommand() throws IOException;

    public void sendACK() throws IOException;
    public String readACK() throws IOException;

    public void sendNACK() throws IOException;
    public String readNACK() throws IOException;

    public void sendTaller(String taller) throws IOException;
    public String readTaller() throws IOException, ClassNotFoundException;

    public void sendOpinionList(List<Opinion> opinionList) throws IOException;
    public List<Opinion> readOpinionList() throws IOException, ClassNotFoundException;
}
