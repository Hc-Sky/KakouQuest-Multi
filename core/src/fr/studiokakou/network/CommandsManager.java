package fr.studiokakou.network;

import fr.studiokakou.kakouquest.player.OnlinePlayer;
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
            System.out.print("\n#> ");
            String command = scanner.nextLine();
            computeCommand(command);
        }
    }

    public void computeCommand(String command){
        if (command.startsWith("list")){
            String[] splitCommand = command.split(" ");
            if (splitCommand[1].equals("players") || splitCommand[1].equals("player")){
                GameServer.onlinePlayers.forEach((integer, onlinePlayer) -> {
                    System.out.println("Player "+onlinePlayer.username+" with id : "+integer);
                });
            }
        }

        else if(command.startsWith("help") || command.equals("h")){
            System.out.println("Commands : ");
            System.out.println("list players : list all players");
            System.out.println("regen : regenerate the current level");
            System.out.println("get stats [username] : get stats of a player");
            System.out.println("kick [username] : kick a player");
            System.out.println("set stat speed [username] [value] : set speed of a player");
            System.out.println("set weapon rusty [username] : set rusty sword to a player");
            System.out.println("set weapon anime [username] : set anime sword to a player");
            System.out.println("give xp [username] [value] : give xp to a player");
        }

        else if (command.startsWith("regen")){
            GameServer.currentLevel--;
            gameServer.nextLevel();
        }

        else if (command.startsWith("get")){
            String[] splitCommand = command.split(" ");
            if (splitCommand[1].equals("stats") || splitCommand[1].equals("stat")){
                if (splitCommand.length==3){
                    OnlinePlayer p =GameServer.onlinePlayers.get(gameServer.getIdWithUsername(splitCommand[2]));
                    try {
                        System.out.println("hp : "+p.hp);
                        System.out.println("max hp : "+p.max_hp);
                        System.out.println("stamina : "+p.stamina);
                        System.out.println("max stamina : "+p.max_stamina);
                        System.out.println("position : "+p.pos);
                        System.out.println("strength : "+p.strength);
                        if (p.currentWeapon != null){
                            System.out.println("weapon : "+p.currentWeapon.name);
                        } else {
                            System.out.println("weapon : none");
                        }
                    }catch (Exception e){
                        System.out.println("Player not found...");
                    }

                }
            }
        }

        else if (command.startsWith("kick ")){
            String[] splitCommand = command.split(" ");
            gameServer.server.sendToTCP(gameServer.getIdWithUsername(splitCommand[1]), "kick");
        }

        else if (command.startsWith("set ")){
            String[] splitCommand = command.split(" ");
            if (splitCommand[1].equals("stat")){
                if (splitCommand[2].equals("speed")){
                    int id = gameServer.getIdWithUsername(splitCommand[3]);
                    GameServer.onlinePlayers.get(id).speed = Integer.parseInt(splitCommand[4]);
                    gameServer.changePlayerStats(GameServer.onlinePlayers.get(id));
                }
            }
            else if (splitCommand[1].equals("weapon")){
                if (splitCommand[2].equals("rusty")){
                    int id = gameServer.getIdWithUsername(splitCommand[3]);
                    GameServer.onlinePlayers.get(id).currentWeapon = StaticsMeleeWeapon.RUSTY_SWORD();
                    gameServer.changePlayerStats(GameServer.onlinePlayers.get(id));
                }
                else if (splitCommand[2].equals("anime")){
                    int id = gameServer.getIdWithUsername(splitCommand[3]);
                    GameServer.onlinePlayers.get(id).currentWeapon = StaticsMeleeWeapon.ANIME_SWORD();
                    gameServer.changePlayerStats(GameServer.onlinePlayers.get(id));
                }
            }
        }

        else if(command.startsWith("give")){
            String[] splitCommand = command.split(" ");
            if (splitCommand[1].equals("xp")){
                int id = gameServer.getIdWithUsername(splitCommand[2]);
                GameServer.onlinePlayers.get(id).experience += Integer.parseInt(splitCommand[3]);
                gameServer.changePlayerStats(GameServer.onlinePlayers.get(id));
            }
        }

        else {
            System.out.println("Command ["+command+"] does not exists...");
        }
    }
}
