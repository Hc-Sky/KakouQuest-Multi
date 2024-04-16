package fr.studiokakou.network;

import java.io.IOException;

public class RunServer {
    public static void main(String[] args) {
        GameServer gameServer = new GameServer();

        try {
            gameServer.startServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
