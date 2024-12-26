package io.github.dracosomething.trawakened.ability.skill.ultimate;

import io.github.dracosomething.trawakened.ability.skill.unique.Starkill;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class herrscheroftime {
    public static long time = 0L;

    public static Timer timer;

    public static ScheduledExecutorService service;

    public static float PERCENT = 20.0F;

    public static double millisF = 0.0D;

    public static void create() {
        if (service == null)
            service = Executors.newSingleThreadScheduledExecutor();
        if (timer == null)
            timer = new Timer();
        try {
            timer.schedule(new TimerTask() {
                public void run() {
                    herrscheroftime.service.scheduleAtFixedRate(herrscheroftime::update, 1L, 1L, TimeUnit.MILLISECONDS);
                }
            }, 1L);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void update() {
        float p = PERCENT / 20.0F;
        millisF = p + millisF;
        time = (long)millisF;
    }

    public static void changeAll(float percent) {
        PERCENT = percent;
        System.out.println("PER " + PERCENT);
        System.out.println("add " + percent / 20.0F);
        System.out.println("TickLengthChanged ->" + PERCENT);
    }

}
