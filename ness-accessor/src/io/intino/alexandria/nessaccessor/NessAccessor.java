package io.intino.alexandria.nessaccessor;

import io.intino.alexandria.nessaccessor.fs.FileEventStore;
import io.intino.alexandria.nessaccessor.fs.FileSetStore;
import io.intino.alexandria.nessaccessor.fs.FileStage;
import io.intino.alexandria.nessaccessor.sessions.EventSession;
import io.intino.alexandria.nessaccessor.sessions.SetSession;

import java.util.function.Function;
import java.util.stream.Stream;

public class NessAccessor {
    private URI uri;

    public NessAccessor(String uri) {
        this.uri = new URI(uri);
    }

    public static SetSession createSetSession(FileStage stage)  {
        return new SetSession(stage.start(Stage.Type.set));
    }

    public static EventSession createEventSession(FileStage stage) {
        return new EventSession(stage.start(Stage.Type.event));
    }

    public void commit(Stage stage) {
        Stream<Stage.Session> sessions = stage.sessions();
        sessions.forEach(null);
        //TODO
    }

    public EventStore eventStore() {
        return uri.isFile() ? new FileEventStore() : null; //TODO
    }

    public SetStore setStore() {
        return uri.isFile() ? new FileSetStore() : null; //TODO
    }

    private class URI {
        private String uri;

        URI(String uri) {
            this.uri = uri;
        }

        boolean isFile() {
            return uri.startsWith("file:");
        }
    }

    public interface SetStore {
        Stream<Tank> tanks();
        Tank tank(String name);

        interface Timetag {
            Timetag next();
        }

        interface Tank {
            String name();
            Tub first();
            Tub last();
            Tub on(Timetag timetag);
            Stream<Tub> tubs();
            Stream<Tub> tubs(Function<Boolean, Tub> filter);
            Stream<Tub> tubs(Timetag from, Timetag to);
            Stream<Tub> tubs(int count);
        }

        interface Tub {
            Timetag timetag();
            Set set(String name);
            Stream<Set> sets();
            Stream<Set> sets(Function<Set, Boolean> filter);
        }

        interface Set {
            String name();
            int size();
            Variable variable(String name);

            void put(long... ids);
            void put(Stream<Long> ids);
            void define(Variable variable);


            Stream<Long> content();
            Stream<Variable> variables();
        }

        class Variable {
            private final String name;
            private final String value;

            public Variable(String name, String value) {
                this.name = name;
                this.value = value;
            }

            public String name() {
                return name;
            }

            public String value() {
                return value;
            }
        }

        static Variable variable(String name, String value) {
            return new Variable(name, value);
        }

    }

    public interface EventStore {
        //TODO
        interface Tank {

        }
    }

    public interface Stage {

        Stream<Session> sessions();

        interface Session {
            Type type();

            //TODO Lo que se necesite para hacer el commit

        }
        enum Type { event, set }
    }


}
