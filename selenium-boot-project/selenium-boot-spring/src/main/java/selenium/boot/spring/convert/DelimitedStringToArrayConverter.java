package selenium.boot.spring.convert;


import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import selenium.boot.utils.Assert;
import selenium.boot.utils.text.StringUtils;

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.Set;



/**
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 2.0
 */
public class DelimitedStringToArrayConverter implements ConditionalGenericConverter
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    private final ConversionService conversionService;

    DelimitedStringToArrayConverter( ConversionService conversionService )
    {
        Assert.notNull( conversionService, "ConversionService must not be null" );
        this.conversionService = conversionService;
    }

    //endregion

    @Override
    public Set<ConvertiblePair> getConvertibleTypes()
    {
        return Collections.singleton( new ConvertiblePair( String.class, Object[].class ) );
    }

    @Override
    @Nullable
    public Object convert( @Nullable Object source,
                           @NonNull TypeDescriptor sourceType,
                           @NonNull TypeDescriptor targetType )
    {
        if( source == null )
        {
            return null;
        }
        return convert( ( String ) source, sourceType, targetType );
    }

    private Object convert( String source, TypeDescriptor sourceType, TypeDescriptor targetType )
    {
        Delimiter delimiter = targetType.getAnnotation( Delimiter.class );
        String[] elements = getElements( source, ( delimiter == null ? "," : delimiter.value() ) );

        TypeDescriptor elementDescriptor = targetType.getElementTypeDescriptor();
        Object target = Array.newInstance( elementDescriptor.getType(), elements.length );
        for( int i = 0; i < elements.length; i++ )
        {
            String sourceElement = elements[ i ];
            Object targetElement = this.conversionService.convert( sourceElement.trim(),
                                                                   sourceType, elementDescriptor );
            Array.set( target, i, targetElement );
        }
        return target;
    }

    private String[] getElements( String source, String delimiter )
    {
        return StringUtils.delimitedListToStringArray( source,
                                                       Delimiter.NONE.equals( delimiter ) ? null : delimiter
        );
    }

    @Override
    public boolean matches( @NonNull TypeDescriptor sourceType,
                            @NonNull TypeDescriptor targetType )
    {
        return targetType.getElementTypeDescriptor() == null ||
                       this.conversionService.canConvert( sourceType, targetType.getElementTypeDescriptor() );
    }

}
