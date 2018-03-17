package selenium.boot.core.logging;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import com.google.common.collect.Lists;
import org.slf4j.impl.StaticLoggerBinder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.boot.context.logging.LoggingApplicationListener;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.logging.LogFile;
import org.springframework.boot.logging.LoggingInitializationContext;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.GenericApplicationListener;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ResourceUtils;
import selenium.boot.core.bootstrap.BootstrapContext;
import selenium.boot.utils.text.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;



/**
 * An {@link org.springframework.context.ApplicationListener} that configures the {@link org.springframework.boot.logging.LoggingSystem}.
 * If the environment contains a {@code logging.config} property it will be used to bootstrap the
 * logging system, otherwise a default configuration is used.
 *
 * Regardless, logging levels will be customized if the environment contains {@code logging.level.*} entries.
 * <p>
 * Debug and trace logging for Spring, Tomcat, Jetty and Hibernate will be enabled when
 * the environment contains {@code debug} or {@code trace} properties that aren't set to
 * {@code "false"} (i.e. if you start your application using
 * {@literal java -jar myapp.jar [--debug | --trace]}). If you prefer to ignore these
 * properties you can set {@link #setParseArgs(boolean) parseArgs} to {@code false}.
 * <p>
 * By default, log output is only written to the console. If a log file is required the
 * {@code logging.path} and {@code logging.file} properties can be used.
 * <p>
 * Some system properties may be set as side effects, and these can be useful if the
 * logging configuration supports placeholders (i.e. log4j or logback):
 * <ul>
 *     <li>{@code LOG_FILE} is set to the value of path of the log file that should be writ (if any).</li>
 *     <li>{@code PID} is set to the value of the current process ID if it can be determined.</li>
 * </ul>
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 1.0
 */
public class CustomLoggingApplicationListener implements GenericApplicationListener
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    private static final Bindable<Map<String, String>> STRING_STRING_MAP = Bindable.mapOf( String.class, String.class );

    private static MultiValueMap<Level, String> LOG_LEVEL_LOGGERS;

    private static AtomicBoolean shutdownHookRegistered = new AtomicBoolean( false );

    private static Class<?>[] EVENT_TYPES = {
            ApplicationStartingEvent.class,
            ApplicationEnvironmentPreparedEvent.class,
            ApplicationPreparedEvent.class,
            ContextClosedEvent.class,
            ApplicationFailedEvent.class
    };

    private static Class<?>[] SOURCE_TYPES = {
            SpringApplication.class,
            ApplicationContext.class
    };

    static
    {
        LOG_LEVEL_LOGGERS = new LinkedMultiValueMap<>();
        LOG_LEVEL_LOGGERS.add( Level.DEBUG, "org.springframework.boot" );
        LOG_LEVEL_LOGGERS.add( Level.TRACE, "org.springframework" );
        LOG_LEVEL_LOGGERS.add( Level.TRACE, "selenium.boot" );
        LOG_LEVEL_LOGGERS.add( Level.TRACE, "org.apache.tomcat" );
        LOG_LEVEL_LOGGERS.add( Level.TRACE, "org.apache.catalina" );
        LOG_LEVEL_LOGGERS.add( Level.TRACE, "org.eclipse.jetty" );
        LOG_LEVEL_LOGGERS.add( Level.TRACE, "org.hibernate.tool.hbm2ddl" );
        LOG_LEVEL_LOGGERS.add( Level.DEBUG, "org.hibernate.SQL" );
    }

    private CustomLogbackLoggingSystem loggingSystem;

    private boolean parseArgs = true;

    private int order = LoggingApplicationListener.DEFAULT_ORDER;

    private Level springBootLogging = null;

    //endregion



    @Override
    public boolean supportsEventType( @NonNull ResolvableType resolvableType )
    {
        return isAssignableFrom( resolvableType.getRawClass(), EVENT_TYPES );
    }

    @Override
    public boolean supportsSourceType( @Nullable Class<?> sourceType )
    {
        return isAssignableFrom( sourceType, SOURCE_TYPES );
    }

    private boolean isAssignableFrom( Class<?> type, Class<?>... supportedTypes )
    {
        if( type != null )
        {
            for( Class<?> supportedType : supportedTypes )
            {
                if( supportedType.isAssignableFrom( type ) )
                {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onApplicationEvent( @NonNull ApplicationEvent event )
    {
        if( event instanceof ApplicationStartingEvent )
        {
            ApplicationStartingEvent startingEvent = ApplicationStartingEvent.class.cast( event );
            onApplicationStartingEvent( startingEvent );
        }
        else if( event instanceof ApplicationEnvironmentPreparedEvent )
        {
            onApplicationEnvironmentPreparedEvent( ( ApplicationEnvironmentPreparedEvent ) event );
        }
        else if( event instanceof ApplicationPreparedEvent )
        {
            onApplicationPreparedEvent( ( ApplicationPreparedEvent ) event );
        }
        else if( event instanceof ContextClosedEvent && ( ( ContextClosedEvent ) event ).getApplicationContext().getParent() == null )
        {
            onContextClosedEvent();
        }
        else if( event instanceof ApplicationFailedEvent )
        {
            onApplicationFailedEvent();
        }
    }

    private void onApplicationStartingEvent( ApplicationStartingEvent event )
    {
        this.loggingSystem = new CustomLogbackLoggingSystem( event.getSpringApplication().getClassLoader() );
        this.loggingSystem.beforeInitialize();
    }

    private void onApplicationEnvironmentPreparedEvent( ApplicationEnvironmentPreparedEvent event )
    {
        if( this.loggingSystem == null )
        {
            this.loggingSystem = new CustomLogbackLoggingSystem( event.getSpringApplication().getClassLoader() );
        }

        initialize( event.getEnvironment() );
    }

    private void onApplicationPreparedEvent( ApplicationPreparedEvent event )
    {
        ConfigurableListableBeanFactory beanFactory = event.getApplicationContext().getBeanFactory();
        if( !beanFactory.containsBean( LoggingApplicationListener.LOGGING_SYSTEM_BEAN_NAME ) )
        {
            beanFactory.registerSingleton( LoggingApplicationListener.LOGGING_SYSTEM_BEAN_NAME, this.loggingSystem );
        }
    }

    private void onContextClosedEvent()
    {
        if( this.loggingSystem != null )
        {
            this.loggingSystem.cleanUp();
        }
    }

    private void onApplicationFailedEvent()
    {
        if( this.loggingSystem != null )
        {
            this.loggingSystem.cleanUp();
        }
    }

    /**
     * Initialize the logging system according to preferences expressed through the
     * {@link org.springframework.core.env.Environment} and the classpath.
     *
     * @param environment the environment
     */
    private void initialize( ConfigurableEnvironment environment )
    {
        new ExtendedLoggingSystemProperties( environment ).apply();

        LogFile logFile = LogFile.get( environment );
        if( logFile != null )
        {
            logFile.applyToSystemProperties();
        }

        initializeEarlyLoggingLevel( environment );
        initializeSystem( environment, this.loggingSystem, logFile );
        initializeFinalLoggingLevels( environment, this.loggingSystem );
        displayCachedMessagesIfRequired();
        registerShutdownHookIfNecessary( environment, this.loggingSystem );
    }

    private void initializeEarlyLoggingLevel( ConfigurableEnvironment environment )
    {
        if( this.parseArgs && this.springBootLogging == null )
        {
            if( isSet( environment, "debug" ) )
            {
                this.springBootLogging = Level.DEBUG;
            }
            if( isSet( environment, "trace" ) )
            {
                this.springBootLogging = Level.TRACE;
            }
        }
    }

    private void initializeSystem( ConfigurableEnvironment environment,
                                   CustomLogbackLoggingSystem system,
                                   LogFile logFile )
    {
        LoggingInitializationContext initializationContext = new LoggingInitializationContext( environment );
        String logConfig = environment.getProperty( LoggingApplicationListener.CONFIG_PROPERTY );
        if( ignoreLogConfig( logConfig ) )
        {
            system.initialize( initializationContext, null, logFile );
        }
        else
        {
            try
            {
                ResourceUtils.getURL( logConfig ).openStream().close();
                system.initialize( initializationContext, logConfig, logFile );
            }
            catch( Exception ex )
            {
                // NOTE: can't use the logger here to report the problem, logging system under construction
                System.err.println( "Logging system failed to initialize "
                                            + "using configuration from '" + logConfig + "'" );
                ex.printStackTrace( System.err );
                throw new IllegalStateException( ex );
            }
        }
    }

    private void initializeFinalLoggingLevels( ConfigurableEnvironment environment,
                                               CustomLogbackLoggingSystem system )
    {
        if( this.springBootLogging != null )
        {
            initializeLogLevel( system, this.springBootLogging );
        }
        setLogLevels( system, environment );
    }

    @SuppressWarnings( "unchecked" )
    private void displayCachedMessagesIfRequired()
    {
        if( BootstrapContext.getInstance().containsProperty( BootstrapContext.CACHED_LOG_MESSAGE_PROPERTY ) )
        {
            List<ILoggingEvent> events = ( List<ILoggingEvent> ) BootstrapContext.getInstance().removeProperty( BootstrapContext.CACHED_LOG_MESSAGE_PROPERTY );
            List<Appender<ILoggingEvent>> appenders = Lists.newArrayList();
            LoggerContext loggerContext = ( LoggerContext ) StaticLoggerBinder.getSingleton().getLoggerFactory();
            Logger logger = loggerContext.getLogger( Logger.ROOT_LOGGER_NAME );
            Appender<ILoggingEvent> ca = logger.getAppender( DefaultLogbackConfiguration.DEFAULT_CONSOLE_APPENDER_NAME );
            if( null != ca )
            {
                appenders.add( ca );
                Appender<ILoggingEvent> fa = logger.getAppender( DefaultLogbackConfiguration.DEFAULT_FILE_APPENDER );
                if( null != fa )
                {
                    appenders.add( fa );
                }
            }

            if( events != null )
            {
                for( ILoggingEvent event : events )
                {
                    for( Appender<ILoggingEvent> appender : appenders )
                    {
                        appender.doAppend( event );
                    }
                }
            }
        }
    }

    private void registerShutdownHookIfNecessary( Environment environment,
                                                  CustomLogbackLoggingSystem loggingSystem )
    {
        boolean registerShutdownHook =
                environment.getProperty( LoggingApplicationListener.REGISTER_SHUTDOWN_HOOK_PROPERTY, Boolean.class, false );
        if( registerShutdownHook )
        {
            Runnable shutdownHandler = loggingSystem.getShutdownHandler();
            if( shutdownHandler != null && shutdownHookRegistered.compareAndSet( false, true ) )
            {
                registerShutdownHook( new Thread( shutdownHandler ) );
            }
        }
    }

    private boolean isSet( ConfigurableEnvironment environment, String property )
    {
        String value = environment.getProperty( property );
        return ( value != null && !value.equals( "false" ) );
    }

    private boolean ignoreLogConfig( String logConfig )
    {
        return !StringUtils.hasLength( logConfig ) || logConfig.startsWith( "-D" );
    }

    private void initializeLogLevel( CustomLogbackLoggingSystem system, Level level )
    {
        List<String> loggers = LOG_LEVEL_LOGGERS.get( level );
        if( loggers != null )
        {
            for( String logger : loggers )
            {
                system.setLogLevel( logger, level );
            }
        }
    }

    private void setLogLevels( CustomLogbackLoggingSystem system, Environment environment )
    {
        if( !( environment instanceof ConfigurableEnvironment ) )
        {
            return;
        }

        Binder binder = Binder.get( environment );
        binder.bind( "logging.level", STRING_STRING_MAP )
                .orElseGet( Collections:: emptyMap )
                .forEach( ( name, level ) -> setLogLevel( system, name, level ) );
    }

    private void registerShutdownHook( Thread shutdownHook )
    {
        Runtime.getRuntime().addShutdownHook( shutdownHook );
    }

    private void setLogLevel( CustomLogbackLoggingSystem system, String name, String level )
    {
        try
        {
            name = ( name.equalsIgnoreCase( LoggingSystem.ROOT_LOGGER_NAME ) ? null : name );
            system.setLogLevel( name, coerceLogLevel( level ) );
        }
        catch( RuntimeException ex )
        {
            System.err.println( "Cannot set level: " + level + " for '" + name + "'" );
        }
    }

    private Level coerceLogLevel( String level )
    {
        if( "false".equalsIgnoreCase( level ) )
        {
            return Level.OFF;
        }
        return Level.valueOf( level.toUpperCase() );
    }

    @Override
    public int getOrder()
    {
        return this.order;
    }

    public void setOrder( int order )
    {
        this.order = order;
    }

    /**
     * Sets a custom logging level to be used for Spring Boot and related libraries.
     *
     * @param springBootLogging the logging level
     */
    public void setSpringBootLogging( Level springBootLogging )
    {
        this.springBootLogging = springBootLogging;
    }

    /**
     * Sets if initialization arguments should be parsed for {@literal --debug} and
     * {@literal --trace} options. Defaults to {@code true}.
     *
     * @param parseArgs if arguments should be parsed
     */
    public void setParseArgs( boolean parseArgs )
    {
        this.parseArgs = parseArgs;
    }
}
