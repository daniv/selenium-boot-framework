package selenium.boot.spring.convert;


import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.lang.NonNull;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Set;



/**
 * {@link org.springframework.core.convert.converter.Converter} to convert from a {@link java.time.Duration} to a {@link java.lang.String}.
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 1.0
 */
final class DurationToStringConverter implements GenericConverter
{
    @Override
    public Set<ConvertiblePair> getConvertibleTypes()
    {
        return Collections.singleton( new ConvertiblePair( Duration.class, String.class ) );
    }

    @Override
    public Object convert( Object source,
                           @NonNull TypeDescriptor sourceType,
                           @NonNull TypeDescriptor targetType )
    {
        if( source == null )
        {
            return null;
        }
        DurationFormat format = sourceType.getAnnotation( DurationFormat.class );
        DurationUnit unit = sourceType.getAnnotation( DurationUnit.class );
        return convert( ( Duration ) source, ( format == null ? null : format.value() ),
                        ( unit == null ? null : unit.value() )
        );
    }

    private String convert( Duration source, DurationStyle style, ChronoUnit unit )
    {
        style = ( style != null ? style : DurationStyle.ISO8601 );
        return style.print( source, unit );
    }
}
