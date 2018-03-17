package selenium.boot.core.logging.async;


import ch.qos.logback.classic.AsyncAppender;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AsyncAppenderBase;



/**
 * An implementation of {@link selenium.boot.core.logging.async.AsyncAppenderFactory}
 * for {@link ch.qos.logback.classic.spi.ILoggingEvent}.
 *
 * This appender and derived classes, log events asynchronously. In order to avoid loss of logging events,
 * this appender should be closed.
 * It is the user's responsibility to close appenders, typically at the end of the application lifecycle.
 * This appender buffers events in a BlockingQueue.
 * Worker thread created by this appender takes events from the head of the queue, and dispatches them to the single appender attached to this appender.
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 1.0
 */
public class AsyncLoggingEventAppenderFactory implements AsyncAppenderFactory<ILoggingEvent>
{
    /**
     * Creates an {@link selenium.boot.core.logging.async.AsyncAppenderFactory}
     * of type {@link ch.qos.logback.classic.spi.ILoggingEvent}
     *
     * @return the {@link selenium.boot.core.logging.async.AsyncAppenderFactory}
     */
    @Override
    public AsyncAppenderBase<ILoggingEvent> build()
    {
        return new AsyncAppender();
    }
}
