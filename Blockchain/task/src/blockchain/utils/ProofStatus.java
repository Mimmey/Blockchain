package blockchain.utils;

import lombok.Getter;

@Getter
public enum ProofStatus {
    STAYS("N stays the same"), INCREASES("N was increased to %d"), DECREASES("N was decreased to %d");

    String output;

    ProofStatus(String output) {
        this.output = output;
    }
}
