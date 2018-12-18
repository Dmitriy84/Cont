package com.continuum.cucumber.utils;

import lombok.experimental.UtilityClass;
import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.RetryPolicy;
import net.jodah.failsafe.function.CheckedRunnable;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@UtilityClass
public class CommonUtils {

    private static Random random = new Random();

    public static int getRandomInt(int... bound) {
        if (bound.length > 0)
            return random.nextInt(bound[0]);
        return random.nextInt();
    }

    @SuppressWarnings("unchecked")
    public void retryOnException(CheckedRunnable runnable, int retries, int pauseBetweenAttempts, Class<? extends Throwable>... retryOnExceptionType) {
        RetryPolicy retryPolicy = new RetryPolicy()
                .retryOn(retryOnExceptionType)
                .withDelay(pauseBetweenAttempts, TimeUnit.MILLISECONDS)
                .withMaxRetries(retries);
        Failsafe.with(retryPolicy).run(runnable);
    }
}
