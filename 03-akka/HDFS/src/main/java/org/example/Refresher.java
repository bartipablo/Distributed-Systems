package org.example;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.javadsl.TimerScheduler;

import java.time.Duration;

public class Refresher<T> extends AbstractBehavior<Object> {

    private static final Object TICK_KEY = new Object();

    private static final class FirstTick {}

    private static final class Tick {}

    private final int time;
    private final ActorRef<T> actorRef;
    private final T refreshMessage;
    private final TimerScheduler<Object> timers;

    private Refresher(ActorContext<Object> context, TimerScheduler<Object> timers, int time, ActorRef<T> actorRef, T refreshMessage) {
        super(context);
        this.timers = timers;
        this.time = time;
        this.actorRef = actorRef;
        this.refreshMessage = refreshMessage;
        timers.startSingleTimer(TICK_KEY, new FirstTick(), Duration.ofMillis(time));
    }


    public static <T> Behavior<Object> create(int time, ActorRef<T> actorRef, T refreshMessage) {
        return Behaviors.setup(context -> Behaviors.withTimers(timers -> new Refresher<>(context, timers, time, actorRef, refreshMessage)));
    }


    @Override
    public Receive<Object> createReceive() {
        return newReceiveBuilder()
                .onMessage(FirstTick.class, message -> {
                    timers.startTimerWithFixedDelay(TICK_KEY, new Tick(), Duration.ofSeconds(time));
                    return Behaviors.same();
                })
                .onMessage(Tick.class, message -> {
                    actorRef.tell(refreshMessage);
                    return Behaviors.same();
                })
                .build();
    }

}
