package selenium.boot.core.logging;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.classic.turbo.TurboFilter;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.OutputStreamAppender;
import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.pattern.Converter;
import ch.qos.logback.core.spi.ContextAware;
import ch.qos.logback.core.spi.LifeCycle;
import ch.qos.logback.core.status.ErrorStatus;
import ch.qos.logback.core.status.InfoStatus;
import ch.qos.logback.core.status.Status;
import ch.qos.logback.core.status.StatusManager;
import ch.qos.logback.core.status.WarnStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import selenium.boot.utils.Assert;
import selenium.boot.utils.Classes;
import selenium.boot.utils.text.StringUtils;

import java.util.HashMap;
import java.util.Map;

//@formatter:off

/**
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @see ch.qos.logback.core.Context
 * @see ch.qos.logback.core.ConsoleAppender
 * @see ch.qos.logback.core.CoreConstants
 * @since 1.0
 */
public class LogbackConfigurator implements ContextAware
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    private final Logger log = LoggerFactory.getLogger( LogbackConfigurator.class.getName() );

    private Context context;

    @Nullable
    private Object origin;

    private int noContextWarning = 0;

    LogbackConfigurator( LoggerContext context, @Nullable Object origin )
    {
        Assert.notNull( context, "Context must not be null" );
        this.context = context;
        this.origin = origin;
    }

    //endregion

    @SuppressWarnings( { "rawtypes", "unchecked" } )
    void conversionRule( String conversionWord, Class<? extends Converter> converterClass )
    {
        Assert.hasLength( conversionWord, "Conversion word must not be empty" );
        Assert.notNull( converterClass, "Converter class must not be null" );

        Map<String, String> registry = ( Map<String, String> ) this.context.getObject( CoreConstants.PATTERN_RULE_REGISTRY );
        if( registry == null )
        {
            registry = new HashMap<>();
            putObject( CoreConstants.PATTERN_RULE_REGISTRY, registry );

        }

        registry.put( conversionWord, converterClass.getName() );
    }

    /**
     * Puts a property in the logger context map
     *
     * @param key   the key value as string
     * @param value the property value as string
     *
     * @throws java.lang.NullPointerException     if the key is null.
     * @throws java.lang.IllegalArgumentException if the key is empty
     * @see ch.qos.logback.core.Context#putProperty(String, String)
     * @see ch.qos.logback.core.Context#putObject(String, Object)
     */
    void putProperty( String key, String value )
    {
        Assert.notNull( key, "Property key must not be null" );
        Assert.hasLength( key, "Property key must not be empty" );
        context.putProperty( key, value );
        addInfo( String.format( "added property '%s' to logback property map", key ) );
    }

    /**
     * Puts a property object in the logger context map
     *
     * @param key   the key value as string
     * @param value the property value as string
     *
     * @throws java.lang.NullPointerException     if the key is null.
     * @throws java.lang.IllegalArgumentException if the key is empty
     * @see ch.qos.logback.core.Context#putProperty(String, String)
     * @see ch.qos.logback.core.Context#putObject(String, Object)
     */
    void putObject( String key, Object value )
    {
        Assert.notNull( key, "Property key must not be null" );
        Assert.hasLength( key, "Property key must not be empty" );
        context.putObject( key, value );
        addInfo( String.format( "added object '%s' to logback object map", key ) );
    }

    @Nullable
    private Object getOrigin()
    {
        return origin;
    }

    Object getConfigurationLock()
    {
        return this.context.getConfigurationLock();
    }

    /**
     * Modifies a logger configuration level
     *
     * @param name  the logger name
     * @param level the modifies level
     *
     * @see ch.qos.logback.classic.Level
     * @see ch.qos.logback.classic.jul.LevelChangePropagator
     */
    void logger( String name, Level level )
    {
        logger( name, level, true );
    }

    /**
     * Modifies a logger configuration level
     *
     * @param name     the logger name
     * @param level    the modifies level
     * @param additive Sets the additive flag
     *
     * @see ch.qos.logback.classic.Level
     * @see ch.qos.logback.classic.jul.LevelChangePropagator
     * @see ch.qos.logback.classic.Logger#isAdditive()
     */
    void logger( String name, Level level, boolean additive )
    {
        logger( name, level, additive, null );
    }

    /**
     * Modifies a logger configuration level
     *
     * @param name     the logger name
     * @param level    the modifies level
     * @param additive Sets the additive flag
     * @param appender sets the logger appender
     *
     * @see ch.qos.logback.classic.Level
     * @see ch.qos.logback.classic.jul.LevelChangePropagator
     * @see ch.qos.logback.classic.Logger#isAdditive()
     * @see ch.qos.logback.classic.Logger#addAppender(ch.qos.logback.core.Appender)
     * @see #root(ch.qos.logback.classic.Level, ch.qos.logback.core.Appender[])
     */
    void logger( String name, @Nullable Level level, boolean additive, @Nullable Appender<ILoggingEvent> appender )
    {
        ch.qos.logback.classic.Logger logger = getLoggerContext().getLogger( name );
        String levelStr = "{null}";
        String appenderName = "{null}";

        if( level != null )
        {
            levelStr = level.levelStr;
            logger.setLevel( level );
        }

        logger.setAdditive( additive );

        if( appender != null )
        {
            appenderName = appender.getName();
            logger.addAppender( appender );
        }

        addInfo( String.format( "Logger '%s' was modified; new level: %s, appender: '%s', additive: %s", name, levelStr, appenderName, additive ) );
    }

    /**
     * Configures an appender by giving a name and activate the {@link ch.qos.logback.core.spi.LifeCycle} interface
     *
     * @param name     The appender name
     * @param appender The appender instance
     *
     * @see ch.qos.logback.core.spi.LifeCycle#isStarted()
     * @see ch.qos.logback.core.spi.LifeCycle#start()
     */
    void appender( @NonNull String name, Appender<?> appender )
    {
        appender.setName( name );
        addInfo( "Starting appender: " + StringUtils.quote( name ) );
        appender.addInfo( "starting appender " + StringUtils.quote( name ) );
        start( appender );
    }

    LoggerContext getLoggerContext()
    {
        return Classes.safeCast( LoggerContext.class, context );
    }

    /**
     * Adds a {@link ch.qos.logback.classic.turbo.TurboFilter} to the logger context
     *
     * @param turboFilter The turbo-filter to add.
     *
     * @see ch.qos.logback.classic.LoggerContext#addTurboFilter(ch.qos.logback.classic.turbo.TurboFilter)
     */
    void turboFilter( TurboFilter turboFilter )
    {
        turboFilter.addInfo( "Starting turbo filter" );
        start( turboFilter );
        getLoggerContext().addTurboFilter( turboFilter );
        addInfo( "Adding and starting turbo filter: " + StringUtils.quote( turboFilter.getName() ) );
    }

    /**
     * Implements a conditional {@link ch.qos.logback.core.spi.LifeCycle} instantiation
     * in case that the {@link ch.qos.logback.core.spi.ContextAware} was not initialized with
     * a {@link ch.qos.logback.core.Context} it will set the {@linkplain #getLoggerContext}
     *
     * @param lifeCycle a indstance of Lifecycle interface implementor
     */
    void start( LifeCycle lifeCycle )
    {
        if( lifeCycle instanceof ContextAware )
        {
            if( ( ( ContextAware ) lifeCycle ).getContext() == null )
            {
                ( ( ContextAware ) lifeCycle ).setContext( getLoggerContext() );
            }
        }

        if( !lifeCycle.isStarted() )
        {
            lifeCycle.start();
        }
    }

    /**
     * Appends an {@link ch.qos.logback.core.encoder.Encoder} to an {@link ch.qos.logback.core.Appender}
     *
     * @param encoder  An instance of the encoder
     * @param appender An instance of the appender
     *
     * @see ch.qos.logback.core.OutputStreamAppender#setEncoder(ch.qos.logback.core.encoder.Encoder)
     */
    void encoder( Encoder<ILoggingEvent> encoder, Appender<ILoggingEvent> appender )
    {
        Assert.notNull( encoder, "Encoder<ILoggingEvent>  cannot be null" );
        Assert.notNull( appender, "Appender<ILoggingEvent>  cannot be null" );

        if( appender instanceof OutputStreamAppender )
        {
            ( ( OutputStreamAppender<ILoggingEvent> ) appender ).setEncoder( encoder );
            appender.addInfo( "ConsoleAppender: 'encoder' was set to " + encoder.getClass().getName() );
        }

        encoder.addInfo( "Starting encoder" );
        start( encoder );
    }

    /**
     * Same as {@linkplain #logger(String, ch.qos.logback.classic.Level, boolean, ch.qos.logback.core.Appender)}
     * However, this is the ROOT appender.
     *
     * @param level     the level of the root appender
     * @param appenders a list of appenders to configure
     *
     * @see org.slf4j.Logger#ROOT_LOGGER_NAME
     */
    @SafeVarargs
    final void root( @Nullable Level level, Appender<ILoggingEvent>... appenders )
    {
        LoggerContext loggerContext = getLoggerContext();
        ch.qos.logback.classic.Logger logger = loggerContext.getLogger( Logger.ROOT_LOGGER_NAME );

        if( level != null )
        {
            addInfo( "Setting org.slf4j.Logger.ROOT_LOGGER_NAME level to:  " + level.levelStr );
            logger.setLevel( level );
        }

        for( Appender<ILoggingEvent> appender : appenders )
        {
            addInfo( String.format( "Adding appender '%s' to logger '%s'.", appender.getName(), Logger.ROOT_LOGGER_NAME ) );
            logger.addAppender( appender );
        }
    }

    /**
     * Appends an {@link ch.qos.logback.classic.spi.LoggerContextListener} to the context
     *
     * @param listener An instance of LoggerContextListener
     *
     * @see ch.qos.logback.classic.LoggerContext#addListener(ch.qos.logback.classic.spi.LoggerContextListener)
     */
    void contextListener( LoggerContextListener listener )
    {
        if( ! getLoggerContext().getCopyOfListenerList().contains( listener ) )
        {
            getLoggerContext().addListener( listener );
        }
        getLoggerContext().addListener( listener );
    }

    @Nullable
    private StatusManager getStatusManager()
    {
        return this.context == null ? null : this.context.getStatusManager();
    }

    //---------------------------------------------------------------------
    // Implementation of ContextAware interface
    //---------------------------------------------------------------------

    @Override
    public void setContext( Context context )
    {
        this.context = Assert.nonNull( context, () -> "Context cannot be null" );
    }

    @Override
    public Context getContext()
    {
        return context;
    }

    @Override
    public void addInfo( String msg )
    {
        this.addStatus( new InfoStatus( msg, this.getOrigin() ) );
    }

    @Override
    public void addInfo( String msg, Throwable ex )
    {
        this.addStatus( new InfoStatus( msg, this.getOrigin(), ex ) );
    }

    @Override
    public void addWarn( String msg )
    {
        this.addStatus( new WarnStatus( msg, this.getOrigin() ) );
    }

    @Override
    public void addWarn( String msg, Throwable ex )
    {
        this.addStatus( new WarnStatus( msg, this.getOrigin(), ex ) );
    }

    @Override
    public void addError( String msg )
    {
        this.addStatus( new ErrorStatus( msg, this.getOrigin() ) );
    }

    @Override
    public void addError( String msg, Throwable ex )
    {
        this.addStatus( new ErrorStatus( msg, this.getOrigin(), ex ) );
    }

    public void addStatus( Status status )
    {
        if( this.context == null )
        {
            if( this.noContextWarning++ == 0 )
            {
                System.out.println( "LOGBACK: No context given for " + this );
            }

        }
        else
        {
            StatusManager sm = this.context.getStatusManager();
            if( sm != null )
            {
                sm.add( status );
            }

        }
    }

    /**
     * A convenience enumeration that defines targets for {@link ch.qos.logback.core.ConsoleAppender}
     *
     * @see ch.qos.logback.core.ConsoleAppender#setTarget(String)
     */
    public enum ConsoleStream
    {
        STDOUT( "System.out" ),

        STDERR( "System.err" );

        private final String value;

        ConsoleStream( String value )
        {
            this.value = value;
        }

        /**
         * @return the string value of the target
         */
        public String get()
        {
            return value;
        }
    }
}
