package selenium.boot.spring.convert;


import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;



/**
 * Converts an array to a delimited String.
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @see org.springframework.core.convert.ConversionService
 * @see CollectionToDelimitedStringConverter
 * @see org.springframework.core.convert.TypeDescriptor
 * @since 1.0
 */
final class ArrayToDelimitedStringConverter implements ConditionalGenericConverter
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    private final CollectionToDelimitedStringConverter delegate;

    ArrayToDelimitedStringConverter( ConversionService conversionService )
    {
        this.delegate = new CollectionToDelimitedStringConverter( conversionService );
    }

    //endregion

    @Override
    public Set<ConvertiblePair> getConvertibleTypes()
    {
        return Collections.singleton( new ConvertiblePair( Object[].class, String.class ) );
    }

    @Override
    public boolean matches( @NonNull TypeDescriptor sourceType, @NonNull TypeDescriptor targetType )
    {
        return this.delegate.matches( sourceType, targetType );
    }

    @Override
    @Nullable
    public Object convert( @Nullable Object source, @NonNull TypeDescriptor sourceType, @NonNull TypeDescriptor targetType )
    {
        List<Object> list = Arrays.asList( ObjectUtils.toObjectArray( source ) );
        return this.delegate.convert( list, sourceType, targetType );
    }
}
