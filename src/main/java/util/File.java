package util;

import java.util.Arrays;

public enum File {
    A('a', 1),
    B('b', 2),
    C('c', 3),
    D('d', 4),
    E('e', 5),
    F('f', 6),
    G('g', 7),
    H('h', 8);

    private final char fileSymbol;
    private final int fileValue;

    File(char fileSymbol, int fileValue) {
        this.fileSymbol = fileSymbol;
        this.fileValue = fileValue;
    }

    public static File from(char inputFileSymbol) {
        return Arrays.stream(File.values())
                .filter(file -> file.fileSymbol == inputFileSymbol)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("입력한 File는 올바르지 않은 값입니다"));
    }

    public static boolean validate(int fileValue) {
        return A.fileValue <= fileValue && fileValue <= H.fileValue;
    }

    public int value() {
        return fileValue;
    }
}
