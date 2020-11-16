package io.intino.alexandria.led.util;

import static io.intino.alexandria.led.util.MemoryUtils.NULL;

public final class NativePointerCleaner implements Runnable {

    private final ModifiableMemoryAddress address;

    public NativePointerCleaner(ModifiableMemoryAddress address) {
        this.address = address;
    }

    @Override
    public void run() {
        MemoryUtils.free(address.get());
        address.set(NULL);
    }
}
