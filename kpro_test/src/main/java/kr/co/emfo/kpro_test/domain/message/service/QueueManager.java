package kr.co.emfo.kpro_test.domain.message.service;

import kr.co.emfo.kpro_test.global.response.code.resultCode.ErrorStatus;
import kr.co.emfo.kpro_test.global.response.exception.handler.ServerHandler;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class QueueManager {

    private final BlockingQueue<Long> messageIdxQueue = new LinkedBlockingQueue<>();

    public void addToQueue(Long idx) {

        try {

            messageIdxQueue.put(idx);
        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
            throw new ServerHandler(ErrorStatus.THREAD_INTERRUPTED);
        }
    }

    public Long getIdxFromQueue() {
        try {

            return messageIdxQueue.take();
        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
            throw new ServerHandler(ErrorStatus.THREAD_INTERRUPTED);
        }
    }

    public boolean isQueueEmpty() {

        return messageIdxQueue.isEmpty();
    }
}
