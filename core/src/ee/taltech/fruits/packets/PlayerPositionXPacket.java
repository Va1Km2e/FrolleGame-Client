package ee.taltech.fruits.packets;

public class PlayerPositionXPacket extends Packet {

    private float x;
    private int connectionId;

    /**
     * Make a packet with player x position.
     *
     * @param x      player X coordinate.
     * @param gameId int.
     */
    public PlayerPositionXPacket(float x, int gameId) {
        this.x = x;
        this.gameId = gameId;
    }

    public PlayerPositionXPacket() {
    }

    /**
     * return connectionId.
     *
     * @return int.
     */
    public int getConnectionId() {
        return connectionId;
    }

    public float getX() {
        return x;
    }
}
