package com.alpaca.mrc.domain.room.model;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class Room implements Serializable {

    private String host;
    private List<String> playerList;
    private int maxPlayerNumber;

    public Room(String host, List<String> playerList, int maxPlayerNumber) {
        this.host = host;
        this.playerList = playerList;
        this.maxPlayerNumber = maxPlayerNumber;
    }

    @Override
    public String toString() {
        return "Room{" +
                "host='" + host + '\'' +
                ", playerList=" + playerList +
                ", maxPlayerNumber=" + maxPlayerNumber +
                '}';
    }

    public void updatePlayerList(List<String> playerList) {
        this.playerList = playerList;
    }

    public void addPlayer(String player) {
        this.playerList.add(player);
    }

    public void removePlayer(String player) {
        this.playerList.remove(player);
    }
}