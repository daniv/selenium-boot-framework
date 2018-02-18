package selenium.boot.autoconfigure;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.JacksonProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jackson.JsonComponentModule;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Primary;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.Ordered;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import selenium.boot.spring.support.jackson.DiscoverableSubtypeResolver;
import selenium.boot.spring.support.jackson.FuzzyEnumModule;
import selenium.boot.utils.Assert;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;



/**
 * Autoconfiguration for Jackson json
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 1.0
 */
@Configuration
@AutoConfigureOrder( value = AutoConfigureOrder.DEFAULT_ORDER + 1 )
@ConditionalOnClass( ObjectMapper.class )
@EnableConfigurationProperties( value = { JacksonProperties.class } )
public class JacksonAutoConfiguration
{
    //region initialization and constructors section

    private JacksonProperties jacksonProperties;

    @Autowired
    public JacksonAutoConfiguration( JacksonProperties jacksonProperties )
    {
        this.jacksonProperties = jacksonProperties;
    }

    @Bean
    public JsonComponentModule jsonComponentModule()
    {
        return new JsonComponentModule();
    }

    @Bean( name = "customIndenter" )
    @ConditionalOnClass( DefaultPrettyPrinter.class )
    @Qualifier( value = "customIndenter" )
    public PrettyPrinter customIndenter()
    {
        DefaultPrettyPrinter.Indenter indenter = new DefaultIndenter( "    ", DefaultIndenter.SYS_LF );
        // DefaultPrettyPrinter.Indenter indenter = new DefaultIndenter( "    ", "\n" );
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        printer.indentObjectsWith( indenter );
        printer.indentArraysWith( indenter );

        return printer;
    }


    @Configuration( value = "JacksonConfiguration$JacksonObjectMapperConfiguration" )
    @ConditionalOnClass( { ObjectMapper.class, JacksonObjectMapperBuilder.class } )
    static class JacksonObjectMapperConfiguration
    {

        @Bean( name = "jacksonObjectMapper" )
        @Qualifier( value = "jacksonObjectMapper" )
        @Primary
        @ConditionalOnMissingBean( ObjectMapper.class )
        public ObjectMapper jacksonObjectMapper( JacksonObjectMapperBuilder builder )
        {
            ObjectMapper mapper = builder.createXmlMapper( false ).build();
            mapper.setSubtypeResolver( new DiscoverableSubtypeResolver() );
            return mapper;
        }

    }


    @Configuration( value = "JacksonConfiguration$ParameterNamesModuleConfiguration" )
    @ConditionalOnClass( ParameterNamesModule.class )
    static class ParameterNamesModuleConfiguration
    {
        @Bean
        @ConditionalOnMissingBean( ParameterNamesModule.class )
        public ParameterNamesModule parameterNamesModule()
        {
            return new ParameterNamesModule( JsonCreator.Mode.DEFAULT );
        }

    }


    @Configuration( value = "FuzzyEnumModuleConfiguration$ParameterNamesModuleConfiguration" )
    @ConditionalOnClass( FuzzyEnumModule.class )
    @Description( value = "A module for deserializing enums that is more permissive than the default." )
    static class FuzzyEnumModuleConfiguration
    {
        @Bean
        @ConditionalOnMissingBean( FuzzyEnumModule.class )
        public FuzzyEnumModule fuzzyEnumModule()
        {
            return new FuzzyEnumModule();
        }
    }


    @Configuration( value = "JacksonConfiguration$JacksonObjectMapperBuilderConfiguration" )
    @ConditionalOnClass( value = { ObjectMapper.class, JacksonObjectMapperBuilder.class } )
    static class JacksonObjectMapperBuilderConfiguration
    {
        private final ApplicationContext applicationContext;

        JacksonObjectMapperBuilderConfiguration( ApplicationContext applicationContext )
        {
            this.applicationContext = applicationContext;
        }

        @Bean( name = "jacksonObjectMapperBuilder" )
        @ConditionalOnMissingBean( JacksonObjectMapperBuilder.class )
        public JacksonObjectMapperBuilder jacksonObjectMapperBuilder( List<JacksonObjectMapperBuilderCustomizer> customizers )
        {
            JacksonObjectMapperBuilder builder = new JacksonObjectMapperBuilder();
            builder.applicationContext( this.applicationContext );
            customize( builder, customizers );
            return builder;
        }

        private void customize( JacksonObjectMapperBuilder builder, List<JacksonObjectMapperBuilderCustomizer> customizers )
        {
            for( JacksonObjectMapperBuilderCustomizer customizer : customizers )
            {
                customizer.customize( builder );
            }
        }
    }


    @Configuration( value = "JacksonConfiguration$ObjectMapperBuilderCustomizerConfiguration" )
    @ConditionalOnClass( value = { ObjectMapper.class, JacksonObjectMapperBuilder.class } )
    @EnableConfigurationProperties( value = { JacksonProperties.class } )
    static class ObjectMapperBuilderCustomizerConfiguration
    {
        @Bean
        public StandardObjectMapperBuilderCustomizer standardObjectMapperBuilderCustomizer( ApplicationContext applicationContext,
                                                                                            JacksonProperties jacksonProperties )
        {
            return new StandardObjectMapperBuilderCustomizer( applicationContext, jacksonProperties );
        }

        private static final class StandardObjectMapperBuilderCustomizer implements JacksonObjectMapperBuilderCustomizer, Ordered
        {
            private final ApplicationContext applicationContext;

            private final JacksonProperties jacksonProperties;


            StandardObjectMapperBuilderCustomizer( ApplicationContext applicationContext, JacksonProperties jacksonProperties )
            {
                this.applicationContext = applicationContext;
                this.jacksonProperties = jacksonProperties;
            }

            @Override
            public int getOrder()
            {
                return 0;
            }

            @Override
            public void customize( JacksonObjectMapperBuilder builder )
            {

                if( this.jacksonProperties.getDefaultPropertyInclusion() != null )
                {
                    builder.serializationInclusion(
                            this.jacksonProperties.getDefaultPropertyInclusion() );
                }
                if( this.jacksonProperties.getTimeZone() != null )
                {
                    builder.timeZone( this.jacksonProperties.getTimeZone() );
                }

                configureFeatures( builder, this.jacksonProperties.getDeserialization() );
                configureFeatures( builder, this.jacksonProperties.getSerialization() );
                configureFeatures( builder, this.jacksonProperties.getMapper() );
                configureFeatures( builder, this.jacksonProperties.getParser() );
                configureFeatures( builder, this.jacksonProperties.getGenerator() );
                configureDateFormat( builder );
                configurePropertyNamingStrategy( builder );
                configureModules( builder );
                configureLocale( builder );
            }

            private void configureFeatures( JacksonObjectMapperBuilder builder, Map<?, Boolean> features )
            {
                features.forEach( ( feature, value ) -> {
                    if( value != null )
                    {
                        if( value )
                        {
                            builder.featuresToEnable( feature );
                        }
                        else
                        {
                            builder.featuresToDisable( feature );
                        }
                    }
                } );
            }

            private void configureDateFormat( JacksonObjectMapperBuilder builder )
            {
                String dateFormat = this.jacksonProperties.getDateFormat();
                if( dateFormat != null )
                {
                    try
                    {
                        Class<?> dateFormatClass = ClassUtils.forName( dateFormat, null );
                        builder.dateFormat(
                                ( DateFormat ) BeanUtils.instantiateClass( dateFormatClass ) );
                    }
                    catch( ClassNotFoundException ex )
                    {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat( dateFormat );
                        TimeZone timeZone = this.jacksonProperties.getTimeZone();
                        if( timeZone == null )
                        {
                            timeZone = LocaleContextHolder.getTimeZone();
                        }
                        simpleDateFormat.setTimeZone( timeZone );
                        builder.dateFormat( simpleDateFormat );
                    }
                }
            }

            private void configurePropertyNamingStrategy( JacksonObjectMapperBuilder builder )
            {
                String strategy = this.jacksonProperties.getPropertyNamingStrategy();
                if( strategy != null )
                {
                    try
                    {
                        configurePropertyNamingStrategyClass( builder, ClassUtils.forName( strategy, null ) );
                    }
                    catch( ClassNotFoundException ex )
                    {
                        configurePropertyNamingStrategyField( builder, strategy );
                    }
                }
            }

            private void configureModules( JacksonObjectMapperBuilder builder )
            {
                Collection<Module> moduleBeans = getBeans( this.applicationContext, Module.class );
                builder.modulesToInstall( moduleBeans.toArray( new Module[ moduleBeans.size() ] ) );
            }

            private void configureLocale( JacksonObjectMapperBuilder builder )
            {
                builder.locale( LocaleContextHolder.getLocale() );
                //                Locale locale = this.jacksonProperties.getLocale();
                //                if( locale != null )
                //                {
                //                    builder.locale( LocaleContextHolder.getLocale() );
                //                }
            }

            private void configurePropertyNamingStrategyClass( JacksonObjectMapperBuilder builder,
                                                               Class<?> propertyNamingStrategyClass )
            {
                builder.propertyNamingStrategy( ( PropertyNamingStrategy ) BeanUtils.instantiateClass( propertyNamingStrategyClass ) );
            }

            private void configurePropertyNamingStrategyField( JacksonObjectMapperBuilder builder, String fieldName )
            {
                Field field = ReflectionUtils.findField( PropertyNamingStrategy.class, fieldName, PropertyNamingStrategy.class );
                Assert.notNull( field, "Constant named '" + fieldName + "' not found on " + PropertyNamingStrategy.class.getName() );
                try
                {
                    builder.propertyNamingStrategy( ( PropertyNamingStrategy ) field.get( null ) );
                }
                catch( Exception ex )
                {
                    throw new IllegalStateException( ex );
                }
            }

            private static <T> Collection<T> getBeans( ListableBeanFactory beanFactory, Class<T> type )
            {
                return BeanFactoryUtils.beansOfTypeIncludingAncestors( beanFactory, type ).values();
            }
        }
    }
}
