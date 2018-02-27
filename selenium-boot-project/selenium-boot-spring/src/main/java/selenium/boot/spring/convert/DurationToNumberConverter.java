package selenium.boot.spring.convert;


import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.ReflectionUtils;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Set;



/**
 * {@link org.springframework.core.convert.converter.Converter} to convert from a {@link java.time.Duration} to a {@link java.lang.Number}.
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @see DurationFormat
 * @see DurationUnit
 * @since 1.0
 */
final class DurationToNumberConverter implements GenericConverter
{
    @Override
    public Set<ConvertiblePair> getConvertibleTypes()
    {
        return Collections.singleton( new ConvertiblePair( Duration.class, Number.class ) );
    }

    @Override
    public Object convert( @Nullable Object source,
                           @NonNull TypeDescriptor sourceType,
                           @NonNull TypeDescriptor targetType )
    {
        if( source == null )
        {
            return null;
        }
        DurationUnit unit = sourceType.getAnnotation( DurationUnit.class );

        return convert( ( Duration ) source, ( unit == null ? null : unit.value() ), targetType.getObjectType() );
    }

    private Object convert( Duration source, ChronoUnit unit, Class<?> type )
    {
        try
        {
            return type.getConstructor( String.class )
                           .newInstance( String.valueOf( DurationStyle.Unit.fromChronoUnit( unit ).longValue( source ) ) );
        }
        catch( Exception ex )
        {
            ReflectionUtils.rethrowRuntimeException( ex );
            throw new IllegalStateException( ex );
        }
    }
}
