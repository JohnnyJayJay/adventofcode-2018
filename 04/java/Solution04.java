import java.beans.beancontext.BeanContextMembershipEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Johnny_JayJay (https://www.github.com/JohnnyJayJay)
 */
public class Solution04 {


    public static void main(String[] args) throws IOException {
        Path input = Paths.get(args.length > 0 ? args[0] : "./04/input.txt");
        List<InputEntry> entries;
        try (Stream<String> lines = Files.lines(input)) {
            entries = lines.map(InputEntry::parse)
                    .sorted(Comparator.comparing(entry -> entry.localDateTime))
                    .collect(Collectors.toList());
        }

        Pattern guardIdPattern = Pattern.compile("[\\d]+");
        Map<Integer, Guard> guards = new HashMap<>();
        uniqueGuardShifts(entries).map((entry) -> entry.line)
                .map(guardIdPattern::matcher)
                .filter(Matcher::find)
                .map(Matcher::group)
                .map(Integer::parseInt)
                .map(Guard::new)
                .forEach((guard) -> guards.put(guard.id, guard));

        Iterator<InputEntry> entryIterator = entries.iterator();
        assert entryIterator.hasNext();
        InputEntry currentEntry = entryIterator.next();

        while (entryIterator.hasNext()) {
            if (currentEntry.type == EntryType.SHIFT_BEGIN) {
                Matcher idMatcher = guardIdPattern.matcher(currentEntry.line);
                assert idMatcher.find();
                int guardId = Integer.parseInt(idMatcher.group());
                ScheduleBuilder builder = new ScheduleBuilder();
                while (entryIterator.hasNext() && (currentEntry = entryIterator.next()).type != EntryType.SHIFT_BEGIN) {
                    builder.add(currentEntry);
                }
                long schedule = builder.build();
                guards.get(guardId).add(schedule);
            } else {
                currentEntry = entryIterator.next();
            }
        }

        guards.values().stream().min(Comparator.comparingInt((guard) -> minutesAsleep(guard.sleepMask()))).ifPresent((guard) -> {
            System.out.printf("Guard #%d spends %d / 59 minutes asleep%n", guard.id, minutesAsleep(guard.sleepMask()));
            System.out.printf("This guard is most likely to be asleep at minute %d", lastSleepMinute(guard.mostLikelyToBeAsleepMask()));
        });
    }

    private static int lastSleepMinute(long schedule) {
        int count;
        for (count = 59; ; count--) {
            if (schedule % 2 == 1)
                break;
            schedule <<= 1;
        }
        return count;
    }

    private static int minutesAsleep(long schedule) {
        int count;
        for (count = 0; schedule != 0; count++)
            schedule &= schedule - 1;

        return count;
    }

    private static Stream<InputEntry> uniqueGuardShifts(List<InputEntry> entries) {
        return entries.stream()
                .filter((entry) -> EntryType.SHIFT_BEGIN == entry.type)
                .distinct();
    }

    private static class Guard {
        private final List<Long> schedules;
        private final int id;

        private Guard(int id) {
            this.id = id;
            this.schedules = new ArrayList<>();
        }

        public void add(long schedule) {
            schedules.add(schedule);
        }

        public long sleepMask() {
            checkNotEmpty();

            long sleepMask = schedules.get(0);
            for (int i = 1; i < schedules.size(); i++) {
                long schedule = schedules.get(i);
                sleepMask |= schedule;
            }

            return sleepMask;
        }

        public long mostLikelyToBeAsleepMask() {
            checkNotEmpty();

            long schedule = schedules.get(0);
            long maxSleepMask = schedule;
            for (int i = 1; i < schedules.size() && (maxSleepMask & (schedule = schedules.get(i))) != 0; i++) {
                maxSleepMask &= schedule;
            }

            return maxSleepMask;
        }

        private void checkNotEmpty() {
            if (schedules.isEmpty())
                throw new IllegalStateException("Schedules must not be empty");
        }
    }

    private static class ScheduleBuilder {
        private long schedule = 0;
        private boolean awake = true;
        private int lastEventAsMinute = 0;

        public long build() {
            this.append(59 - lastEventAsMinute);
            return schedule;
        }

        public void add(InputEntry entry) {
            int minute = entry.localDateTime.getMinute();
            switch (entry.type) {
                case FALL_ASLEEP:
                    awake = false;
                    break;
                case WAKE_UP:
                    awake = true;
                    break;
                default:
                    throw new IllegalArgumentException("Must not be shift begin");
            }
            this.append(minute - lastEventAsMinute);
            lastEventAsMinute = minute;
        }

        private void append(int minutes) {
            if (awake) {
                schedule <<= (minutes);
            } else {
                for (int i = 0; i < minutes; i++) {
                    schedule++;
                    schedule <<= 1;
                }
            }
        }

    }

    private static class InputEntry {
        private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        private final String line;
        private final LocalDateTime localDateTime;
        private final EntryType type;

        private InputEntry(String line, LocalDateTime localDateTime) {
            this.line = line;
            this.localDateTime = localDateTime;

            switch (line) {
                case "falls asleep":
                    type = EntryType.FALL_ASLEEP;
                    break;
                case "wakes up":
                    type = EntryType.WAKE_UP;
                    break;
                default:
                    type = EntryType.SHIFT_BEGIN;
                    break;
            }
        }

        public static InputEntry parse(String line) {
            String[] parts = line.split("] ");
            assert parts.length == 2;
            LocalDateTime localDateTime = LocalDateTime.parse(parts[0].substring(1), formatter); // FIXME: 07.12.2018 
            return new InputEntry(parts[1], localDateTime);
        }
    }

    private enum EntryType {
        SHIFT_BEGIN, FALL_ASLEEP, WAKE_UP
    }

}
