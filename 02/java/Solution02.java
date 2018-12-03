import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author Johnny_JayJay (https://www.github.com/JohnnyJayJay)
 */
public class Solution02 {

    public static void main(String[] args) throws IOException {
        Path input = Paths.get(args.length > 0 ? args[0] : "./02/input.txt");
        List<String> ids = Files.readAllLines(input);

        assert !ids.isEmpty();

        // Task 1
        int twiceAmount = 0;
        int threeTimesAmount = 0;

        for (String id : ids) {
            char[] characters = id.toCharArray();
            // only check for one occurrence each
            boolean twiceChecked = false;
            boolean threeTimesChecked = false;
            for (char character : characters) {
                if (twiceChecked && threeTimesChecked) {
                    break;
                } else if (!twiceChecked && frequency(character, characters) == 2) {
                    twiceAmount++;
                    twiceChecked = true;
                } else if (!threeTimesChecked && frequency(character, characters) == 3) {
                    threeTimesAmount++;
                    threeTimesChecked = true;
                }
            }

        }

        int checksum = twiceAmount * threeTimesAmount;
        System.out.printf("Checksum: %d * %d = %d%n", twiceAmount, threeTimesAmount, checksum);

        // Task 2
        String correctId1 = null;
        String correctId2 = null;

        search:
        for (String currentId : ids) {
            for (String otherId : ids) {
                if (differences(currentId, otherId) == 1) {
                    if (correctId1 == null)
                        correctId1 = currentId;
                    else
                        correctId2 = currentId;

                    if (correctId2 != null) // if both ids found
                        break search;

                    break;
                }
            }
        }

        assert correctId1 != null && correctId2 != null;

        // calculate the position both id's difference is at
        int differencePosition = 0;
        for (int i = 0; i < correctId1.length(); i++) {
            if (correctId1.charAt(i) != correctId2.charAt(i)) {
                differencePosition = i;
                break;
            }
        }

        // concatenate the part before and after the difference; no replace usage to avoid wrong replacements
        String commonString = correctId1.substring(0, differencePosition) + correctId1.substring(differencePosition + 1);
        System.out.printf("Common characters: %s", commonString);
    }

    // how many character differences do 2 strings have
    private static int differences(String s1, String s2) {
        int differences = 0;

        assert s1.length() == s2.length();

        for (int i = 0; i < s1.length(); i++) {
            if (s1.charAt(i) != s2.charAt(i))
                differences++;
        }

        return differences;
    }

    // how often does a char array contain a specific char
    private static int frequency(char c, char[] chars) {
        int count = 0;
        for (char element : chars) {
            if (element == c)
                count++;
        }
        return count;
    }

}
