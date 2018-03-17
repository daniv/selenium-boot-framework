package selenium.boot.core.logging;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.CompositeConverter;
import com.google.common.collect.Maps;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiElement;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.ansi.AnsiStyle;

import java.util.Collections;
import java.util.Map;



/**
 * Logback {@link ch.qos.logback.core.pattern.CompositeConverter} colors output using the
 * {@link org.springframework.boot.ansi.AnsiOutput} class.
 * A single 'color' option can be provided to the converter, or if not specified color will be picked based on the logging level.
 *
 * @author Phillip Webb
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @see org.springframework.boot.ansi.AnsiColor
 * @see org.springframework.boot.ansi.AnsiElement
 * @see org.springframework.boot.ansi.AnsiOutput
 * @see org.springframework.boot.ansi.AnsiStyle
 * @see ch.qos.logback.core.pattern.CompositeConverter
 * @see ch.qos.logback.classic.spi.ILoggingEvent
 * @since 1.0
 */
public class CustomColorConverter extends CompositeConverter<ILoggingEvent>
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    private static final Map<String, AnsiElement> ELEMENTS;

    private static final Map<Integer, AnsiElement> LEVELS;

    static
    {
        Map<String, AnsiElement> elements = Maps.newHashMap();
        elements.put( "faint", AnsiStyle.FAINT );
        elements.put( "bold", AnsiStyle.BOLD );
        elements.put( "red", AnsiColor.RED );
        elements.put( "green", AnsiColor.GREEN );
        elements.put( "yellow", AnsiColor.YELLOW );
        elements.put( "blue", AnsiColor.BLUE );
        elements.put( "bright-blue", AnsiColor.BRIGHT_BLUE );
        elements.put( "bright-cyan", AnsiColor.BRIGHT_CYAN );
        elements.put( "bright-black", AnsiColor.BRIGHT_BLACK );
        elements.put( "bright-green", AnsiColor.BRIGHT_GREEN );
        elements.put( "bright-yellow", AnsiColor.BRIGHT_YELLOW );
        elements.put( "bright-red", AnsiColor.BRIGHT_RED );
        elements.put( "bright-magenta", AnsiColor.BRIGHT_MAGENTA );
        elements.put( "bold", AnsiStyle.BOLD );
        elements.put( "magenta", AnsiColor.MAGENTA );
        elements.put( "cyan", AnsiColor.CYAN );

        ELEMENTS = Collections.unmodifiableMap( elements );
    }

    static
    {
        Map<Integer, AnsiElement> levels = Maps.newHashMap();
        levels.put( Level.ERROR_INTEGER, AnsiColor.RED );
        levels.put( Level.WARN_INTEGER, AnsiColor.YELLOW );
        levels.put( Level.INFO_INTEGER, AnsiColor.BLUE );
        levels.put( Level.DEBUG_INTEGER, AnsiColor.BLACK );
        levels.put( Level.TRACE_INT, AnsiColor.BRIGHT_GREEN );

        LEVELS = Collections.unmodifiableMap( levels );
    }

    //endregion


    @Override
    protected String transform( ILoggingEvent event, String in )
    {
        AnsiElement element = ELEMENTS.get( getFirstOption() );
        if( element == null )
        {
            element = LEVELS.get( event.getLevel().toInteger() );
            element = ( element == null ? AnsiColor.GREEN : element );
        }
        return toAnsiString( in, element );
    }

    private String toAnsiString( String in, AnsiElement element )
    {
        return AnsiOutput.toString( element, in );
    }
}
