package ee.taltech.fruits.packets;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Packet is used to see all ongoing games where player can join.
 */
public class RequestGamesPacket extends Packet {
    Map<Integer, String> gameIdsAndNames;

    /**
     * Make new packet that has HashMap.
     */
    public RequestGamesPacket() {
        gameIdsAndNames = new HashMap<>();
    }

    /**
     * Copy given map keys and values into gameIdsAndNames map
     *
     * @param map given map with gameIds and names.
     */
    public void addGames(Map<Integer, String> map) {
        gameIdsAndNames.putAll(map);
    }

    /**
     * Add one game to gameIdsAndNames map.
     *
     * @param gameId int
     * @param name   name of game.
     */
    public void addGames(int gameId, String name) {
        gameIdsAndNames.put(gameId, name);
    }

    /**
     * Return gameIdsAndNames map.
     *
     * @return Map.
     */
    public Map<Integer, String> getGames() {
        return gameIdsAndNames;
    }
}