package selenium.boot.spring.convert;


import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

import java.time.Duration;
import java.util.Collections;
import java.util.Set;



/**
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 2.0
 */
final class NumberToDurationConverter implements GenericConverter
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    private final StringToDurationConverter delegate = new StringToDurationConverter();

    //endregion

    @Override
    public Set<ConvertiblePair> getConvertibleTypes()
    {
        return Collections.singleton( new ConvertiblePair( Number.class, Duration.class ) );
    }

    @Override
    public Object convert( Object source, TypeDescriptor sourceType,
                           TypeDescriptor targetType )
    {
        return this.delegate.convert( source == null ? null : source.toString(),
                                      TypeDescriptor.valueOf( String.class ), targetType
        );
    }
}
