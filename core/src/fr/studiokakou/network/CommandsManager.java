package fr.studiokakou.network;

import fr.studiokakou.kakouquest.weapon.StaticsMeleeWeapon;

import java.util.Scanner;

public class CommandsManager implements Runnable {
    GameServer gameServer;
    Scanner scanner;

    public CommandsManager(GameServer gameServer){
        this.gameServer=gameServer;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void run() {
        while (true){
            System.out.print("#> ");
            String command = scanner.nextLine();
            computeCommand(command);
        }
    }

    public void computeCommand(String command){
        if (command.startsWith("kick ")){
            String[] splitCommand = command.split(" ");
            gameServer.server.sendToTCP(gameServer.getIdWithUsername(splitCommand[1]), "kick");
        }

        if (command.startsWith("set ")){
            String[] splitCommand = command.split(" ");
            if (splitCommand[1].equals("stat")){
                if (splitCommand[2].equals("speed")){
                    int id = gameServer.getIdWithUsername(splitCommand[3]);
                    GameServer.onlinePlayers.get(id).speed = Integer.parseInt(splitCommand[4]);
                    gameServer.changePlayerStats(GameServer.onlinePlayers.get(id));
                }
            }

            if (splitCommand[1].equals("weapon")){
                if (splitCommand[2].equals("anime")){
                    int id = gameServer.getIdWithUsername(splitCommand[3]);
                    GameServer.onlinePlayers.get(id).currentWeapon = StaticsMeleeWeapon.ANIME_SWORD();
                    gameServer.changePlayerStats(GameServer.onlinePlayers.get(id));
                }
            }
        }

        else {
            System.out.println("Command ["+command+"] does not exists...");
        }
    }
}