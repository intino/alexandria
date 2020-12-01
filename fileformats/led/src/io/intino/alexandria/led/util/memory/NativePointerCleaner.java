package io.intino.alexandria.led.util.memory;

import static io.intino.alexandria.led.util.memory.MemoryUtils.NULL;

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
