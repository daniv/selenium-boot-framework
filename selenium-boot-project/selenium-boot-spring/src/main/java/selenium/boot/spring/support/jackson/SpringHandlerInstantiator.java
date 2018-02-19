package selenium.boot.spring.support.jackson;


import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.ser.VirtualBeanPropertyWriter;
import com.fasterxml.jackson.databind.util.Converter;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import selenium.boot.utils.Assert;



/**
 * Allows for creating Jackson ({@link com.fasterxml.jackson.databind.JsonSerializer},
 * and {@link com.fasterxml.jackson.databind.KeyDeserializer},
 * {@link com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder},
 * {@link com.fasterxml.jackson.databind.jsontype.TypeIdResolver})
 * beans with autowiring against a Spring {@link org.springframework.context.ApplicationContext}.
 *
 * <p>As of Spring 4.3, this overrides all factory methods in {@link HandlerInstantiator},
 * including non-abstract ones and recently introduced ones from Jackson 2.4 and 2.5:
 * for {@link ValueInstantiator}, {@link ObjectIdGenerator}, {@link ObjectIdResolver},
 * {@link PropertyNamingStrategy}, {@link Converter}, {@link VirtualBeanPropertyWriter}.
 *
 * @see Jackson2ObjectMapperBuilder#handlerInstantiator(HandlerInstantiator)
 * @see ApplicationContext#getAutowireCapableBeanFactory()
 * @see com.fasterxml.jackson.databind.cfg.HandlerInstantiator
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 1.0
 */
public class SpringHandlerInstantiator extends HandlerInstantiator
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    private final AutowireCapableBeanFactory beanFactory;


    /**
     * Create a new SpringHandlerInstantiator for the given BeanFactory.
     *
     * @param beanFactory the target BeanFactory
     */
    public SpringHandlerInstantiator( AutowireCapableBeanFactory beanFactory )
    {
        Assert.notNull( beanFactory, "BeanFactory must not be null" );
        this.beanFactory = beanFactory;
    }

    //endregion

    @Override
    public JsonDeserializer<?> deserializerInstance( DeserializationConfig config,
                                                     Annotated annotated, Class<?> implClass )
    {

        return ( JsonDeserializer<?> ) this.beanFactory.createBean( implClass );
    }

    @Override
    public KeyDeserializer keyDeserializerInstance( DeserializationConfig config,
                                                    Annotated annotated, Class<?> implClass )
    {

        return ( KeyDeserializer ) this.beanFactory.createBean( implClass );
    }

    @Override
    public JsonSerializer<?> serializerInstance( SerializationConfig config,
                                                 Annotated annotated, Class<?> implClass )
    {

        return ( JsonSerializer<?> ) this.beanFactory.createBean( implClass );
    }

    @Override
    public TypeResolverBuilder<?> typeResolverBuilderInstance( MapperConfig<?> config,
                                                               Annotated annotated, Class<?> implClass )
    {

        return ( TypeResolverBuilder<?> ) this.beanFactory.createBean( implClass );
    }

    @Override
    public TypeIdResolver typeIdResolverInstance( MapperConfig<?> config, Annotated annotated, Class<?> implClass )
    {
        return ( TypeIdResolver ) this.beanFactory.createBean( implClass );
    }

    /** @since 4.3 */
    @Override
    public ValueInstantiator valueInstantiatorInstance( MapperConfig<?> config,
                                                        Annotated annotated, Class<?> implClass )
    {

        return ( ValueInstantiator ) this.beanFactory.createBean( implClass );
    }

    /** @since 4.3 */
    @Override
    public ObjectIdGenerator<?> objectIdGeneratorInstance( MapperConfig<?> config,
                                                           Annotated annotated, Class<?> implClass )
    {

        return ( ObjectIdGenerator<?> ) this.beanFactory.createBean( implClass );
    }

    /** @since 4.3 */
    @Override
    public ObjectIdResolver resolverIdGeneratorInstance( MapperConfig<?> config,
                                                         Annotated annotated, Class<?> implClass )
    {

        return ( ObjectIdResolver ) this.beanFactory.createBean( implClass );
    }

    /** @since 4.3 */
    @Override
    public PropertyNamingStrategy namingStrategyInstance( MapperConfig<?> config,
                                                          Annotated annotated, Class<?> implClass )
    {

        return ( PropertyNamingStrategy ) this.beanFactory.createBean( implClass );
    }

    /** @since 4.3 */
    @Override
    public Converter<?, ?> converterInstance( MapperConfig<?> config,
                                              Annotated annotated, Class<?> implClass )
    {

        return ( Converter<?, ?> ) this.beanFactory.createBean( implClass );
    }

    /** @since 4.3 */
    @Override
    public VirtualBeanPropertyWriter virtualPropertyWriterInstance( MapperConfig<?> config, Class<?> implClass )
    {
        return ( VirtualBeanPropertyWriter ) this.beanFactory.createBean( implClass );
    }
}
