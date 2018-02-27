package selenium.boot.spring.convert;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



/**
 * Annotation that can be used to indicate the format to use when converting a {@link java.time.Duration}.
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 1.0
 */
@Target( ElementType.FIELD )
@Retention( RetentionPolicy.RUNTIME )
@Documented
public @interface DurationFormat
{
    /**
     * The duration format style.
     *
     * @return the duration format style.
     */
    DurationStyle value();
}
