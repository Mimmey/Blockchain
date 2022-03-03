package blockchain.entities;

public interface BlockChainInterface {
    boolean tryAddBlock();
    boolean validate();
    void print();
    int getSize();
    void addMessage(Message message);
    // void addMessage(MessageCrypto message);
}
