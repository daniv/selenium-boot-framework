package selenium.boot.spring.convert;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.CollectionFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.lang.Nullable;
import selenium.boot.utils.Assert;
import selenium.boot.utils.text.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Stream;



/**
 * Converts a {@link Delimiter delimited} String to a Collection.
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 1.0
 */
final class DelimitedStringToCollectionConverter implements ConditionalGenericConverter
{
    //region initialization and constructors section

    private final Logger log = LoggerFactory.getLogger( DelimitedStringToCollectionConverter.class.getName() );

    private final ConversionService conversionService;

    DelimitedStringToCollectionConverter( ConversionService conversionService )
    {
        this.conversionService = Assert.nonNull( conversionService, () -> "ConversionService must not be null" );
    }

    //endregion

    @Override
    public Set<ConvertiblePair> getConvertibleTypes()
    {
        return Collections.singleton( new ConvertiblePair( String.class, Collection.class ) );
    }

    @Override
    @Nullable
    public Object convert( @Nullable Object source, TypeDescriptor sourceType,
                           TypeDescriptor targetType )
    {
        if( source == null )
        {
            return null;
        }
        return convert( ( String ) source, sourceType, targetType );
    }

    private Object convert( String source, TypeDescriptor sourceType,
                            TypeDescriptor targetType )
    {
        Delimiter delimiter = targetType.getAnnotation( Delimiter.class );

        String[] elements = getElements( source, ( delimiter == null ? "," : delimiter.value() ) );

        TypeDescriptor elementDescriptor = targetType.getElementTypeDescriptor();
        Collection<Object> target = createCollection( targetType, elementDescriptor, elements.length );
        Stream<Object> stream = Arrays.stream( elements ).map( String:: trim );
        if( elementDescriptor != null )
        {
            stream = stream.map( ( element ) -> this.conversionService.convert( element, sourceType, elementDescriptor ) );
        }
        stream.forEach( target:: add );
        return target;
    }

    private String[] getElements( String source, String delimiter )
    {
        return StringUtils.delimitedListToStringArray( source, Delimiter.NONE.equals( delimiter ) ? null : delimiter );
    }

    private Collection<Object> createCollection( TypeDescriptor targetType,
                                                 TypeDescriptor elementDescriptor, int length )
    {
        return CollectionFactory.createCollection( targetType.getType(),
                                                   ( elementDescriptor != null ? elementDescriptor.getType() : null ), length
        );
    }

    @Override
    public boolean matches( TypeDescriptor sourceType, TypeDescriptor targetType )
    {
        return targetType.getElementTypeDescriptor() == null || this.conversionService
                                                                        .canConvert( sourceType, targetType.getElementTypeDescriptor() );
    }

}
