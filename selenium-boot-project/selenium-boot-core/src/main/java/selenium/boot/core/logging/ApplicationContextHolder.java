package selenium.boot.core.logging;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Description;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.OrderComparator;
import org.springframework.core.Ordered;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import selenium.boot.utils.Assert;
import selenium.boot.utils.Classes;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Map;



/**
 * An {@link org.springframework.context.ApplicationListener} to make the context available statically
 * to objects which, for whatever reason, cannot be wired up in Spring (for example, logging appenders which must be
 * defined in XML or properties files used to initialize the logging system).
 *
 * Note that no ID is necessary because this holder should always be used via its static accessors,
 * rather than being injected.
 * Any Spring bean which wishes to access the {@code ApplicationContext} should not rely on this holder; it
 * should simply implement {@code ApplicationContextAware}.
 *
 * <b>WARNING: This object uses static memory to retain the ApplicationContext.</b>
 * This means this bean (and the related configuration strategy)
 * is only usable when no other Logback-enabled Spring applications exist in the same JVM.
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 1.0
 */
@Description( value = "An ApplicationListener to make the context available statically to objects which, " +
                              "for whatever reason, cannot be wired up in Spring."
)
public class ApplicationContextHolder implements ApplicationContextAware, ApplicationListener<ApplicationPreparedEvent>, Ordered
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    private final Logger log = LoggerFactory.getLogger( ApplicationContextHolder.class.getName() );
    
    private static ConfigurableApplicationContext applicationContext;

    /**
     * A flag indicating whether the {@code ApplicationContext} has been refreshed.
     * Theoretically, it is possible for this method to return {@code true} when {@link #hasApplicationContext()} returns {@code false},
     * but in practice that is very unlikely since the bean for the holder should have been created and initialized
     * before the refresh event was raised.
     */
    private static volatile boolean refreshed = false;

    //endregion

    @Override
    public int getOrder()
    {
        return Ordered.HIGHEST_PRECEDENCE;
    }


    //---------------------------------------------------------------------
    // Implementation of ApplicationListener interface
    //---------------------------------------------------------------------

    @Override
    public void onApplicationEvent( @NonNull ApplicationPreparedEvent event )
    {
        event.getApplicationContext().addApplicationListener( this );
    }

    /**
     * Ensures that the {@code ApplicationContext} has been set <i>and</i> that it has been refreshed.
     * The refresh event is sent when the context has completely finished starting up, meaning all beans have been created and
     * initialized successfully.
     *
     * This method has a loosely defined relationship with {@linkplain #getApplicationContext()}.
     * When this method returns {@code true}, calling {@link #getApplicationContext()} is guaranteed to return a non-{@code null} context which
     * has been completely initialized. When this method returns {@code false}, {@link #getApplicationContext()} may
     * return {@code null}, or it may return a non-{@code null} context which is not yet completely initialized.
     *
     * @return {@code true} if the context has been set and refreshed; otherwise, {@code false}
     */
    public static boolean hasApplicationContext()
    {
        return ( applicationContext != null );
    }


    /**
     * Returns a flag indicating whether the {@code ApplicationContext} has been refreshed. Theoretically, it is
     * possible for this method to return {@code true} when {@link #hasApplicationContext()} returns {@code false},
     * but in practice that is very unlikely since the bean for the holder should have been created and initialized
     * before the refresh event was raised.
     *
     * @return {@code true} if the context refresh event has been received; otherwise, {@code false}
     */
    public static boolean isRefreshed()
    {
        return refreshed;
    }

    /**
     * Retrieves the {@code ApplicationContext} set when Spring created and initialized the holder bean. If the
     * holder has not been created (see the class documentation for details on how to wire up the holder), or if
     * the holder has not been initialized, this accessor may return {@code null}.
     *
     * As a general usage pattern, callers should wrap this method in a check for {@link #hasApplicationContext()}.
     * That ensures both that the context is set and also that it has fully initialized. Using a context which has
     * not been fully initialized can result in unexpected initialization behaviors for some beans. The most common
     * example of this behavior is receiving unproxied references to some beans, such as beans which were supposed
     * to have transactional semantics applied by AOP. By waiting for the context refresh event, the likelihood of
     * encountering such behavior is greatly reduced.
     *
     * @return the set context, or {@code null} if the holder bean has not been initialized
     */
    @NonNull
    public static ConfigurableApplicationContext getApplicationContext()
    {
        Assert.state( hasApplicationContext(), () -> "ApplicationContext was not set" );
        return applicationContext;
    }

    @NonNull
    public static Environment getEnvironment()
    {
        Assert.state( hasApplicationContext(), () -> "ApplicationContext was not set" );
        return applicationContext.getEnvironment();
    }

    public static ConfigurableEnvironment getConfigurableEnvironment()
    {
        Assert.state( hasApplicationContext(), () -> "ApplicationContext was not set" );
        return Classes.safeCast( ConfigurableEnvironment.class, getEnvironment() );
    }

    //---------------------------------------------------------------------
    // Implementation of ApplicationContextAware interface
    //---------------------------------------------------------------------

    @Override
    public void setApplicationContext( @NonNull ApplicationContext context ) throws BeansException
    {
        if( ! hasApplicationContext() )
        {
            ApplicationContext ctx = Assert.nonNull( context, () -> "ApplicationContext context cannot be null." );
            Assert.isInstanceOf( ConfigurableApplicationContext.class, ctx );
            applicationContext = Classes.safeCast( ConfigurableApplicationContext.class, ctx );
        }
    }

    /**
     * @return The {@link ConversionService} bean whose name is "seleniumBootConversionService" if available.
     */
    public static ConversionService getConversionService()
    {
        return getConfigurableEnvironment().getConversionService();
    }

    /**
     * Return the internal bean factory of this holded application context.
     * Can be used to access specific functionality of the underlying factory.
     *
     * @throws java.lang.IllegalStateException if the context does not hold an internal
     *                                         bean factory (usually if {@code refresh()} hasn't been called yet or if {@code #close()} has already been called)
     * @throws java.lang.IllegalStateException if the application context is still null
     * @see #hasApplicationContext()
     * @see #isRefreshed()
     */
    public static ConfigurableListableBeanFactory getBeanFactory()
    {
        return getApplicationContext().getBeanFactory();
    }

    //---------------------------------------------------------------------
    // Local Listeners
    //---------------------------------------------------------------------

    @EventListener( classes = { ContextRefreshedEvent.class } )
    public void onContextRefreshed( ContextRefreshedEvent event )
    {
        refreshed = true;
    }

    @EventListener( classes = { ApplicationFailedEvent.class, ContextClosedEvent.class } )
    void terminationEvent( )
    {
        refreshed = false;
        if( hasApplicationContext() )
        {
            applicationContext = null;
        }
    }

    //---------------------------------------------------------------------
    // Fast access to beans
    //---------------------------------------------------------------------

    @Nullable
    public static <T> T getBeanOfType( Class<T> clazz )
    {
        Map<String, T> beans = getBeanFactory().getBeansOfType( clazz );
        ArrayList<T> values = new ArrayList<>( beans.values() );
        if( values.size() != 0 )
        {
            OrderComparator.sort( values );
            return values.get( 0 );
        }
        return null;
    }

    public static <T> T getBean( String name, Class<T> clazz )
    {
        return getApplicationContext().getBean( name, clazz );
    }

    public static <T> T getBean( Class<T> clazz )
    {
        return getApplicationContext().getBean( clazz );
    }

    public static Object getBean( String name, Object... objects )
    {
        return getApplicationContext().getBean( name, objects );
    }

    public static String[] getBeanNames( Class<?> clazz )
    {
        return getApplicationContext().getBeanNamesForType( clazz );
    }

    public static Object getBean( String name )
    {
        return getApplicationContext().getBean( name );
    }

    public static Map<String, Object> getBeansWithAnnotation( Class<? extends Annotation> annotationType )
    {
        return getApplicationContext().getBeansWithAnnotation( annotationType );
    }
}
