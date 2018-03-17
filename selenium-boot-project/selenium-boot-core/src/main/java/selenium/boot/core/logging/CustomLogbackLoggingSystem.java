package selenium.boot.core.logging;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.classic.jul.LevelChangePropagator;
import ch.qos.logback.classic.selector.ContextSelector;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.classic.turbo.TurboFilter;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.classic.util.ContextSelectorStaticBinder;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.spi.FilterReply;
import ch.qos.logback.core.status.Status;
import org.slf4j.ILoggerFactory;
import org.slf4j.Marker;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.slf4j.impl.StaticLoggerBinder;
import org.springframework.boot.logging.LogFile;
import org.springframework.boot.logging.LoggingInitializationContext;
import org.springframework.boot.logging.logback.LogbackLoggingSystem;
import org.springframework.boot.logging.logback.SpringBootJoranConfiguratorBridge;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;
import org.springframework.util.SystemPropertyUtils;
import selenium.boot.utils.Assert;
import selenium.boot.utils.Classes;
import selenium.boot.utils.text.StringUtils;

import javax.annotation.concurrent.GuardedBy;
import java.net.URL;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.LogManager;



/**
 * {@link org.springframework.boot.logging.LoggingSystem} for <a href="http://logback.qos.ch">logback</a>.
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 1.0
 */
public class CustomLogbackLoggingSystem
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    private static final String CONFIGURATION_FILE_PROPERTY = "logback.configurationFile";

    private static final String BRIDGE_HANDLER = "org.slf4j.bridge.SLF4JBridgeHandler";

    @GuardedBy( "JUL_HIJACKING_LOCK" )
    private static boolean julHijacked = false;

    private static final Lock JUL_HIJACKING_LOCK = new ReentrantLock();

    private final ClassLoader classLoader;

    /**
     * This turbo filter will block all the log messages until logback system is initialized.
     * however, if the level message is {@link ch.qos.logback.classic.Level#WARN} or
     * {@link ch.qos.logback.classic.Level#ERROR} the message will be displayed.
     */
    private static final TurboFilter FILTER = new TurboFilter()
    {

        @Override
        public FilterReply decide( Marker marker, Logger logger,
                                   Level level, String format, Object[] params, Throwable t )
        {
            if( level.levelInt == Level.WARN.levelInt )
            {
                return FilterReply.ACCEPT;
            }

            return FilterReply.DENY;
        }

    };

    CustomLogbackLoggingSystem( ClassLoader classLoader )
    {
        this.classLoader = classLoader;
    }

    //endregion


    void beforeInitialize()
    {
        LoggerContext loggerContext = getLoggerContext();

        if( isAlreadyInitialized( loggerContext ) )
        {
            return;
        }

        configureJdkLoggingBridgeHandler();
        loggerContext.getTurboFilterList().add( FILTER );
        LoggerContextListener listener = new LogbackContextListener();
        if( ! loggerContext.getCopyOfListenerList().contains( listener ) )
        {
            loggerContext.addListener( listener );
        }
    }

    void initialize( LoggingInitializationContext initializationContext,
                     String configLocation,
                     LogFile logFile )
    {
        LoggerContext loggerContext = getLoggerContext();
        if( isAlreadyInitialized( loggerContext ) )
        {
            return;
        }

        if( StringUtils.hasLength( configLocation ) )
        {
            initializeWithSpecificConfig( initializationContext, configLocation, logFile );
            return;
        }

        initializeWithConventions( initializationContext, logFile );

        loggerContext.getTurboFilterList().remove( FILTER );
        markAsInitialized( loggerContext );
        if( StringUtils.hasText( System.getProperty( CONFIGURATION_FILE_PROPERTY ) ) )
        {
            getLogger( LogbackLoggingSystem.class.getName() ).warn( "Ignoring '" + CONFIGURATION_FILE_PROPERTY + "' system property. "
                            + "Please use 'logging.config' instead." );
        }
    }

    void cleanUp()
    {
        LoggerContext context = getLoggerContext();
        markAsUninitialized( context );
        removeJdkLoggingBridgeHandler();
        context.getStatusManager().clear();
        context.getTurboFilterList().remove( FILTER );
    }

    void setLogLevel( String loggerName, Level level )
    {
        Logger logger = getLogger( loggerName );
        if( logger != null )
        {
            logger.setLevel( level );
        }
    }

    Runnable getShutdownHandler()
    {
        return new ShutdownHandler();
    }

    private Logger getLogger( String name )
    {
        LoggerContext factory = getLoggerContext();
        if( StringUtils.isEmpty( name ) || Logger.ROOT_LOGGER_NAME.equals( name ) )
        {
            name = Logger.ROOT_LOGGER_NAME;
        }
        return factory.getLogger( name );

    }

    private void loadDefaults( LoggingInitializationContext initializationContext, LogFile logFile )
    {
        LoggerContext context = getLoggerContext();
        stopAndReset( context );
        LogbackConfigurator configurator = new LogbackConfigurator( context, getClass() );

        Environment environment = initializationContext.getEnvironment();

        String value = environment.resolvePlaceholders( "${logging.pattern.level:${LOG_LEVEL_PATTERN:%5p}}" );
        configurator.putProperty( ExtendedLoggingSystemProperties.LOG_LEVEL_PATTERN, value );

        value = environment.resolvePlaceholders( "${logging.pattern.dateformat:${LOG_DATEFORMAT_PATTERN:yyyy-MM-dd HH:mm:ss.SSS}}" );
        configurator.putProperty( ExtendedLoggingSystemProperties.LOG_DATEFORMAT_PATTERN, value );

        value = environment.resolvePlaceholders( "${logging.context.name:${LOG_CONTEXT_NAME:" + CoreConstants.DEFAULT_CONTEXT_NAME + "}}" );
        configurator.putProperty( ExtendedLoggingSystemProperties.CONTEXT_NAME, value );

        new DefaultLogbackConfiguration( initializationContext, logFile ).apply( configurator );
        context.setPackagingDataEnabled( true );
    }

    private void initializeWithConventions( LoggingInitializationContext initializationContext, LogFile logFile )
    {
        String config = getSelfInitializationConfig();
        if( config != null && logFile == null )
        {
            // self initialization has occurred, reinitialize in case of property changes
            reinitialize( initializationContext );
            return;
        }

        if( config == null )
        {
            config = getSpringInitializationConfig();
        }

        if( config != null )
        {
            loadConfiguration( initializationContext, config, logFile );
            return;
        }
        loadDefaults( initializationContext, logFile );
    }

    /**
     * Return any spring specific initialization config that should be applied. By default
     * this method checks {@linkplain #getSpringConfigLocations()}.
     *
     * @return the spring initialization config or {@code null}
     */
    private String getSpringInitializationConfig()
    {
        return findConfig( getSpringConfigLocations() );
    }

    /**
     * Return the spring config locations for this system. By default this method returns
     * a set of locations based on {@linkplain #getStandardConfigLocations()}.
     *
     * @return the spring config locations
     *
     * @see #getSpringInitializationConfig()
     */
    private String[] getSpringConfigLocations()
    {
        String[] locations = getStandardConfigLocations();
        for( int i = 0; i < locations.length; i++ )
        {
            String extension = org.springframework.util.StringUtils.getFilenameExtension( locations[ i ] );
            locations[ i ] = locations[ i ].substring( 0, locations[ i ].length() - extension.length() - 1 ) + "-spring." + extension;
        }
        return locations;
    }

    private String[] getStandardConfigLocations()
    {
        return new String[] { "logback-test.xml", "logback.xml" };
    }

    private String findConfig( String[] locations )
    {
        for( String location : locations )
        {
            ClassPathResource resource = new ClassPathResource( location, this.classLoader );
            if( resource.exists() )
            {
                return "classpath:" + location;
            }
        }
        return null;
    }

    /**
     * Return any self initialization config that has been applied.
     * By default this method checks {@linkplain #getStandardConfigLocations()} and assumes that any file that exists
     * will have been applied.
     *
     * @return the self initialization config or {@code null}
     */
    private String getSelfInitializationConfig()
    {
        return findConfig( getStandardConfigLocations() );
    }

    private boolean isAlreadyInitialized( LoggerContext loggerContext )
    {
        return loggerContext.getObject( LogbackLoggingSystem.class.getName() ) != null;
    }

    private void markAsInitialized( LoggerContext loggerContext )
    {
        loggerContext.putObject( LogbackLoggingSystem.class.getName(), new Object() );
    }

    private void markAsUninitialized( LoggerContext loggerContext )
    {
        loggerContext.removeObject( LogbackLoggingSystem.class.getName() );
    }

    private void stopAndReset( LoggerContext loggerContext )
    {
        loggerContext.getLogger( Logger.ROOT_LOGGER_NAME ).detachAndStopAllAppenders();
        loggerContext.stop();
        loggerContext.reset();
        if( this.isBridgeHandlerAvailable() )
        {
            this.addLevelChangePropagator( loggerContext );
        }

    }

    private void reinitialize( LoggingInitializationContext initializationContext )
    {
        getLoggerContext().reset();
        getLoggerContext().getStatusManager().clear();
        loadConfiguration( initializationContext, getSelfInitializationConfig(), null );
    }

    private void addLevelChangePropagator( LoggerContext loggerContext )
    {
        LevelChangePropagator levelChangePropagator = new LevelChangePropagator();
        levelChangePropagator.setResetJUL( true );
        levelChangePropagator.setContext( loggerContext );
        loggerContext.addListener( levelChangePropagator );
    }

    private void configureJdkLoggingBridgeHandler()
    {
        JUL_HIJACKING_LOCK.lock();
        try
        {
            if( isBridgeHandlerAvailable() )
            {
                removeJdkLoggingBridgeHandler();
                SLF4JBridgeHandler.install();
                julHijacked = true;
            }
        }
        catch( Throwable ex )
        {
            /* Ignore. No java.util.logging bridge is installed. */
        }
        finally
        {
            JUL_HIJACKING_LOCK.unlock();
        }
    }

    private boolean isBridgeHandlerAvailable()
    {
        return Classes.isPresent( BRIDGE_HANDLER, this.classLoader );
    }

    private void removeJdkLoggingBridgeHandler()
    {
        try
        {
            if( isBridgeHandlerAvailable() )
            {
                removeDefaultRootHandler();
                SLF4JBridgeHandler.uninstall();
            }
        }
        catch( Throwable ex )
        {
            // Ignore and continue
        }
    }

    private void removeDefaultRootHandler()
    {
        JUL_HIJACKING_LOCK.lock();
        try
        {
            if( !julHijacked )
            {
                if( isBridgeHandlerAvailable() )
                {
                    try
                    {
                        SLF4JBridgeHandler.removeHandlersForRootLogger();
                        java.util.logging.Logger rootLogger = LogManager.getLogManager().getLogger( "" );
                        Handler[] handlers = rootLogger.getHandlers();
                        if( handlers.length == 1 && handlers[ 0 ] instanceof ConsoleHandler )
                        {
                            rootLogger.removeHandler( handlers[ 0 ] );
                        }
                    }
                    catch( NoSuchMethodError ex )
                    {
                        SLF4JBridgeHandler.uninstall();
                    }
                }
            }
        }
        catch( Throwable ex )
        {
            ex.printStackTrace();
        }
        finally
        {
            JUL_HIJACKING_LOCK.unlock();
        }
    }

    private void initializeWithSpecificConfig( LoggingInitializationContext initializationContext,
                                               String configLocation,
                                               LogFile logFile )
    {
        configLocation = SystemPropertyUtils.resolvePlaceholders( configLocation );
        loadConfiguration( initializationContext, configLocation, logFile );
    }

    private void loadConfiguration( LoggingInitializationContext initializationContext,
                                    String location,
                                    LogFile logFile )
    {

        Assert.notNull( location, "Location must not be null" );
        if( initializationContext != null )
        {
            applySystemProperties( initializationContext.getEnvironment(), logFile );
        }

        LoggerContext loggerContext = getLoggerContext();
        stopAndReset( loggerContext );

        try
        {
            configureByResourceUrl( initializationContext, loggerContext, ResourceUtils.getURL( location ) );
        }
        catch( Exception ex )
        {
            throw new IllegalStateException( "Could not initialize Logback logging from " + location, ex );
        }

        List<Status> statuses = loggerContext.getStatusManager().getCopyOfStatusList();
        StringBuilder errors = new StringBuilder();
        for( Status status : statuses )
        {
            if( status.getLevel() == Status.ERROR )
            {
                errors.append( errors.length() > 0 ? String.format( "%n" ) : "" );
                errors.append( status.toString() );
            }
        }
        if( errors.length() > 0 )
        {
            throw new IllegalStateException( String.format( "Logback configuration error detected: %n%s", errors ) );
        }
    }

    private void configureByResourceUrl( LoggingInitializationContext initializationContext,
                                         LoggerContext loggerContext, URL url ) throws JoranException
    {
        if( url.toString().endsWith( "xml" ) )
        {
            JoranConfigurator configurator = new SpringBootJoranConfiguratorBridge( initializationContext );
            configurator.setContext( loggerContext );
            configurator.doConfigure( url );
        }
        else
        {
            new ContextInitializer( loggerContext ).configureByResource( url );
        }
    }

    private LoggerContext getLoggerContext()
    {
        ILoggerFactory factory = StaticLoggerBinder.getSingleton().getLoggerFactory();
        return ( LoggerContext ) factory;
    }

    private void applySystemProperties( Environment environment, LogFile logFile )
    {
        new ExtendedLoggingSystemProperties( environment ).apply( logFile );
    }

    //---------------------------------------------------------------------
    // Implementation of ShutdownHandler class
    //---------------------------------------------------------------------

    private final class ShutdownHandler implements Runnable
    {
        @Override
        public void run()
        {
            ContextSelector selector = ContextSelectorStaticBinder.getSingleton().getContextSelector();
            LoggerContext loggerContext = selector.getLoggerContext();
            String loggerContextName = loggerContext.getName();
            LoggerContext context = selector.detachLoggerContext( loggerContextName );
            getLoggerContext().stop();
            context.reset();
        }
    }
}
