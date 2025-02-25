package ee.taltech.fruits.packets;

import java.util.ArrayList;
import java.util.List;

public class UpdatedInventoryPacket extends Packet {
    private List<Integer> inventory;

    public UpdatedInventoryPacket(ArrayList<Integer> inventory) {
        this.inventory = inventory;
    }

    public UpdatedInventoryPacket() { }

    public List<Integer> getInventory() {
        return inventory;
    }
}
