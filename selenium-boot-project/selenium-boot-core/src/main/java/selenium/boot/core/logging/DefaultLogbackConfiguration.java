package selenium.boot.core.logging;


import ch.qos.logback.classic.AsyncAppender;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.classic.turbo.DuplicateMessageFilter;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.AsyncAppenderBase;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.util.OptionHelper;
import ch.qos.logback.core.util.StatusPrinter;
import com.google.common.base.Charsets;
import org.slf4j.impl.StaticLoggerBinder;
import org.springframework.boot.logging.LogFile;
import org.springframework.boot.logging.LoggingInitializationContext;
import org.springframework.boot.logging.logback.ExtendedWhitespaceThrowableProxyConverter;
import org.springframework.boot.logging.logback.WhitespaceThrowableProxyConverter;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.env.PropertySourcesPropertyResolver;
import org.springframework.lang.Nullable;
import selenium.boot.core.logging.LogbackConfigurator.ConsoleStream;
import selenium.boot.core.logging.async.AsyncLoggingEventAppenderFactory;
import selenium.boot.utils.Systems;
import selenium.boot.utils.text.StringUtils;



/**
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 1.0
 */
public class DefaultLogbackConfiguration
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    private static final String ASYNC_NEVER_BLOCK_PROPERTY = "logging.async-never-block";

    private static final String ASYNC_DISCARD_THRESHOLD_PROPERTY = "logging.async-discard-threshold";

    private static final String ASYNC_MAX_FLUSH_TIME_PROPERTY = "logging.async-max-flush-time";

    private static final String WRAP_ASYNC_PROPERTY = "logging.async-wrap-file-appender";

    private static final String ASYNC_INCLUDE_CALLER_DATA = "logging.async-include-caller-data";

    private static final String DUP_TURBO_FILTER_NAME = "DUPLICATE_MSG_GUARD";

    public static final String DEFAULT_CONSOLE_APPENDER_NAME = "DEFAULT_CONSOLE";
    
    public static final String DEFAULT_FILE_APPENDER = "DEFAULT_FILE";

    private static final String FILE_LOG_PATTERN = "%d{${LOG_DATEFORMAT_PATTERN:-yyyy-MM-dd HH:mm:ss.SSS}} | %-5relative | " +
                                                           "${LOG_CONTEXT_NAME:-sb} |" +
                                                           "${LOG_LEVEL_PATTERN:-%5p} | ${PID:- } [%t] | %-40.40logger{0} | " +
                                                           "%file | %M : %L --- %m%n-%wEx";

    private static final String CONSOLE_LOG_PATTERN = "%clr(%-5relative){bold} | " +
                                                              "%clr(${LOG_CONTEXT_NAME:-sb}){bright-blue} | " +
                                                              "%clr(${LOG_LEVEL_PATTERN:-%5p}) | " +
                                                              "%clr( %-35logger{0} ){magenta} | %clr( %M : %L ){yellow} " +
                                                              "%clr(---){faint} %m%n${LOG_EXCEPTION_CONVERSION_WORD:-%wex}";

    private final PropertyResolver patterns;

    private final LoggingInitializationContext initializationContext;

    private final LogFile logFile;

    DefaultLogbackConfiguration( LoggingInitializationContext initializationContext, LogFile logFile )
    {
        this.patterns = getPatternsResolver( initializationContext.getEnvironment() );
        this.logFile = logFile;
        this.initializationContext = initializationContext;
    }

    //endregion

    private PropertyResolver getPatternsResolver( Environment environment )
    {
        if( environment == null )
        {
            return new PropertySourcesPropertyResolver( null );
        }
        if( environment instanceof ConfigurableEnvironment )
        {
            ConfigurableEnvironment configurableEnvironment = ConfigurableEnvironment.class.cast( environment );
            PropertySourcesPropertyResolver resolver = new PropertySourcesPropertyResolver( configurableEnvironment.getPropertySources() );
            resolver.setIgnoreUnresolvableNestedPlaceholders( true );
            return resolver;
        }

        return environment;
    }

    void apply( LogbackConfigurator config )
    {
        synchronized( config.getConfigurationLock() )
        {
            /* Applying conversion rules for throwables */
            config.conversionRule( "wex", WhitespaceThrowableProxyConverter.class );
            config.conversionRule( "wEx", ExtendedWhitespaceThrowableProxyConverter.class );

            /* apply ansi color converter */
            config.conversionRule( "clr", CustomColorConverter.class );

            /* Adding a duplicate message turbo filter */
            DuplicateMessageFilter turboFilter  = new DuplicateMessageFilter();
            turboFilter.setContext( config.getContext() );
            turboFilter.setName( DUP_TURBO_FILTER_NAME );
            turboFilter.setAllowedRepetitions( 1 );
            config.turboFilter( turboFilter );

            /* adding context listener */
            LoggerContextListener listener = new LogbackContextListener();
            config.contextListener( new LogbackContextListener() );

            /* configure loggers */
            config.logger( "org.apache.catalina.startup.DigesterFactory", Level.ERROR );
            config.logger( "org.apache.catalina.util.LifecycleBase", Level.ERROR );
            config.logger( "org.apache.coyote.http11.Http11NioProtocol", Level.WARN );
            config.logger( "org.apache.sshd.common.util.SecurityUtils", Level.WARN );
            config.logger( "org.apache.tomcat.util.net.NioSelectorPool", Level.WARN );
            config.logger( "org.eclipse.jetty.util.component.AbstractLifeCycle", Level.ERROR );
            config.logger( "org.hibernate.validator.internal.util.Version", Level.WARN );

            Appender<ILoggingEvent> consoleAppender = consoleAppender( config );
            if( this.logFile != null )
            {
                /* adding a file appender or a async file appender to root logger */
                Appender<ILoggingEvent> fileAppender = fileAppender( config, this.logFile.toString() );
                Appender<ILoggingEvent> async = wrapAsync( config, fileAppender );
                if( async != null )
                {
                    config.root( Level.INFO, consoleAppender, async );
                }
                else
                {
                    config.root( Level.INFO, consoleAppender, fileAppender );
                }
            }
            else
            {
                config.root( Level.INFO, consoleAppender );
            }

            if( ! config.getLoggerContext().isStarted() )
            {
                config.getLoggerContext().start();
                if( Systems.isDebuggerAttached() )
                {
                    StatusPrinter.print( config.getLoggerContext() );
                }
                else
                {
                    StatusPrinter.printInCaseOfErrorsOrWarnings( config.getLoggerContext() );
                }
            }
        }
    }

    private Appender<ILoggingEvent> consoleAppender( LogbackConfigurator config )
    {
        ConsoleAppender<ILoggingEvent> appender = new ConsoleAppender<>();

        appender.setTarget( ConsoleStream.STDOUT.get() );
        appender.addInfo( "ConsoleAppender: 'target' was set to: [ " + appender.getTarget() + " ]" );
        appender.setImmediateFlush( true );
        appender.addInfo( "ConsoleAppender: 'immediateFlush' was set to: [ " + appender.isImmediateFlush() + " ]" );

         /* Creating a new encoder to support custom pattern */
        appender.addInfo( "Instantiating a new PatternLayoutEncoder " );
        PatternLayoutEncoder encoder = new CustomPatternLayoutEncoder();

        /* will print the pattern on the console */
        encoder.setOutputPatternAsHeader( true );
        encoder.addInfo( "CustomPatternLayoutEncoder: 'outputPatterAsHeader' was set to: [ " + encoder.isOutputPatternAsHeader() + " ]" );
        encoder.setCharset( Charsets.UTF_8 );
        encoder.addInfo( "CustomPatternLayoutEncoder: 'charset' was set to: [ " + encoder.getCharset() + " ]" );

        String logPattern = this.patterns.getProperty( ExtendedLoggingSystemProperties.CONSOLE_LOG_PATTERN, CONSOLE_LOG_PATTERN );
        encoder.setPattern( OptionHelper.substVars( logPattern, config.getContext() ) );
        encoder.addInfo( "CustomPatternLayoutEncoder: 'pattern' was set to: [ " + encoder.getPattern() + " ]" );

        config.encoder( encoder, appender );
        config.appender( DEFAULT_CONSOLE_APPENDER_NAME, appender );

        return appender;
    }

    private Appender<ILoggingEvent> fileAppender( LogbackConfigurator config, String logFile )
    {
        FileAppender<ILoggingEvent> appender = new FileAppender<>();
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        String logPattern = this.patterns.getProperty( "logging.pattern.file", FILE_LOG_PATTERN );
        encoder.setPattern( OptionHelper.substVars( logPattern, config.getContext() ) );
        appender.setEncoder( encoder );
        config.start( encoder );
        appender.setFile( logFile );
        appender.setAppend( false );

        config.appender( DEFAULT_FILE_APPENDER, appender );
        return appender;
    }

    @Nullable
    private Appender<ILoggingEvent> wrapAsync( LogbackConfigurator config, Appender<ILoggingEvent> appender )
    {
        boolean wrap = initializationContext.getEnvironment().getProperty( WRAP_ASYNC_PROPERTY, Boolean.class, true );
        if( ! wrap )
        {
            return null;
        }

        final AsyncAppenderBase<ILoggingEvent> base = new AsyncLoggingEventAppenderFactory().build();
        base.setContext( config.getLoggerContext() );

        /* When debugging the queue size needs to be bigger, to avoid natirak queue message loss */
        if( Systems.isDebuggerAttached() )
        {
            base.setQueueSize( 5_000 );
        }
        else
        {
            base.setQueueSize( 500 );
        }

        int maxFlushTime = initializationContext.getEnvironment().getProperty( ASYNC_MAX_FLUSH_TIME_PROPERTY, int.class, 10 );
        base.addInfo( "Setting " + ASYNC_MAX_FLUSH_TIME_PROPERTY + " to: " + maxFlushTime );
        int discardingThreshold = initializationContext.getEnvironment().getProperty( ASYNC_DISCARD_THRESHOLD_PROPERTY, int.class, 0 );
        base.addInfo( "Setting " + ASYNC_DISCARD_THRESHOLD_PROPERTY + " to: " + discardingThreshold );
        boolean neverBlock = initializationContext.getEnvironment().getProperty( ASYNC_NEVER_BLOCK_PROPERTY, boolean.class, true );
        base.addInfo( "Setting " + ASYNC_NEVER_BLOCK_PROPERTY + " to: " + neverBlock );
        boolean includeCallerData = initializationContext.getEnvironment().getProperty( ASYNC_INCLUDE_CALLER_DATA, boolean.class, true );
        if( includeCallerData )
        {
            if( base instanceof AsyncAppender )
            {
                ( ( AsyncAppender ) base ).setIncludeCallerData( true );
            }
        }

        base.setMaxFlushTime( maxFlushTime );
        base.setDiscardingThreshold( discardingThreshold );
        base.setNeverBlock( neverBlock );
        base.setName( "ASYNC-" + appender.getName() );
        base.addAppender( appender );
        base.start();

        return base;
    }




    //
    //    void apply( LogbackConfigurator config )
    //    {
    //        synchronized( config.getConfigurationLock() )
    //        {
    //
    //            /* Applying conversion rules for throwables */
    //            config.conversionRule( "wex", WhitespaceThrowableProxyConverter.class );
    //            config.conversionRule( "wEx", ExtendedWhitespaceThrowableProxyConverter.class );
    //
    //            /* apply ansi color converter */
    //            config.conversionRule( "clr", CustomColorConverter.class );
    //
    //            /* configure loggers */
    //            config.logger( "org.apache.catalina.startup.DigesterFactory", Level.ERROR );
    //            config.logger( "org.apache.catalina.util.LifecycleBase", Level.ERROR );
    //            config.logger( "org.apache.coyote.http11.Http11NioProtocol", Level.WARN );
    //            config.logger( "org.apache.sshd.common.util.SecurityUtils", Level.WARN );
    //            config.logger( "org.apache.tomcat.util.net.NioSelectorPool", Level.WARN );
    //            config.logger( "org.eclipse.jetty.util.component.AbstractLifeCycle", Level.ERROR );
    //            config.logger( "org.hibernate.validator.internal.util.Version", Level.WARN );
    //
    //          //  base( config );
    //            Appender<ILoggingEvent> consoleAppender = consoleAppender( config );
    ////            if( this.logFile != null )
    ////            {
    ////                Appender<ILoggingEvent> fileAppender = fileAppender( config, this.logFile.toString() );
    ////                config.root( Level.INFO, consoleAppender, fileAppender );
    ////            }
    ////            else
    ////            {
    //                config.root( Level.INFO, consoleAppender );
    //            //}
    //        }
    //    }
    //
    //    /**
    //     * Creates a new {@link ch.qos.logback.core.ConsoleAppender}
    //     * with STDOUT as target, immediateFlush = true and a {@link ch.qos.logback.classic.encoder.PatternLayoutEncoder}
    //     *
    //     * @param config the configuration helper
    //     *
    //     * @return as {@link ch.qos.logback.core.Appender} of instance ConsoleAppender
    //     */
    //    private Appender<ILoggingEvent> consoleAppender( LogbackConfigurator config )
    //    {
    //        ConsoleAppender<ILoggingEvent> appender = new ConsoleAppender<>();
    //        appender.setContext( config.getLoggerContext() );
    //        appender.setTarget( ConsoleStream.STDOUT.get() );
    //        appender.addInfo( "ConsoleAppender: 'target' was set to: [ " + appender.getTarget() + " ]" );
    //        appender.setImmediateFlush( true );
    //        appender.addInfo( "ConsoleAppender: 'immediateFlush' was set to: [ " + appender.isImmediateFlush() + " ]" );
    //
    //        /* Creating a new encoder to support custom pattern */
    //        appender.addInfo( "Instantiating a new PatternLayoutEncoder " );
    //        String logPattern = this.patterns.getProperty( "logging.pattern.console", CONSOLE_LOG_PATTERN );
    //        setEncoder( appender, config, logPattern, true );
    //        config.appender( "CONSOLE", appender );
    //        return appender;
    //    }
    //
    //    /**
    //     * Sets an {@link ch.qos.logback.core.encoder.Encoder} to an {@link ch.qos.logback.core.Appender}
    //     * By default a {@link ch.qos.logback.classic.encoder.PatternLayoutEncoder} is used, with
    //     *
    //     * @param appender         The {@link ch.qos.logback.core.Appender} where the encoder will be added
    //     * @param config           An instance of {@link LogbackConfigurator}
    //     * @param logPattern       the pattern to be applied
    //     * @param patternAsHeader  {@code true} if the pattern header will be displayed on appender.
    //     *
    //     * @see ch.qos.logback.classic.encoder.PatternLayoutEncoder
    //     * @see ch.qos.logback.core.util.OptionHelper
    //     */
    //    @SuppressWarnings( "SameParameterValue" )
    //    private void setEncoder( Appender<ILoggingEvent> appender,
    //                             LogbackConfigurator config,
    //                             String logPattern,
    //                             boolean patternAsHeader )
    //    {
    //
    //        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
    //
    //        encoder.setContext( config.getLoggerContext() );
    //        encoder.setPattern( OptionHelper.substVars( logPattern, config.getContext() ) );
    //        encoder.addInfo( "CustomPatternLayoutEncoder: 'pattern' was set to: [ " + encoder.getPattern() + " ]" );
    //        encoder.setCharset( Charsets.UTF_8 );
    //        encoder.addInfo( "CustomPatternLayoutEncoder: 'charset' was set to: [ " + encoder.getCharset() + " ]" );
    //        encoder.setOutputPatternAsHeader( patternAsHeader );
    //        encoder.addInfo( "CustomPatternLayoutEncoder: 'outputPatterAsHeader' was set to: [ " + encoder.isOutputPatternAsHeader() + " ]" );
    //
    //        config.encoder( encoder, appender );
    //    }
    //
    //
    //
    private Logger getLogger( String name )
    {
        LoggerContext factory = getLoggerContext();
        if( StringUtils.isEmpty( name ) || Logger.ROOT_LOGGER_NAME.equals( name ) )
        {
            name = Logger.ROOT_LOGGER_NAME;
        }
        return factory.getLogger( name );
    }

    private LoggerContext getLoggerContext()
    {
        return ( LoggerContext ) StaticLoggerBinder.getSingleton().getLoggerFactory();
    }

}
