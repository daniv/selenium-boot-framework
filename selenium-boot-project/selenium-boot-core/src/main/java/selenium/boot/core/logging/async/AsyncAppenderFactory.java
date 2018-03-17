package selenium.boot.core.logging.async;


import ch.qos.logback.core.AsyncAppenderBase;
import ch.qos.logback.core.spi.DeferredProcessingAware;



/**
 * Factory used to create an {@link AsyncAppenderBase} of type E
 * source:  io.dropwizard.logging.async
 *
 *
 * @param <E> The type of log event
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @see ch.qos.logback.core.Appender
 * @since 1.0
 */
public interface AsyncAppenderFactory<E extends DeferredProcessingAware>
{
    /**
     * Creates an {@link AsyncAppenderBase} of type E
     *
     * @return a new {@link AsyncAppenderBase}
     */
    AsyncAppenderBase<E> build();
}
