package io.intino.alexandria.event;

import java.util.Iterator;

public interface EventReader<T extends Event> extends Iterator<T>, AutoCloseable {

}
