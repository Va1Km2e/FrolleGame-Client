package ee.taltech.fruits.packets;

public class SheepAteFruitPacket extends Packet {
    private int fruitId;
    public SheepAteFruitPacket() { }
    public SheepAteFruitPacket(int fruitId, int gameId) {
        this.fruitId = fruitId;
        this.gameId = gameId;
    }


    public int getFruitId() {
        return fruitId;
    }
}
