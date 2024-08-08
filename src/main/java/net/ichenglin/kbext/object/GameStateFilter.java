package net.ichenglin.kbext.object;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.LinkedList;

public class GameStateFilter {

    private final LinkedList<GameStateInstant> filter_cache;
    private final int                          filter_volume;

    public GameStateFilter(int filter_volume) {
        if (filter_volume <= 0) throw new IllegalArgumentException("Filter volume must be greater than 0");
        this.filter_cache  = new LinkedList<GameStateInstant>();
        this.filter_volume = filter_volume;
    }

    public void state_add(GameState state_data) {
        while (this.filter_cache.size() >= filter_volume) this.filter_cache.remove();
        this.filter_cache.add(new GameStateInstant(state_data, Instant.now()));
    }

    public GameState state_forecast(Instant instant_future) {
        Instant   instant_begin   = this.filter_cache.getFirst().get_instant();
        Instant   instant_end     = this.filter_cache.getLast() .get_instant();
        int[]     instant_minutes = this.filter_cache.stream().mapToInt((instant_state) -> instant_state.get_data().get_timer() / 60).toArray();
        int[]     instant_seconds = this.filter_cache.stream().mapToInt((instant_state) -> instant_state.get_data().get_timer() % 60).toArray();
        Duration  median_offset   = Duration.between(instant_begin, instant_end).dividedBy(2);
        Duration  future_offset   = Duration.between(instant_begin, instant_future);
        double    median_timer    = (int) ((GameStateFilter.list_median(instant_minutes) * 60) + GameStateFilter.list_median(instant_seconds));
        int       future_timer    = (int) Math.round(median_timer - future_offset.minus(median_offset).getSeconds());
        GameState last_state      = this.filter_cache.getLast().get_data();
        return new GameState(last_state.get_health(), last_state.get_wave(), future_timer);
    }

    private static double list_median(int[] list_data) {
        int   list_data_length = list_data.length;
        int[] list_data_cloned = list_data.clone();
        Arrays.sort(list_data_cloned);
        if ((list_data_length % 2) == 1) return list_data_cloned[(list_data_length / 2)];
        int list_median_front = list_data_cloned[(list_data_length / 2) - 1];
        int list_median_back  = list_data_cloned[(list_data_length / 2)];
        return ((list_median_front + list_median_back) / 2f);
    }
}

class GameStateInstant {
    private final GameState state_data;
    private final Instant   state_instant;

    public GameStateInstant(GameState state_data, Instant state_instant) {
        this.state_data    = state_data;
        this.state_instant = state_instant;
    }

    public GameState get_data() {
        return this.state_data;
    }

    public Instant get_instant() {
        return this.state_instant;
    }
}