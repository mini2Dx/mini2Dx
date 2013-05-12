package org.mini2Dx.injection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class as a {@link <a href="http://en.wikipedia.org/wiki/Singleton_pattern">singleton</a>} bean
 *
 * @author Thomas Cashman
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Singleton {

}
