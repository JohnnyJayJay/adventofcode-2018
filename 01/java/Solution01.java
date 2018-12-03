import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Johnny_JayJay (https://www.github.com/JohnnyJayJay)
 */
public class Solution01 {

    public static void main(String[] args) throws IOException {
        Path input = Paths.get(args.length > 0 ? args[0] : "./01/input.txt"); // get the file
        List<Integer> frequencyChanges;
        try (Stream<String> lines = Files.lines(input)) {
            frequencyChanges = lines.map(Integer::parseInt).collect(Collectors.toList());
        }

        assert !frequencyChanges.isEmpty();

        // Task 1
        int resultFrequency = frequencyChanges.stream().mapToInt(i -> i).sum();
        System.out.printf("Result frequency: %d%n", resultFrequency);

        // Task 2
        Set<Integer> checkedFrequencies = new HashSet<>();
        Iterator<Integer> changeIterator = frequencyChanges.iterator();

        int currentFrequency;

        for (currentFrequency = 0; checkedFrequencies.add(currentFrequency); currentFrequency += changeIterator.next()) {
            if (!changeIterator.hasNext())
                changeIterator = frequencyChanges.iterator();
        }

        System.out.printf("Double frequency: %d", currentFrequency);
    }
}
