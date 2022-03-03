package blockchain;

import blockchain.entities.BlockChain;
import blockchain.entities.Message;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Thread.sleep;

public class Main {
    private static final int MINER_NUMBER = 4;
    private static final int SENDER_NUMBER = 1;
    private static final int BLOCKCHAIN_SIZE = 15;

    public static void main(String[] args) throws InterruptedException {
        BlockChain blockChain = new BlockChain();
        ExecutorService minersExecutor = Executors.newFixedThreadPool(MINER_NUMBER);
        ExecutorService sendersExecutor = Executors.newFixedThreadPool(SENDER_NUMBER);

        while (true) {
            sendersExecutor.submit(() -> {
                try {
                    blockChain.addMessage(new Message("Hello!"));
                    /*
                    try {
                        blockChain.addMessage(new Message("Hello!", Thread.currentThread().getName()));
                    } catch (NullPointerException e) {
                        Message.addPath(Thread.currentThread().getName(), new GenerateKeys(Message.getKeyLength()).generate());
                        blockChain.addMessage(new Message("Hello!", Thread.currentThread().getName()));
                    }*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (blockChain.getSize() >= BLOCKCHAIN_SIZE) {
                    Thread.currentThread().interrupt();
                }
                try {
                    sleep(2000);
                } catch (InterruptedException e) {

                }
            });

            minersExecutor.submit(blockChain::tryAddBlock);

            if (blockChain.getSize() >= BLOCKCHAIN_SIZE) {
                minersExecutor.shutdownNow();
                sendersExecutor.shutdownNow();
                break;
            }
        }

        blockChain.print();
    }
}
