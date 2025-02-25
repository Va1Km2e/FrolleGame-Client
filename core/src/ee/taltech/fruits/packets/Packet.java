package ee.taltech.fruits.packets;

/**
 * Superclass for other Packets.
 */
public class Packet {
    int gameId;

    public Packet() {
    }

    /**
     * Make new packet instance with given gameId.
     *
     * @param gameId int.
     */
    public Packet(int gameId) {
        this.gameId = gameId;
    }

    /**
     * Set gameId to given one.
     *
     * @param gameId id that every game has.
     */
    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    /**
     * Return id of the game.
     *
     * @return int.
     */
    public int getGameId() {
        return gameId;
    }
}
