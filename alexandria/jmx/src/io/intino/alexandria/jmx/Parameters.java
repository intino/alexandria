package io.intino.alexandria.jmx;

import javax.management.DescriptorKey;
import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Parameters {

	@DescriptorKey("Parameters")
	String[] value();
}
