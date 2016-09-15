package teseo.rules;

import tara.lang.model.Node;
import tara.lang.model.rules.composition.NodeRule;

import java.util.List;

public class SingleNamed implements NodeRule {

    public int min() {
        return 0;
    }

    public int max() {
        return 1;
    }

    public boolean accept(List<Node> nodes) {
        for (Node node : nodes)
            if (node.isAnonymous()) return false;
        return true;
    }

    public String errorMessage() {
        return "This element must have name";
    }

}
