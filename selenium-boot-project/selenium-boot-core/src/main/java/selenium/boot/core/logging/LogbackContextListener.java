package selenium.boot.core.logging;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggerContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import selenium.boot.utils.text.StringUtils;

import java.util.concurrent.atomic.AtomicInteger;



/**
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 2.0
 */
public class LogbackContextListener implements LoggerContextListener
{
    //region initialization and constructors section

    private static final Logger log = LoggerFactory.getLogger( LogbackContextListener.class );

    /** counts the times in which the logger context was started */
    private AtomicInteger startCount = new AtomicInteger( 0 );

    /** counts the times in which the logger context was reset */
    private AtomicInteger resetCount = new AtomicInteger( 0 );

    /** counts the times in which the logger context was stopped */
    private AtomicInteger stopCount = new AtomicInteger( 0 );

    //endregion


    @Override
    public boolean isResetResistant()
    {
        return true;
    }

    @Override
    public void onStart( LoggerContext loggerContext )
    {
        int count = startCount.incrementAndGet();
        System.out.printf( "loggerContext '%s' was started. count: %d\n", StringUtils.defaultString( loggerContext.getName() ), count );
    }

    @Override
    public void onReset( LoggerContext loggerContext )
    {
        int count = resetCount.incrementAndGet();
        System.out.printf( "loggerContext '%s' was reset. count: %d\n", StringUtils.defaultString( loggerContext.getName() ), count );
    }

    @Override
    public void onStop( LoggerContext loggerContext )
    {
        int count = stopCount.incrementAndGet();
        System.out.printf( "loggerContext '%s' was stopped. count: %d\n", StringUtils.defaultString( loggerContext.getName() ), count );
    }

    @Override
    public void onLevelChange( ch.qos.logback.classic.Logger logger, Level level )
    {
        System.out.println( "onLevelChange for logger " + logger.getName() + " to level " + level.levelStr );
    }
}
