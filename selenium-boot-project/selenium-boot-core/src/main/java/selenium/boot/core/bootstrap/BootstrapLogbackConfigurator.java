package selenium.boot.core.bootstrap;


import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.Configurator;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.status.OnConsoleStatusListener;
import ch.qos.logback.core.status.OnErrorConsoleStatusListener;
import ch.qos.logback.core.status.StatusManager;
import ch.qos.logback.core.util.StatusListenerConfigHelper;
import org.apache.commons.lang3.BooleanUtils;
import selenium.boot.core.logging.cache.CachingLogbackAppender;
import selenium.boot.core.logging.cache.EventCacheMode;
import selenium.boot.utils.Systems;



/**
 * BasicConfigurator configures logback-classic by attaching a
 * {@link selenium.boot.core.logging.cache.CachingLogbackAppender} and  to the root logger.
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @see ch.qos.logback.core.status.StatusManager
 * @see ch.qos.logback.classic.LoggerContext
 * @see selenium.boot.utils.Systems#isDebuggerAttached
 * @see selenium.boot.core.logging.cache.CachingLogbackAppender
 * @see selenium.boot.core.logging.cache.EventCacheMode
 * @since 1.0
 */
public class BootstrapLogbackConfigurator extends ContextAwareBase implements Configurator
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    public BootstrapLogbackConfigurator()
    {
        super();
    }

    //endregion


    @Override
    public void configure( LoggerContext loggerContext )
    {

        // clearing status manager from existing messages
        StatusManager statusManager = loggerContext.getStatusManager();
        
        if( statusManager.getCount() > 0 )
        {
            statusManager.clear();
        }

        this.addInfo("Setting up bootstrap configuration.");
        loggerContext.setName( "bootstrap" );
        this.addInfo("Context logger 'name' property was set to " + loggerContext.getName() );
        
        loggerContext.setPackagingDataEnabled( true );
        this.addInfo("Context logger 'packagingDataEnabled' property was set to " + loggerContext.isPackagingDataEnabled() );

        loggerContext.setMaxCallerDataDepth( 10 );
        this.addInfo("Context logger 'maxCallerDataDepth' property was set to " + loggerContext.getMaxCallerDataDepth() );

        setContext( loggerContext );
        if( Systems.isDebuggerAttached() )
        {
            StatusListenerConfigHelper.addOnConsoleListenerInstance( loggerContext, new OnConsoleStatusListener() );
        }
        else
        {
            System.setProperty( CoreConstants.STATUS_LISTENER_CLASS_KEY, OnErrorConsoleStatusListener.class.getName() );
        }

        StatusListenerConfigHelper.installIfAsked( loggerContext );

        CachingLogbackAppender appender = new CachingLogbackAppender();
        appender.setContext( loggerContext );
        appender.setName( CachingLogbackAppender.APPENDER_NAME );
        appender.setCacheMode( EventCacheMode.ON.name() );
        addInfo( "Starting appender " + CachingLogbackAppender.APPENDER_NAME );
        appender.start();

        Logger rootLogger = loggerContext.getLogger( Logger.ROOT_LOGGER_NAME );
        rootLogger.addAppender( appender );
        if( BooleanUtils.isFalse( loggerContext.isStarted() ) )
        {
            loggerContext.start();
        }

    }
}
