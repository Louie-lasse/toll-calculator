import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MinimumTollingStrategy implements TollingStrategy {

    @Override
    public int getTollFee(Vehicle vehicle, List<LocalDateTime> dates) {
        int n = dates.size() - 1;
        List<List<Integer>> intervals = getIntervals(dates);
        List<Integer> nodes = new ArrayList<>();
        for (LocalDateTime date : dates) {
            nodes.add(vehicle.getTollFee(date));
        }
        List<List<Integer>> nodeIntervals = getNodeIntervals(intervals, n);
        return opt(0, n, nodes, intervals, nodeIntervals);
    }

    private int opt(int left, int right, List<Integer> nodes, List<List<Integer>> intervals,
            List<List<Integer>> nodeIntervals) {
        if (right < left) {
            return 0;
        }
        if (left < 0 || right > nodes.size() - 1) {
            return 0;
        }
        int top = argMax(nodes, left, right);

        List<Integer> possibleValues = new ArrayList<>();
        for (Integer intervalIndex : nodeIntervals.get(top)) {
            possibleValues.add(
                    opt(left, intervals.get(intervalIndex).getFirst() - 1, nodes, intervals, nodeIntervals) +
                            opt(intervals.get(intervalIndex).getLast() + 1, right, nodes, intervals, nodeIntervals));
        }
        return nodes.get(top) + Collections.min(possibleValues);
    }

    private int argMax(List<Integer> list, int left, int right) {
        int highestIndex = -1;
        int highestValue = -1;
        List<Integer> toIterate = list.subList(left, right + 1);
        for (int i = 0; i < toIterate.size(); i++) {
            if (highestValue < toIterate.get(i)) {
                highestIndex = i;
                highestValue = toIterate.get(i);
            }
        }
        return left + highestIndex;
    }

    private List<List<Integer>> getNodeIntervals(List<List<Integer>> intervals, int n) {
        List<List<Integer>> nodeIntervals = new ArrayList<>();
        int size = intervals.size();
        for (int i = 0; i <= n; i++) {
            List<Integer> nodeInterval = new ArrayList<>();
            int intervalIndex = 0;
            while (intervalIndex < size && !intervals.get(intervalIndex).contains(i)) {
                intervalIndex++;
            }
            while (intervalIndex < size && intervals.get(intervalIndex).contains(i)) {
                nodeInterval.add(intervalIndex);
                intervalIndex++;
            }
            nodeIntervals.add(nodeInterval);
        }
        return nodeIntervals;
    }

    private List<List<Integer>> getIntervals(List<LocalDateTime> dates) {
        List<List<Integer>> intervals = new ArrayList<>();
        boolean hasEnded = false;
        int lastNode = -1;
        for (int a = 0; !hasEnded; a++) {
            List<Integer> interval = new ArrayList<>();
            for (int b = a; true; b++) {
                if (b == dates.size()) {
                    hasEnded = true;
                    break;
                }
                if (!dates.get(b).isBefore(dates.get(a).plusHours(1))) {
                    break;
                }
                interval.add(b);
            }
            if (interval.getLast() > lastNode) {
                intervals.add(interval);
                lastNode = interval.getLast();
            }
        }

        return intervals;
    }
}
