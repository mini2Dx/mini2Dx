package org.mini2Dx.injection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class as a prototype bean
 * 
 * @see <a href="http://en.wikipedia.org/wiki/Prototype_pattern">Prototype Pattern</a>
 *
 * @author Thomas Cashman
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Prototype {

}
