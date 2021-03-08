package io.intino.test;

import io.intino.alexandria.led.Schema;
import io.intino.alexandria.led.buffers.store.ByteStore;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

public class StaticProperties {

    @Test
    public void schemaSubclassAccessStaticPropertiesOfBaseClass() {
        Assert.assertEquals(A.SIZE, Schema.sizeOf(B.class));
    }

    static class A extends Schema {

        public static final int SIZE = 8;

        public A(ByteStore store) {
            super(store);
        }

        @Override
        public long id() {
            return 0;
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public UUID serialUUID() {
            return null;
        }
    }

    static class B extends A {

        public B(ByteStore store) {
            super(store);
        }
    }

}
