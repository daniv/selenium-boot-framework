package selenium.boot.spring.convert;


import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;

import java.util.HashSet;
import java.util.Set;



/**
 *  Converts String to a Boolean.
 *  Added also 'y' and 'n' lowercase and uppercase
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 1.0
 */
final class StringToBooleanConverter implements Converter<String, Boolean>
{
    //region Static definitions, members, initialization and constructors
    
    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------
    
    private static final Set<String> trueValues = new HashSet<>( 6 );
    
    private static final Set<String> falseValues = new HashSet<>( 6 );
    
    static
    {
        trueValues.add( "true" );
        trueValues.add( "on" );
        trueValues.add( "yes" );
        trueValues.add( "1" );
        trueValues.add( "y" );
        trueValues.add( "Y" );
        
        falseValues.add( "false" );
        falseValues.add( "off" );
        falseValues.add( "no" );
        falseValues.add( "0" );
        falseValues.add( "n" );
        falseValues.add( "N" );
    }
    
    //endregion
    
    
    //---------------------------------------------------------------------
    // Implementation of  Converter interface
    //---------------------------------------------------------------------
    
    @Override
    @Nullable
    public Boolean convert( @Nullable String source )
    {
        if( null == source ) return null;

        String value = source.trim();
        if( "".equals( value ) )
        {
            return null;
        }
        
        value = value.toLowerCase();
        
        if( trueValues.contains( value ) )
        {
            return Boolean.TRUE;
        }
        else if( falseValues.contains( value ) )
        {
            return Boolean.FALSE;
        }
        else
        {
            throw new IllegalArgumentException( "Invalid boolean value '" + source + "'" );
        }
    }

    @Override
    public String toString()
    {
        return String.format( "Converts a %s to a %s", String.class.getName(), Boolean.class.getName() );
    }
}
