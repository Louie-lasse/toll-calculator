import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MinimumTollingStrategy implements TollingStrategy {

    @Override
    public int getTollFee(Vehicle vehicle, List<LocalDateTime> dates) {
        // throw new UnsupportedOperationException("Unimplemented method; not suitable
        // for production");
        List<Node> nodes = new ArrayList<>();
        for (LocalDateTime date : dates) {
            int toll = vehicle.getTollFee(date);
            if (toll != 0) {
                nodes.add(new Node(date, toll));
            }
        }
        List<NodeInterval> intervals = getIntervals(nodes);
        return minimumValue(nodes, intervals);
    }

    private int minimumValue(List<Node> nodes, List<NodeInterval> intervals) {
        Map<Node, Integer> nodeIndex = new HashMap<>();
        for (int i = 0; i < nodes.size(); i++) {
            nodeIndex.put(nodes.get(i), i);
        }
        
        List<IntervalRange> ranges = new ArrayList<>();
        for (NodeInterval interval : intervals) {
            List<Node> ns = interval.nodes;
            
            int left = nodeIndex.get(ns.getFirst());
            int right = nodeIndex.get(ns.getLast());
            
            ranges.add(new IntervalRange(left, right));
        }

        List<List<Integer>> intervalsForNode = new ArrayList<>();
        for (int i = 0; i < nodes.size(); i++) {
            intervalsForNode.add(new ArrayList<>());
        }
        for (int intervalIdx = 0; intervalIdx < intervals.size(); intervalIdx++) {
            for (Node node : intervals.get(intervalIdx).nodes) {
                intervalsForNode.get(nodeIndex.get(node))
                        .add(intervalIdx);
            }
        }

        int[][] store = buildStore(nodes);

        return 0;
    }
    
    private int[][] buildStore(List<MinimumTollingStrategy.Node> nodes) {
        int n = nodes.size();
        int log = 32 - Integer.numberOfLeadingZeros(n);
        int[][] st = new int[log][n];
        for (int i = 0; i < n; i++) {
            st[0][i] = i;
        }
        for (int k = 1; k < log; k++) {
            int len = 1 << k;
            int half = len >> 1;
        
            for (int i = 0; i + len <= n; i++) {
        
                int a = st[k - 1][i];
                int b = st[k - 1][i + half];
        
                st[k][i] =
                        nodes.get(a).value >= nodes.get(b).value
                                ? a
                                : b;
            }
        }
        return st;
    }

    private List<NodeInterval> getIntervals(List<Node> nodes) {
        List<NodeInterval> intervals = new ArrayList<>();
        boolean hasEnded = false;
        List<Node> lastInterval = new ArrayList<>();
        for (int i = 0; i < nodes.size(); i++) {
            if (hasEnded) {
                break;
            }
            List<Node> interval = new ArrayList<>();
            Node nodeA = nodes.get(i);
            for (int j = i; true; j++) {
                if (j == nodes.size()) {
                    hasEnded = true;
                    break;
                }
                Node nodeB = nodes.get(j);
                if (!nodeB.date.isBefore(nodeA.date.plusHours(1))) {
                    break;
                }
                interval.add(nodeB);
            }
            if (!lastInterval.contains(interval.getLast())) {
                intervals.add(new NodeInterval(interval));
                lastInterval = interval;
            }
            if (hasEnded) {
                break;
            }
        }
        return intervals;
    }
    
    private class Node {
        LocalDateTime date;
        int value;
        
        public Node(LocalDateTime date, int value) {
            this.date = date;
            this.value = value;
        }
    }
    
    private record IntervalRange(int left, int right) {}
    private class NodeInterval {
        List<Node> nodes;

        public NodeInterval(List<Node> nodes) {
            this.nodes = nodes;
        }

    }

}