package selenium.boot.spring.support.jackson;


import com.fasterxml.jackson.annotation.JacksonAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



/**
 * Marker annotation which indicates that the annotated case class should be
 * serialized and deserialized using {@code snake_case} JSON field names instead
 * of {@code camelCase} field names
 * source: io.dropwizard.jackson;
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 1.0
 */
@Target( ElementType.TYPE)
@Retention( RetentionPolicy.RUNTIME)
@JacksonAnnotation
public @interface JsonSnakeCase
{

}
