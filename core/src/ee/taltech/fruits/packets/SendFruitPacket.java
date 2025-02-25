package ee.taltech.fruits.packets;

public class SendFruitPacket extends Packet {
    private int id;
    private float x;
    private float y;
    private int points;
    private int opponentFruitId;

    /**
     * Packet used to send fruit coordinates to clients.
     */
    public SendFruitPacket() {
    }

    /**
     * Make fruit packet that includes game id for what this is meant, fruit's x and y coordinates.
     *
     * @param id int..
     * @param x  coordinate.
     * @param y  coordinate.
     */
    public SendFruitPacket(int id, float x, float y, int points, int opponentFruitId) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.points = points;
        this.opponentFruitId = opponentFruitId;
    }

    /**
     * Return id.
     *
     * @return int.
     */
    public int getId() {
        return id;
    }

    /**
     * Return x coordinate of fruit.
     *
     * @return int.
     */
    public float getX() {
        return x;
    }

    /**
     * Return y coordinate
     *
     * @return int.
     */
    public float getY() {
        return y;
    }

    /**
     * Return points.
     *
     * @return points.
     */
    public int getPoints() {
        return points;
    }

    public int getOpponentFruitId() {
        return opponentFruitId;
    }
}
