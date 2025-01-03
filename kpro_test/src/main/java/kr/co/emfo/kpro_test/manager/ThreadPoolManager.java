package kr.co.emfo.kpro_test.manager;

import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class ThreadPoolManager {

    private ExecutorService logPool;

    public ExecutorService getLogPool(int threadPoolNum) {
        if (logPool == null || logPool.isShutdown() || logPool.isTerminated()) {
            logPool = Executors.newFixedThreadPool(threadPoolNum);
        }

        return logPool;
    }

    public void shutdown() {
        if (logPool != null) {
            logPool.shutdown();
        }
    }
}
