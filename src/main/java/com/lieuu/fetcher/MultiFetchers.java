package com.lieuu.fetcher;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import com.lieuu.fetcher.exception.FetcherErrorCallback;

public final class MultiFetchers {

    @SafeVarargs
    public final static <T> MultiFetcher<T> getMultiConcurrentFetcher(
            final Fetcher<ExecutorService> executorServiceFetcher, final Fetcher<T>... fetchers) {

        final List<NonBlockingConcurrentFetcher<T>> fetchersWrapped = new ArrayList<NonBlockingConcurrentFetcher<T>>(fetchers.length);

        for (final Fetcher<T> fetcher : fetchers) {
            fetchersWrapped.add(
                new NonBlockingConcurrentFetcher<T>(fetcher, executorServiceFetcher));
        }

        return new BlockingMultiConcurrentFetcher<T>(fetchersWrapped);

    }

    @SafeVarargs
    public final static <T> MultiFetcher<T> getMultiConcurrentFetcher(
            final Fetcher<T>... fetchers) {
        return MultiFetchers.getMultiConcurrentFetcher(Fetchers.getExecutorServiceFetcher(),
            fetchers);
    }

    @SafeVarargs
    public final static <T> MultiFetcher<T> getExpiringMultiConcurrentFetcher(
            final Fetcher<ExecutorService> executorServiceFetcher, final long maxTimeMs,
            final Fetcher<T>... fetchers) {

        final List<NonBlockingConcurrentFetcher<T>> fetchersWrapped = new ArrayList<NonBlockingConcurrentFetcher<T>>(fetchers.length);

        for (final Fetcher<T> fetcher : fetchers) {
            fetchersWrapped.add(
                new NonBlockingConcurrentFetcher<T>(fetcher, executorServiceFetcher));
        }

        return new ExpiringMultiConcurrentFetcher<T>(maxTimeMs, fetchersWrapped);

    }

    @SafeVarargs
    public final static <T> MultiFetcher<T> getExpiringMultiConcurrentFetcher(final long maxTimeMs,
            final Fetcher<T>... fetchers) {
        return MultiFetchers.getExpiringMultiConcurrentFetcher(Fetchers.getExecutorServiceFetcher(),
            maxTimeMs,
            fetchers);
    }

    @SafeVarargs
    public final static <T> MultiFetcher<T> getWaterfallFetcher(final Fetcher<T>... fetchers) {

        final List<CachingFetcher<T>> fetchersWrapped = new ArrayList<CachingFetcher<T>>(fetchers.length);

        for (final Fetcher<T> fetcher : fetchers) {
            fetchersWrapped.add(new CachingFetcher<T>(fetcher));
        }

        return new WaterfallCachingFetcher<T>(fetchersWrapped);

    }

    @SafeVarargs
    public final static <T> MultiFetcher<T> getWaterfallFetcher(final FetcherErrorCallback callback,
            final Fetcher<T>... fetchers) {

        final List<CachingFetcher<T>> fetchersWrapped = new ArrayList<CachingFetcher<T>>(fetchers.length);

        for (final Fetcher<T> fetcher : fetchers) {
            fetchersWrapped.add(new CachingFetcher<T>(fetcher));
        }

        return new WaterfallCachingFetcher<T>(callback, fetchersWrapped);

    }

}
