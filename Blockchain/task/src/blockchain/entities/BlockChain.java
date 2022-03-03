package blockchain.entities;

import blockchain.cryptoAlgos.Sha256;
import blockchain.utils.ProofStatus;

import java.util.*;

public class BlockChain implements BlockChainInterface {
    private static final long MIN_VALID_TIME = 0_000;
    private static final long MAX_VALID_TIME = 500;
    private static final int INIT_NUMBER_OF_ZEROS = 0;
    private static final List<Message> messagesBuffer = new ArrayList<>();
    // private static final List<MessageCrypto> messagesBuffer = new ArrayList<>();
    private final List<Block> chain = new LinkedList<>();
    private int numberOfZeros;
    final Object lockMessages = new Object();

    public BlockChain() {
        numberOfZeros = INIT_NUMBER_OF_ZEROS;
    }

    @Override
    public void addMessage(Message message) {
        synchronized (lockMessages) {
            messagesBuffer.add(message);
        }
    }

    @Override
    public synchronized int getSize() {
        return chain.size();
    }

    @Override
    public synchronized boolean validate() {
        synchronized (lockMessages) {
            Block current = chain.get(chain.size() - 1);

            if (chain.size() > 1) {
                Block prev = chain.get(chain.size() - 2);

                if (!current.hashPrev.equals(prev.hash) || !current.isHashValid(current.hash, numberOfZeros)) {
                    return false;
                }
            }

            long seconds = current.timestampAfterSearch - current.timestampBeforeSearch;
            if (seconds < MIN_VALID_TIME) {
                current.numberOfZerosStatus = ProofStatus.INCREASES;
                numberOfZeros++;
                current.currentNumberOfZeros = numberOfZeros;
            } else if (seconds > MAX_VALID_TIME) {
                current.numberOfZerosStatus = ProofStatus.DECREASES;
                numberOfZeros--;
                current.currentNumberOfZeros = numberOfZeros;
            } else {
                current.numberOfZerosStatus = ProofStatus.STAYS;
            }
            return true;
        }
    }

    @Override
     public synchronized boolean tryAddBlock() {
        synchronized (lockMessages) {
            Optional<Block> prev = chain.isEmpty() ? Optional.empty() : Optional.of(chain.get(chain.size() - 1));
            chain.add(new Block(prev, numberOfZeros));
            int index = chain.size() - 1;

            if (!validate()) {
                chain.remove(index);
                return false;
            }

            Block block = chain.get(index);
            block.miner = Thread.currentThread().getName();

            if (chain.size() > 1) {
                block.messages.addAll(messagesBuffer);
                messagesBuffer.clear();
            }

            chain.set(index, block);

            if (chain.size() > 5) {
                chain.remove(index);
                Thread.currentThread().interrupt();
            } else {
                //block.print();
            }

            return true;
        }
    }

    @Override
    public void print() {
        for (Block block : chain) {
            block.print();
            System.out.println();
        }
    }

    static class Block {
        private static final String INIT_HASH = "0";
        private long id;
        private final long timestampBeforeSearch;
        private String hashPrev;
        private String hash;
        private long proof;
        private final long timestampAfterSearch;
        private ProofStatus numberOfZerosStatus;
        private int currentNumberOfZeros;
        private String miner;
        private final List<Message> messages = new ArrayList<>();

        public Block(Optional<BlockChain.Block> prev, int numberOfZeros) {
            prev.ifPresentOrElse(c -> {
                this.id = c.id + 1;
                this.hashPrev = c.hash;
            }, () -> {
                id = 1;
                this.hashPrev = INIT_HASH;
            });

            this.timestampBeforeSearch = new Date().getTime();
            this.proof = 0;
            this.hash = "";

            do {
                proof++;
                this.hash = hash(hashPrev + id + timestampBeforeSearch + proof);
            } while (!isHashValid(hash, numberOfZeros));

            this.timestampAfterSearch = new Date().getTime();
        }

        private boolean isHashValid(String hash, int numberOfZeros) {
            return hash.startsWith("0".repeat(numberOfZeros));
        }

        public String hash(String fieldString) {
            return new Sha256().hash(fieldString);
        }

        public void print() {
            System.out.println("Block: ");
            System.out.println("Created by miner # " + this.miner);
            System.out.println(this.miner + "gets 100 VC");
            System.out.println("Id: " + this.id);
            System.out.println("Timestamp: " + this.timestampBeforeSearch);
            System.out.println("Magic number: " + this.proof);
            System.out.println("Hash of the previous block: \n" + this.hashPrev);
            System.out.println("Hash of the block: \n" + this.hash);
            System.out.print("Block data: ");

            if (this.messages.isEmpty()) {
                System.out.println("no messages");
            } else {
                System.out.println();
                for (Message message : messages) {
                    System.out.println(message.toString());
                }
            }

            System.out.println(String.format("Block was generating for %d seconds", (this.timestampAfterSearch - this.timestampBeforeSearch) / 1000));

            if (this.numberOfZerosStatus == ProofStatus.STAYS) {
                System.out.println(ProofStatus.STAYS.getOutput());
            } else {
                System.out.println(String.format(this.numberOfZerosStatus.getOutput(), this.currentNumberOfZeros));
            }
        }
    }
}
