package io.intino.alexandria.framework.box.helpers;

import io.intino.alexandria.framework.box.model.TimeRange;
import io.intino.alexandria.framework.box.model.TimeScale;

import java.util.Map;

public class Bounds {
    private TimeRange range;
    private Mode mode;
    private Map<TimeScale, Zoom> zooms;

    public TimeRange range() {
        return range;
    }

    public Mode mode() {
        return mode;
    }

    public Map<TimeScale, Zoom> zooms() {
        return zooms;
    }

    public enum Mode {
        FromTheBeginning, ToTheLast;
    }

    public static class Zoom {
        private int min;
        private int max;

        public int min() {
            return min;
        }

        public Zoom min(int min) {
            this.min = min;
            return this;
        }

        public int max() {
            return max;
        }

        public Zoom max(int max) {
            this.max = max;
            return this;
        }
    }
}
