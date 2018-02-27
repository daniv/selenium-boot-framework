package selenium.boot.spring.convert;


import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.lang.NonNull;
import org.springframework.util.ObjectUtils;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Set;



/**
 * {@link org.springframework.core.convert.converter.Converter} to convert from a {@link String}
 * to a {@link java.time.Duration}.
 * Supports {@link java.time.Duration#parse(CharSequence)} as well a more readable {@code 10s} form.
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @see DurationFormat
 * @see DurationUnit
 * @since 1.0
 */
final class StringToDurationConverter implements GenericConverter
{
    @Override
    public Set<ConvertiblePair> getConvertibleTypes()
    {
        return Collections.singleton( new ConvertiblePair( String.class, Duration.class ) );
    }

    @Override
    public Object convert( Object source, @NonNull TypeDescriptor sourceType, @NonNull TypeDescriptor targetType )
    {
        if( ObjectUtils.isEmpty( source ) )
        {
            return null;
        }
        DurationFormat format = targetType.getAnnotation( DurationFormat.class );
        DurationUnit unit = targetType.getAnnotation( DurationUnit.class );
        return convert( source.toString(), ( format == null ? null : format.value() ),
                        ( unit == null ? null : unit.value() ) );
    }

    private Duration convert( String source, DurationStyle style, ChronoUnit unit )
    {
        style = ( style != null ? style : DurationStyle.detect( source ) );
        return style.parse( source, unit );
    }
}
