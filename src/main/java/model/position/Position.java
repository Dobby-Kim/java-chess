package model.position;

import static util.File.A;
import static util.File.H;
import static util.Rank.EIGHT;
import static util.Rank.ONE;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import model.direction.Direction;
import util.File;
import util.Rank;

public class Position {
    private static final Set<Position> cache = new HashSet<>();
    private final int file;
    private final int rank;

    private Position(int file, int rank) {
        this.file = file;
        this.rank = rank;
    }

    static {
        for (int file = A.value(); file <= H.value(); file++) {
            initRank(file);
        }
    }

    private static void initRank(int file) {
        for (int rank = ONE.value(); rank <= EIGHT.value(); rank++) {
            cache.add(new Position(file, rank));
        }
    }

    public static Position of(int file, int rank) {
        if (isLegalFileRank(file, rank)) {
            return cache.stream()
                    .filter(position -> position.file == file && position.rank == rank)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("캐싱된 Position 확인이 필요합니다."));
        }
        throw new IllegalArgumentException("존재하지 않는 Position 입니다.");
    }

    public boolean hasAvailableNextPositionToDirection(Direction direction) {
        int movedFile = file + direction.fileDifferential();
        int movedRank = rank + direction.rankDifferential();
        return isLegalFileRank(movedFile, movedRank);
    }

    private static boolean isLegalFileRank(int file, int rank) {
        return File.validate(file) && Rank.validate(rank);
    }

    public static List<Position> inFile(int file) {
        return cache.stream()
                .filter(position -> position.file == file)
                .toList();
    }

    public Position getNextPosition(Direction direction) {
        return Position.of(file + direction.fileDifferential(), rank + direction.rankDifferential());
    }

    public int file() {
        return file;
    }

    public int rank() {
        return rank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Position position)) {
            return false;
        }
        return file == position.file && rank == position.rank;
    }

    @Override
    public int hashCode() {
        return Objects.hash(file, rank);
    }
}
