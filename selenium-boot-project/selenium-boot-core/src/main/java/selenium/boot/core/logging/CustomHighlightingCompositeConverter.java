package selenium.boot.core.logging;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.color.ForegroundCompositeConverterBase;

import static ch.qos.logback.core.pattern.color.ANSIConstants.BLUE_FG;
import static ch.qos.logback.core.pattern.color.ANSIConstants.BOLD;
import static ch.qos.logback.core.pattern.color.ANSIConstants.CYAN_FG;
import static ch.qos.logback.core.pattern.color.ANSIConstants.DEFAULT_FG;
import static ch.qos.logback.core.pattern.color.ANSIConstants.RED_FG;



/**
 *  * Highlights inner-text depending on the level, in bold red for events of level ERROR, in red for WARN,
 * in BLUE for INFO, and in the default color for other levels.
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @see ch.qos.logback.core.pattern.CompositeConverter
 * @since 1.0
 */
public class CustomHighlightingCompositeConverter extends ForegroundCompositeConverterBase<ILoggingEvent>
{
    @Override
    protected String getForegroundColorCode( ILoggingEvent event )
    {
        Level level = event.getLevel();
        switch( level.toInt() )
        {
            case Level.ERROR_INT:
                return BOLD + RED_FG;
            case Level.WARN_INT:
                return RED_FG;
            case Level.INFO_INT:
                return BLUE_FG;
            case Level.TRACE_INT:
                return CYAN_FG;
            default:
                return DEFAULT_FG;
        }

    }
}
