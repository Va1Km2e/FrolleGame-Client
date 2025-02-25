package ee.taltech.fruits.packets;

/**
 * Packet used to join game.
 */
public class JoinGamePacket extends Packet {
    public JoinGamePacket() {
    }

    public JoinGamePacket(int id) {
        gameId = id;
    }
}
