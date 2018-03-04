package selenium.boot.core.logging.cache;


import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import selenium.boot.core.bootstrap.BootstrapContext;



/**
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 2.0
 */
public class CachingLogbackAppender extends UnsynchronizedAppenderBase<ILoggingEvent>
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    public static final String APPENDER_NAME = "CACHING-APPENDER";

    private final Object lock;

    private ILoggingEventCache cache;

    private EventCacheMode cacheMode;

    public CachingLogbackAppender()
    {
        cacheMode = EventCacheMode.ON;
        lock = new Object();
    }

    //endregion

    public void setCacheMode( String mode )
    {
        cacheMode = Enum.valueOf( EventCacheMode.class, mode.toUpperCase() );
    }

    @Override
    protected void append( ILoggingEvent event )
    {
        synchronized( lock )
        {
            if( !isStarted() )
            {
                return;
            }

            cache.put( event );
        }
    }

    @Override
    public void start()
    {
        if( isStarted() )
        {
            return;
        }

        cache = cacheMode.createCache();

        super.start();
    }

    @Override
    public void stop()
    {
        super.stop();

        if( cache != null )
        {
            if( cacheMode.equals( EventCacheMode.ON ) || cacheMode.equals( EventCacheMode.SOFT ) )
            {
                BootstrapContext.getInstance().putProperty( BootstrapContext.CACHED_LOG_MESSAGE_PROPERTY, cache.get() );
            }
            cache = null;
        }
    }
}
