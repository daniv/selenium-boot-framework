package selenium.boot.core.logging;


import org.springframework.boot.ApplicationPid;
import org.springframework.boot.logging.LogFile;
import org.springframework.boot.logging.LoggingSystemProperties;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.env.PropertySourcesPropertyResolver;
import selenium.boot.utils.Assert;



/**
 * Utility to set system properties that can later be used by log configuration files.
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 2.0
 */
public class ExtendedLoggingSystemProperties
{
    //region initialization and constructors section

    /**
     * The name of the System property that contains the context name.
     */
    public static final String CONTEXT_NAME = "LOG_CONTEXT_NAME";

    /**
     * The name of the System property that contains the process ID.
     */
    public static final String PID_KEY = "PID";

    /**
     * The name of the System property that contains the exception conversion word.
     */
    public static final String EXCEPTION_CONVERSION_WORD = "LOG_EXCEPTION_CONVERSION_WORD";

    /**
     * The name of the System property that contains the log file.
     */
    public static final String LOG_FILE = "LOG_FILE";

    /**
     * The name of the System property that contains the log path.
     */
    public static final String LOG_PATH = "LOG_PATH";

    /**
     * The name of the System property that contains the console log pattern.
     */
    public static final String CONSOLE_LOG_PATTERN = "CONSOLE_LOG_PATTERN";

    /**
     * The name of the System property that contains the file log pattern.
     */
    public static final String FILE_LOG_PATTERN = "FILE_LOG_PATTERN";

    /**
     * The name of the System property that contains the file log max history.
     */
    public static final String FILE_MAX_HISTORY = "LOG_FILE_MAX_HISTORY";

    /**
     * The name of the System property that contains the file log max size.
     */
    public static final String FILE_MAX_SIZE = "LOG_FILE_MAX_SIZE";

    /**
     * The name of the System property that contains the log level pattern.
     */
    public static final String LOG_LEVEL_PATTERN = "LOG_LEVEL_PATTERN";

    /**
     * The name of the System property that contains the log date-format pattern.
     */
    public static final String LOG_DATEFORMAT_PATTERN = "LOG_DATEFORMAT_PATTERN";

    private final ConfigurableEnvironment environment;

    /**
     * Create a new {@link LoggingSystemProperties} instance.
     *
     * @param environment the source environment
     */
    public ExtendedLoggingSystemProperties( Environment environment )
    {
        Assert.nonNull( environment, () -> "Environment must not be null" );
        Assert.isInstanceOf( ConfigurableEnvironment.class, environment );
        this.environment = ConfigurableEnvironment.class.cast( environment );
    }

    //endregion

    void apply()
    {
        apply( null );
    }

    void apply( LogFile logFile )
    {
        PropertyResolver resolver = getPropertyResolver();
        setSystemProperty( resolver, EXCEPTION_CONVERSION_WORD, "exception-conversion-word" );
        setSystemProperty( PID_KEY, new ApplicationPid().toString() );
        setSystemProperty( resolver, CONSOLE_LOG_PATTERN, "pattern.console" );
        setSystemProperty( resolver, FILE_LOG_PATTERN, "pattern.file" );
        setSystemProperty( resolver, FILE_MAX_HISTORY, "file.max-history" );
        setSystemProperty( resolver, FILE_MAX_SIZE, "file.max-size" );
        setSystemProperty( resolver, LOG_LEVEL_PATTERN, "pattern.level" );
        setSystemProperty( resolver, LOG_DATEFORMAT_PATTERN, "pattern.dateformat" );
        setSystemProperty( resolver, CONTEXT_NAME, "context.name" );
        if( logFile != null )
        {
            logFile.applyToSystemProperties();
        }
    }

    private PropertyResolver getPropertyResolver()
    {
        if( this.environment instanceof ConfigurableEnvironment )
        {
            PropertyResolver resolver = new PropertySourcesPropertyResolver( this.environment.getPropertySources() );
            ( ( PropertySourcesPropertyResolver ) resolver ).setIgnoreUnresolvableNestedPlaceholders( true );
            return resolver;
        }
        else
        {
            return this.environment;
        }
    }

    private void setSystemProperty( PropertyResolver resolver, String systemPropertyName, String propertyName )
    {
        this.setSystemProperty( systemPropertyName, resolver.getProperty( "logging." + propertyName ) );
    }

    private void setSystemProperty( String name, String value )
    {
        if( System.getProperty( name ) == null && value != null )
        {
            System.setProperty( name, value );
        }

    }
}
