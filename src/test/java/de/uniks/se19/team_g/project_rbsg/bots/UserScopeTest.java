package de.uniks.se19.team_g.project_rbsg.bots;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.Assert.*;

public class UserScopeTest {

    @Ignore
    @Test
    public void test() throws ExecutionException, InterruptedException {

        UserScope userScope = new UserScope();

        userScope.get("key", () -> "main");
        System.out.println(Thread.currentThread().getName());
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        ThreadPoolTaskExecutor executor2 = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setThreadNamePrefix("pool 1-");
        executor2.setThreadNamePrefix("pool 2-");
        executor.initialize();
        executor2.initialize();

        Runnable runnable = () -> {
            String name = Thread.currentThread().getName();
            System.out.println(userScope.get("key", () -> name) + " in " + name);
        };

        executor.submit(() -> {
            UserContextHolder.clearContext();
            runnable.run();
        }).get();

        executor.submit(() -> {
            runnable.run();
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        executor.submit(() -> {
            runnable.run();
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread.sleep(50);

        executor2.submit(runnable).get();

        runnable.run();

        userScope.get("key", () -> "updated main");

        executor.submit(() -> {
            runnable.run();
        }).get();

        CompletableFuture.runAsync(runnable).get();
    }

}