import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Johnny_JayJay (https://www.github.com/JohnnyJayJay)
 */
public class Solution03 {

    public static void main(String[] args) throws IOException {
        Path input = Paths.get(args.length > 0 ? args[0] : "./03/input.txt");

        List<Claim> claims;
        try (Stream<String> lines = Files.lines(input)) {
            claims = lines.map(Claim::parse).collect(Collectors.toList());
        }

        assert !claims.isEmpty();

        int[][] field = new int[1000][1000];
        claims.forEach((claim) -> addClaimToField(claim, field));

        // Task 1
        // sum of all overlap occurrences
        long overlaps = Arrays.stream(field).mapToLong((row) -> Arrays.stream(row).filter((i) -> i >= 2).count()).sum();
        System.out.printf("There are %d overlaps in fabric%n", overlaps);

        // Task 2
        claims.stream().filter((claim) -> hasNoOverlaps(claim, field)).findFirst()
                .ifPresent((claim) -> System.out.printf("Claim %d does not have any overlaps", claim.id));

    }

    private static void addClaimToField(Claim claim, int[][] field) {
        for (int i = claim.leftEdgeDistance; i < claim.width + claim.leftEdgeDistance; i++) {
            for (int j = claim.topEdgeDistance; j < claim.height + claim.topEdgeDistance; j++) {
                field[i][j]++; // increase the number of elves who claim this square inch
            }
        }
    }

    private static boolean hasNoOverlaps(Claim claim, int[][] field) {
        int area = 0;
        for (int i = claim.leftEdgeDistance; i < claim.width + claim.leftEdgeDistance; i++) {
            for (int j = claim.topEdgeDistance; j < claim.height + claim.topEdgeDistance; j++) {
                area += field[i][j];
            }
        }

        // if every square inch in the area was claimed by one elf only, it's equal to the claim's surface
        return area == claim.width * claim.height;
    }

    // data class for information about a claim
    private static class Claim {
        private final int id, leftEdgeDistance, topEdgeDistance, width, height;

        private Claim(int id, int leftEdgeDistance, int topEdgeDistance, int width, int height) {
            this.id = id;
            this.leftEdgeDistance = leftEdgeDistance;
            this.topEdgeDistance = topEdgeDistance;
            this.width = width;
            this.height = height;
        }

        // parse a string of format "#id @ leftedge,topedge: widthxheight"
        public static Claim parse(String identifier) {
            String[] parts = identifier.replaceAll("[#@:]", "").split("\\s+");
            int id = Integer.parseInt(parts[0]);
            String[] distances = parts[1].split(",");
            int leftEdgeDistance = Integer.parseInt(distances[0]);
            int topEdgeDistance = Integer.parseInt(distances[1]);
            String[] dimensions = parts[2].split("x");
            int width = Integer.parseInt(dimensions[0]);
            int height = Integer.parseInt(dimensions[1]);
            return new Claim(id, leftEdgeDistance, topEdgeDistance, width, height);
        }
    }

}
