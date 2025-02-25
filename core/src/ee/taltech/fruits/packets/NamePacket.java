package ee.taltech.fruits.packets;

public class NamePacket extends Packet {
    private String name;

    /**
     * Make new NamePacket instance.
     *
     * @param name String
     */
    public NamePacket(String name) {
        this.name = name;
    }

    public NamePacket() {
    }

    /**
     * Return name.
     *
     * @return String.
     */
    public String getName() {
        return name;
    }
}
