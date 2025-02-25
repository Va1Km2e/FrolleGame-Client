package ee.taltech.fruits.packets;

public class PlayerPositionYPacket extends Packet {

    private float y;
    private int connectionId;

    /**
     * Make a packet with player y position.
     *
     * @param y      player Y coordinate.
     * @param gameId int.
     */
    public PlayerPositionYPacket(float y, int gameId) {
        this.y = y;
        this.gameId = gameId;
    }

    public PlayerPositionYPacket() {
    }

    /**
     * return connectionId.
     *
     * @return int.
     */
    public int getConnectionId() {
        return connectionId;
    }

    public float getY() {
        return y;
    }
}
