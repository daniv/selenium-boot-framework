package selenium.boot.spring.convert;


import ch.qos.logback.core.CoreConstants;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;



/**
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 1.0
 */
final class CollectionToDelimitedStringConverter implements ConditionalGenericConverter
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    private final ConversionService conversionService;

    CollectionToDelimitedStringConverter( ConversionService conversionService )
    {
        this.conversionService = conversionService;
    }

    //endregion

    @Override
    public Set<ConvertiblePair> getConvertibleTypes()
    {
        return Collections.singleton( new ConvertiblePair( Collection.class, String.class ) );
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
        Collection<?> sourceCollection = ( Collection<?> ) source;
        return convert( sourceCollection, sourceType, targetType );
    }

    private Object convert( Collection<?> source, TypeDescriptor sourceType,
                            TypeDescriptor targetType )
    {
        if( source.isEmpty() )
        {
            return CoreConstants.EMPTY_STRING;
        }
        Delimiter delimiter = sourceType.getAnnotation( Delimiter.class );
        return source.stream()
                       .map( ( element ) -> convertElement( element, sourceType, targetType ) )
                       .collect( Collectors.joining( delimiter == null ? "," : delimiter.value() ) );
    }

    private String convertElement( Object element, TypeDescriptor sourceType,
                                   TypeDescriptor targetType )
    {
        return String.valueOf( this.conversionService.convert( element,
                                                               sourceType.elementTypeDescriptor( element ), targetType
        ) );
    }

    @Override
    public boolean matches( @NonNull TypeDescriptor sourceType, @Nullable TypeDescriptor targetType )
    {
        TypeDescriptor sourceElementType = sourceType.getElementTypeDescriptor();
        if( targetType == null || sourceElementType == null )
        {
            return true;
        }
        return this.conversionService.canConvert( sourceElementType, targetType )
                       || sourceElementType.getType().isAssignableFrom( targetType.getType() );
    }

}
