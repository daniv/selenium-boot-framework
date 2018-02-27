package selenium.boot.spring.convert;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.temporal.ChronoUnit;



/**
 * Annotation that can be used to change the default unit used when converting a
 * {@link DurationFormat}.
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 1.0
 */
@Target( ElementType.FIELD)
@Retention( RetentionPolicy.RUNTIME)
@Documented
public @interface DurationUnit
{
    /**
     * The duration unit to use if one is not specified.
     * @return the duration unit
     */
    ChronoUnit value();
}
