package net.ichenglin.kbext.object;

import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ScheduledTask {
    private final ScheduledExecutorService task_scheduler;
    private final ScheduledFuture<?>       task_handle;
    private final Runnable                 task_runnable;

    public ScheduledTask(int task_delay_initial, int task_delay, Runnable task_runnable) {
        this.task_runnable  = task_runnable;
        this.task_scheduler = Executors.newScheduledThreadPool(1);
        this.task_handle    = this.task_scheduler.scheduleAtFixedRate(() -> {
            try {
                this.task_runnable.run();
            } catch (Exception exception) {
                System.err.println("Scheduled Task Exception: " + exception.getMessage());
            }
        }, task_delay_initial, task_delay, TimeUnit.MILLISECONDS);
    }

    public void task_stop() {
        this.task_handle.cancel(true);
        this.task_scheduler.shutdown();
    }
}
