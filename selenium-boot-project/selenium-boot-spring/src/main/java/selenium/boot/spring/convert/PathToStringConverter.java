package selenium.boot.spring.convert;


import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;

import java.nio.file.Path;

/**
 * Converts a {@link java.nio.file.Path} to a {@code String} expression
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @see org.springframework.core.env.PropertyResolver
 * @see org.springframework.core.convert.ConversionService
 * @since 1.0
 */
final class PathToStringConverter implements Converter<Path,String>
{
    @Override
    @Nullable
    public String convert( @Nullable Path source )
    {
        if( null == source ) return null;
        return source.toString();
    }

    @Override
    public String toString()
    {
        return String.format( "Converts a %s to a %s", Path.class.getName(), String.class.getName() );
    }
}
