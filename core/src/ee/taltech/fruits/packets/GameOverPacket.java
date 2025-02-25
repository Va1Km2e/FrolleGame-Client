package ee.taltech.fruits.packets;
import java.util.Map;

public class GameOverPacket {
    private Map<String, Integer> scores;
    public GameOverPacket() { }

    public GameOverPacket(Map<String, Integer> scores) {
        this.scores = scores;
    }

    public Map<String, Integer> getScores() {
        return scores;
    }
}
