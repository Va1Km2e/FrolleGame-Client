package ee.taltech.fruits.packets;

public class FruitCollisionPacket extends Packet {
    private int fruitId;
    private float playerX;
    private float playerY;
    private int playerId;


    public FruitCollisionPacket() {
    }

    /**
     * When player collides with fruit make a new packet.
     *
     * @param fruitId int of the fruit collided with.
     * @param playerX int player X coordinates that touched the fruit,
     * @param playerY int player Y coordinates that touched the fruit.
     * @param gameId  int gameId where player is.
     */
    public FruitCollisionPacket(int fruitId, float playerX, float playerY, int gameId) {
        this.gameId = gameId;
        this.fruitId = fruitId;
        this.playerX = playerX;
        this.playerY = playerY;
    }

    /**
     * Return fruitId.
     *
     * @return int.
     */
    public int getFruitId() {
        return fruitId;
    }

    /**
     * Return player X coordinate.
     *
     * @return int.
     */
    public float getPlayerX() {
        return playerX;
    }

    /**
     * Return player Y coordinate.
     *
     * @return int.
     */
    public float getPlayerY() {
        return playerY;
    }
    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public void setFruitId(int fruitId) {
        this.fruitId = fruitId;
    }
}
