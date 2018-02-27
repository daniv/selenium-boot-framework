package selenium.boot.spring.convert;


import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;

import java.nio.file.Path;
import java.nio.file.Paths;



/**
 * Converts a String to a Path
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @see java.nio.file.Path
 * @since 1.0
 */
final class StringToPathConverter implements Converter<String, Path>
{
    //---------------------------------------------------------------------
    // Implementation of Converter interface
    //---------------------------------------------------------------------

    @Override
    @Nullable
    public Path convert( @Nullable String source )
    {
        if( null == source ) return null;
        return Paths.get( source ).normalize();
    }

    @Override
    public String toString()
    {
        return String.format( "Converts a %s to a %s", String.class.getName(), Path.class.getName() );
    }
}
