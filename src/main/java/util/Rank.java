package util;

import java.util.Arrays;

public enum Rank {
    ONE('1', 1),
    TWO('2', 2),
    THREE('3', 3),
    FOUR('4', 4),
    FIVE('5', 5),
    SIX('6', 6),
    SEVEN('7', 7),
    EIGHT('8', 8);

    private final char rankSymbol;
    private final int rankValue;

    Rank(char rankSymbol, int rankValue) {
        this.rankSymbol = rankSymbol;
        this.rankValue = rankValue;
    }

    public static Rank from(char inputFileSymbol) {
        return Arrays.stream(Rank.values())
                .filter(rankConverter -> rankConverter.rankSymbol==inputFileSymbol)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("입력한 rank는 올바르지 않은 값입니다."));
    }

    public static boolean validate(int rankValue) {
        return ONE.rankValue <= rankValue && rankValue <= EIGHT.rankValue;
    }

    public int value() {
        return rankValue;
    }
}
