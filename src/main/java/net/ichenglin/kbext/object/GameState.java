package net.ichenglin.kbext.object;

public class GameState {
    private final int round_health;
    private final int round_wave;
    private final int round_timer;

    public GameState(int round_health, int round_wave, int round_timer) {
        this.round_health = round_health;
        this.round_wave   = round_wave;
        this.round_timer  = round_timer;
    }

    public int get_health() {
        return this.round_health;
    }

    public int get_wave() {
        return this.round_wave;
    }

    public int get_timer() {
        return this.round_timer;
    }
}
