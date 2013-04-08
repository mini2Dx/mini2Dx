package org.mini2Dx.injection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class as a singleton bean
 *
 * @see <a href="http://en.wikipedia.org/wiki/Singleton_pattern">Singleton Pattern</a>
 *
 * @author Thomas Cashman
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Singleton {

}
