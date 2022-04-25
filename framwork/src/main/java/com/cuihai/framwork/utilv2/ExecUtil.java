package com.cuihai.framwork.utilv2;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

/*
 *
 * Copyright (C) 2022 NIO Inc
 *
 * Ver   Date        Author    Desc
 *
 * V1.0  2022/04/23  hai.cui  Add for
 *
 */
public class ExecUtil {

    public static final int MODE_MAIN = 1;
    public static final int MODE_IO = 2;
    public static final int MODE_NEW_THREAD = 3;
    public static final int MODE_SINGLE = 4;
    public static final int MODE_COMPUTE = 5;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({MODE_MAIN, MODE_IO, MODE_NEW_THREAD, MODE_SINGLE, MODE_COMPUTE})
    public @interface EXEC_MODE {
    }

    private static Scheduler determineScheduler(@EXEC_MODE int mode) {
        switch (mode) {
            case MODE_IO:
            default:
                return Schedulers.io();
            case MODE_MAIN:
                return AndroidSchedulers.mainThread();
            case MODE_NEW_THREAD:
                return Schedulers.newThread();
            case MODE_SINGLE:
                return Schedulers.single();
            case MODE_COMPUTE:
                return Schedulers.computation();
        }
    }

    public static void execute(@EXEC_MODE int mode, final Runnable command) {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                command.run();
                e.onComplete();
            }
        }).subscribeOn(determineScheduler(mode)).subscribe();
    }

    public static void executeDelayed(@EXEC_MODE int mode, final Runnable command,
                                      long delayMillis) {
        Observable.timer(delayMillis, TimeUnit.MILLISECONDS)
                .observeOn(determineScheduler(mode)).doOnComplete(new Action() {
            @Override
            public void run() throws Exception {
                command.run();
            }
        }).subscribe();
    }

    public static void execute(final Runnable command) {
        execute(MODE_IO, command);
    }

    public static void executeDelayed(final Runnable command, long delayMillis) {
        executeDelayed(MODE_IO, command, delayMillis);
    }

    public static void executeUI(final Runnable command) {
        execute(MODE_MAIN, command);
    }

    public static void executeUIDelayed(final Runnable command, long delayMillis) {
        executeDelayed(MODE_MAIN, command, delayMillis);
    }
}