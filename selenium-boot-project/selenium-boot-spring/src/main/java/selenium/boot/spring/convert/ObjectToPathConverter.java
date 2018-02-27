package selenium.boot.spring.convert;


import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.lang.Nullable;

import java.nio.file.Path;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;



/**
 * Converts a String to a Path
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @see java.nio.file.Path
 * @since 1.0
 */
final class ObjectToPathConverter implements ConditionalGenericConverter
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    private ConversionService conversionService;

    public ObjectToPathConverter( ConversionService conversionService )
    {
        this.conversionService = conversionService;
    }

    //endregion

    /**
     * Should the conversion from {@code sourceType} to {@code targetType} currently under
     * consideration be selected?
     *
     * @param sourceType the type descriptor of the field we are converting from
     * @param targetType the type descriptor of the field we are converting to
     *
     * @return true if conversion should be performed, false otherwise
     */
    @Override
    public boolean matches( TypeDescriptor sourceType, TypeDescriptor targetType )
    {
        return this.conversionService.canConvert( sourceType, new GenericTypeDescriptor( targetType ) );
    }

    /**
     * Return the source and target types that this converter can convert between.
     * Each entry is a convertible source-to-target type pair.
     * For {@link org.springframework.core.convert.converter.ConditionalConverter conditional converters} this method may return
     * {@code null} to indicate all source-to-target pairs should be considered.
     */
    @Override
    public Set<ConvertiblePair> getConvertibleTypes()
    {
        return Collections.singleton( new ConvertiblePair( Object.class, Path.class ) );
    }

    /**
     * Convert the source object to the targetType described by the {@code TypeDescriptor}.
     *
     * @param source     the source object to convert (may be {@code null})
     * @param sourceType the type descriptor of the field we are converting from
     * @param targetType the type descriptor of the field we are converting to
     *
     * @return the converted object
     */
    @Override
    @Nullable
    public Object convert( @Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType )
    {
        Object target = this.conversionService.convert( source, sourceType, new GenericTypeDescriptor( targetType ) );
        return Optional.ofNullable( target );
    }

    @SuppressWarnings( "serial" )
    private static class GenericTypeDescriptor extends TypeDescriptor
    {

        GenericTypeDescriptor( TypeDescriptor typeDescriptor )
        {
            super( typeDescriptor.getResolvableType().getGeneric( 0 ), null, typeDescriptor.getAnnotations() );
        }
    }

    @Override
    public String toString()
    {
        return String.format( "Converts a %s to a %s", Object.class.getName(), Path.class.getName() );
    }
}
