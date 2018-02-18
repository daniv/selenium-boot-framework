package selenium.boot.autoconfigure;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.StaxUtils;
import selenium.boot.spring.support.jackson.SpringHandlerInstantiator;
import selenium.boot.utils.Assert;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;



/**
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 2.0
 */
public class JacksonObjectMapperBuilder
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger( JacksonObjectMapperBuilder.class.getName() );

    private final Map<Class<?>, Class<?>> mixIns = new HashMap<>();

    private final Map<Class<?>, JsonSerializer<?>> serializers = new LinkedHashMap<>();

    private final Map<Class<?>, JsonDeserializer<?>> deserializers = new LinkedHashMap<>();

    private final Map<Object, Boolean> features = new HashMap<>();

    private boolean createXmlMapper = false;

    @Nullable
    private JsonFactory factory;

    @Nullable
    private DateFormat dateFormat;

    @Nullable
    private Locale locale;

    @Nullable
    private TimeZone timeZone;

    @Nullable
    private AnnotationIntrospector annotationIntrospector;

    @Nullable
    private PropertyNamingStrategy propertyNamingStrategy;

    @Nullable
    private TypeResolverBuilder<?> defaultTyping;

    @Nullable
    private JsonInclude.Include serializationInclusion;

    @Nullable
    private FilterProvider filters;

    @Nullable
    private List<Module> modules;

    @Nullable
    private Class<? extends Module>[] moduleClasses;

    private boolean findModulesViaServiceLoader = false;

    private boolean findWellKnownModules = true;

    private ClassLoader moduleClassLoader = getClass().getClassLoader();

    @Nullable
    private HandlerInstantiator handlerInstantiator;

    @Nullable
    private ApplicationContext applicationContext;

    @Nullable
    private Boolean defaultUseWrapper;

    //endregion

    /**
     * Obtain a {@link JacksonObjectMapperBuilder} instance in order to
     * build a regular JSON {@link com.fasterxml.jackson.databind.ObjectMapper} instance.
     */
    public static JacksonObjectMapperBuilder json()
    {
        return new JacksonObjectMapperBuilder();
    }

    /**
     * Obtain a {@link JacksonObjectMapperBuilder} instance in order to
     * build an {@link com.fasterxml.jackson.dataformat.xml.XmlMapper} instance.
     */
    public static JacksonObjectMapperBuilder xml()
    {
        return new JacksonObjectMapperBuilder().createXmlMapper( true );
    }

    /**
     * If set to {@code true}, an {@link com.fasterxml.jackson.dataformat.xml.XmlMapper} will be created using its
     * default constructor. This is only applicable to {@link #build()} calls,
     * not to {@link #configure} calls.
     */
    public JacksonObjectMapperBuilder createXmlMapper( boolean createXmlMapper )
    {
        this.createXmlMapper = createXmlMapper;
        return this;
    }

    /**
     * Define the {@link com.fasterxml.jackson.core.JsonFactory} to be used to create the {@link com.fasterxml.jackson.databind.ObjectMapper}
     * instance.
     *
     * @since 5.0
     */
    public JacksonObjectMapperBuilder factory( JsonFactory factory )
    {
        this.factory = factory;
        return this;
    }

    /**
     * Define the format for date/time with the given {@link DateFormat}.
     * <p>Note: Setting this property makes the exposed {@link com.fasterxml.jackson.databind.ObjectMapper}
     * non-thread-safe, according to Jackson's thread safety rules.
     *
     * @see #simpleDateFormat(String)
     */
    public JacksonObjectMapperBuilder dateFormat( DateFormat dateFormat )
    {
        this.dateFormat = dateFormat;
        return this;
    }

    /**
     * Define the date/time format with a {@link SimpleDateFormat}.
     * <p>Note: Setting this property makes the exposed {@link com.fasterxml.jackson.databind.ObjectMapper}
     * non-thread-safe, according to Jackson's thread safety rules.
     *
     * @see #dateFormat(DateFormat)
     */
    public JacksonObjectMapperBuilder simpleDateFormat( String format )
    {
        this.dateFormat = new SimpleDateFormat( format );
        return this;
    }

    /**
     * Override the default {@link Locale} to use for formatting.
     * Default value used is {@link Locale#getDefault()}.
     *
     * @since 4.1.5
     */
    public JacksonObjectMapperBuilder locale( Locale locale )
    {
        this.locale = locale;
        return this;
    }

    /**
     * Override the default {@link Locale} to use for formatting.
     * Default value used is {@link Locale#getDefault()}.
     *
     * @param localeString the locale ID as a String representation
     */
    public JacksonObjectMapperBuilder locale( String localeString )
    {
        this.locale = Locale.forLanguageTag( localeString );//StringUtils.parseLocale( localeString );
        return this;
    }

    /**
     * Override the default {@link TimeZone} to use for formatting.
     * Default value used is UTC (NOT local timezone).
     *
     * @since 4.1.5
     */
    public JacksonObjectMapperBuilder timeZone( TimeZone timeZone )
    {
        this.timeZone = timeZone;
        return this;
    }

    /**
     * Override the default {@link TimeZone} to use for formatting.
     * Default value used is UTC (NOT local timezone).
     *
     * @param timeZoneString the zone ID as a String representation
     *
     * @since 4.1.5
     */
    public JacksonObjectMapperBuilder timeZone( String timeZoneString )
    {
        this.timeZone = StringUtils.parseTimeZoneString( timeZoneString );
        return this;
    }

    /**
     * Set an {@link com.fasterxml.jackson.databind.AnnotationIntrospector} for both serialization and deserialization.
     */
    public JacksonObjectMapperBuilder annotationIntrospector( AnnotationIntrospector annotationIntrospector )
    {
        this.annotationIntrospector = annotationIntrospector;
        return this;
    }

    /**
     * Specify a {@link com.fasterxml.jackson.databind.PropertyNamingStrategy} to
     * configure the {@link com.fasterxml.jackson.databind.ObjectMapper} with.
     */
    public JacksonObjectMapperBuilder propertyNamingStrategy( PropertyNamingStrategy propertyNamingStrategy )
    {
        this.propertyNamingStrategy = propertyNamingStrategy;
        return this;
    }

    /**
     * Specify a {@link com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder} to use for Jackson's default typing.
     *
     * @since 4.2.2
     */
    public JacksonObjectMapperBuilder defaultTyping( TypeResolverBuilder<?> typeResolverBuilder )
    {
        this.defaultTyping = typeResolverBuilder;
        return this;
    }

    /**
     * Set a custom inclusion strategy for serialization.
     *
     * @see com.fasterxml.jackson.annotation.JsonInclude.Include
     */
    public JacksonObjectMapperBuilder serializationInclusion( JsonInclude.Include serializationInclusion )
    {
        this.serializationInclusion = serializationInclusion;
        return this;
    }

    /**
     * Set the global filters to use in order to support {@link com.fasterxml.jackson.annotation.JsonFilter @JsonFilter} annotated POJO.
     */
    public JacksonObjectMapperBuilder filters( FilterProvider filters )
    {
        this.filters = filters;
        return this;
    }

    /**
     * Add mix-in annotations to use for augmenting specified class or interface.
     *
     * @param target      class (or interface) whose annotations to effectively override
     * @param mixinSource class (or interface) whose annotations are to be "added"
     *                    to target's annotations as value
     *
     * @see com.fasterxml.jackson.databind.ObjectMapper#addMixInAnnotations(Class, Class)
     * @since 4.1.2
     */
    public JacksonObjectMapperBuilder mixIn( Class<?> target, Class<?> mixinSource )
    {
        this.mixIns.put( target, mixinSource );
        return this;
    }

    /**
     * Add mix-in annotations to use for augmenting specified class or interface.
     *
     * @param mixIns Map of entries with target classes (or interface) whose annotations
     *               to effectively override as key and mix-in classes (or interface) whose
     *               annotations are to be "added" to target's annotations as value.
     *
     * @see com.fasterxml.jackson.databind.ObjectMapper#addMixInAnnotations(Class, Class)
     * @since 4.1.2
     */
    public JacksonObjectMapperBuilder mixIns( Map<Class<?>, Class<?>> mixIns )
    {
        this.mixIns.putAll( mixIns );
        return this;
    }

    /**
     * Configure custom serializers. Each serializer is registered for the type
     * returned by {@link com.fasterxml.jackson.databind.JsonSerializer#handledType()}, which must not be {@code null}.
     *
     * @see #serializersByType(Map)
     */
    public JacksonObjectMapperBuilder serializers( JsonSerializer<?>... serializers )
    {
        for( JsonSerializer<?> serializer : serializers )
        {
            Class<?> handledType = serializer.handledType();
            if( handledType == null || handledType == Object.class )
            {
                throw new IllegalArgumentException( "Unknown handled type in " + serializer.getClass().getName() );
            }
            this.serializers.put( serializer.handledType(), serializer );
        }
        return this;
    }

    /**
     * Configure a custom serializer for the given type.
     *
     * @see #serializers(com.fasterxml.jackson.databind.JsonSerializer...)
     * @since 4.1.2
     */
    public JacksonObjectMapperBuilder serializerByType( Class<?> type, JsonSerializer<?> serializer )
    {
        this.serializers.put( type, serializer );
        return this;
    }

    /**
     * Configure custom serializers for the given types.
     *
     * @see #serializers(com.fasterxml.jackson.databind.JsonSerializer...)
     */
    public JacksonObjectMapperBuilder serializersByType( Map<Class<?>, JsonSerializer<?>> serializers )
    {
        this.serializers.putAll( serializers );
        return this;
    }

    /**
     * Configure custom deserializers. Each deserializer is registered for the type
     * returned by {@link com.fasterxml.jackson.databind.JsonDeserializer#handledType()}, which must not be {@code null}.
     *
     * @see #deserializersByType(Map)
     * @since 4.3
     */
    public JacksonObjectMapperBuilder deserializers( JsonDeserializer<?>... deserializers )
    {
        for( JsonDeserializer<?> deserializer : deserializers )
        {
            Class<?> handledType = deserializer.handledType();
            if( handledType == null || handledType == Object.class )
            {
                throw new IllegalArgumentException( "Unknown handled type in " + deserializer.getClass().getName() );
            }
            this.deserializers.put( deserializer.handledType(), deserializer );
        }
        return this;
    }

    /**
     * Configure a custom deserializer for the given type.
     *
     * @since 4.1.2
     */
    public JacksonObjectMapperBuilder deserializerByType( Class<?> type, JsonDeserializer<?> deserializer )
    {
        this.deserializers.put( type, deserializer );
        return this;
    }

    /**
     * Configure custom deserializers for the given types.
     */
    public JacksonObjectMapperBuilder deserializersByType( Map<Class<?>, JsonDeserializer<?>> deserializers )
    {
        this.deserializers.putAll( deserializers );
        return this;
    }

    /**
     * Shortcut for {@link com.fasterxml.jackson.databind.MapperFeature#AUTO_DETECT_FIELDS} option.
     */
    public JacksonObjectMapperBuilder autoDetectFields( boolean autoDetectFields )
    {
        this.features.put( MapperFeature.AUTO_DETECT_FIELDS, autoDetectFields );
        return this;
    }

    /**
     * Shortcut for {@link com.fasterxml.jackson.databind.MapperFeature#AUTO_DETECT_SETTERS}/
     * {@link com.fasterxml.jackson.databind.MapperFeature#AUTO_DETECT_GETTERS}/{@link com.fasterxml.jackson.databind.MapperFeature#AUTO_DETECT_IS_GETTERS}
     * options.
     */
    public JacksonObjectMapperBuilder autoDetectGettersSetters( boolean autoDetectGettersSetters )
    {
        this.features.put( MapperFeature.AUTO_DETECT_GETTERS, autoDetectGettersSetters );
        this.features.put( MapperFeature.AUTO_DETECT_SETTERS, autoDetectGettersSetters );
        this.features.put( MapperFeature.AUTO_DETECT_IS_GETTERS, autoDetectGettersSetters );
        return this;
    }

    /**
     * Shortcut for {@link com.fasterxml.jackson.databind.MapperFeature#DEFAULT_VIEW_INCLUSION} option.
     */
    public JacksonObjectMapperBuilder defaultViewInclusion( boolean defaultViewInclusion )
    {
        this.features.put( MapperFeature.DEFAULT_VIEW_INCLUSION, defaultViewInclusion );
        return this;
    }

    /**
     * Shortcut for {@link com.fasterxml.jackson.databind.DeserializationFeature#FAIL_ON_UNKNOWN_PROPERTIES} option.
     */
    public JacksonObjectMapperBuilder failOnUnknownProperties( boolean failOnUnknownProperties )
    {
        this.features.put( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, failOnUnknownProperties );
        return this;
    }

    /**
     * Shortcut for {@link com.fasterxml.jackson.databind.SerializationFeature#FAIL_ON_EMPTY_BEANS} option.
     */
    public JacksonObjectMapperBuilder failOnEmptyBeans( boolean failOnEmptyBeans )
    {
        this.features.put( SerializationFeature.FAIL_ON_EMPTY_BEANS, failOnEmptyBeans );
        return this;
    }

    /**
     * Shortcut for {@link com.fasterxml.jackson.databind.SerializationFeature#INDENT_OUTPUT} option.
     */
    public JacksonObjectMapperBuilder indentOutput( boolean indentOutput )
    {
        this.features.put( SerializationFeature.INDENT_OUTPUT, indentOutput );
        return this;
    }

    /**
     * Define if a wrapper will be used for indexed (List, array) properties or not by
     * default (only applies to {@link com.fasterxml.jackson.dataformat.xml.XmlMapper}).
     *
     * @since 4.3
     */
    public JacksonObjectMapperBuilder defaultUseWrapper( boolean defaultUseWrapper )
    {
        this.defaultUseWrapper = defaultUseWrapper;
        return this;
    }

    /**
     * Specify features to enable.
     *
     * @see com.fasterxml.jackson.core.JsonParser.Feature
     * @see com.fasterxml.jackson.core.JsonGenerator.Feature
     * @see com.fasterxml.jackson.databind.SerializationFeature
     * @see com.fasterxml.jackson.databind.DeserializationFeature
     * @see com.fasterxml.jackson.databind.MapperFeature
     */
    public JacksonObjectMapperBuilder featuresToEnable( Object... featuresToEnable )
    {
        for( Object feature : featuresToEnable )
        {
            this.features.put( feature, Boolean.TRUE );
        }
        return this;
    }

    /**
     * Specify features to disable.
     *
     * @see com.fasterxml.jackson.core.JsonParser.Feature
     * @see com.fasterxml.jackson.core.JsonGenerator.Feature
     * @see com.fasterxml.jackson.databind.SerializationFeature
     * @see com.fasterxml.jackson.databind.DeserializationFeature
     * @see com.fasterxml.jackson.databind.MapperFeature
     */
    public JacksonObjectMapperBuilder featuresToDisable( Object... featuresToDisable )
    {
        for( Object feature : featuresToDisable )
        {
            this.features.put( feature, Boolean.FALSE );
        }
        return this;
    }

    /**
     * Specify one or more modules to be registered with the {@link com.fasterxml.jackson.databind.ObjectMapper}.
     * <p>Note: If this is set, no finding of modules is going to happen - not by
     * Jackson, and not by Spring either (see {@link #findModulesViaServiceLoader}).
     * As a consequence, specifying an empty list here will suppress any kind of
     * module detection.
     * <p>Specify either this or {@link #modulesToInstall}, not both.
     *
     * @see #modules(List)
     * @see com.fasterxml.jackson.databind.Module
     * @since 4.1.5
     */
    public JacksonObjectMapperBuilder modules( Module... modules )
    {
        return modules( Arrays.asList( modules ) );
    }

    /**
     * Set a complete list of modules to be registered with the {@link com.fasterxml.jackson.databind.ObjectMapper}.
     * <p>Note: If this is set, no finding of modules is going to happen - not by
     * Jackson, and not by Spring either (see {@link #findModulesViaServiceLoader}).
     * As a consequence, specifying an empty list here will suppress any kind of
     * module detection.
     * <p>Specify either this or {@link #modulesToInstall}, not both.
     *
     * @see #modules(com.fasterxml.jackson.databind.Module...)
     * @see com.fasterxml.jackson.databind.Module
     */
    public JacksonObjectMapperBuilder modules( List<Module> modules )
    {
        this.modules = new LinkedList<>( modules );
        this.findModulesViaServiceLoader = false;
        this.findWellKnownModules = false;
        return this;
    }

    /**
     * Specify one or more modules to be registered with the {@link com.fasterxml.jackson.databind.ObjectMapper}.
     * <p>Modules specified here will be registered after
     * Spring's autodetection of JSR-310 and Joda-Time, or Jackson's
     * finding of modules (see {@link #findModulesViaServiceLoader}),
     * allowing to eventually override their configuration.
     * <p>Specify either this or {@link #modules}, not both.
     *
     * @see com.fasterxml.jackson.databind.Module
     * @since 4.1.5
     */
    public JacksonObjectMapperBuilder modulesToInstall( Module... modules )
    {
        this.modules = Arrays.asList( modules );
        this.findWellKnownModules = true;
        return this;
    }

    /**
     * Specify one or more modules by class to be registered with
     * the {@link com.fasterxml.jackson.databind.ObjectMapper}.
     * <p>Modules specified here will be registered after
     * Spring's autodetection of JSR-310 and Joda-Time, or Jackson's
     * finding of modules (see {@link #findModulesViaServiceLoader}),
     * allowing to eventually override their configuration.
     * <p>Specify either this or {@link #modules}, not both.
     *
     * @see #modulesToInstall(com.fasterxml.jackson.databind.Module...)
     * @see com.fasterxml.jackson.databind.Module
     */
    @SuppressWarnings( "unchecked" )
    public JacksonObjectMapperBuilder modulesToInstall( Class<? extends Module>... modules )
    {
        this.moduleClasses = modules;
        this.findWellKnownModules = true;
        return this;
    }

    /**
     * Set whether to let Jackson find available modules via the JDK ServiceLoader,
     * based on META-INF metadata in the classpath. Requires Jackson 2.2 or higher.
     * <p>If this mode is not set, Spring's Jackson2ObjectMapperBuilder itself
     * will try to find the JSR-310 and Joda-Time support modules on the classpath -
     * provided that Java 8 and Joda-Time themselves are available, respectively.
     *
     * @see com.fasterxml.jackson.databind.ObjectMapper#findModules()
     */
    public JacksonObjectMapperBuilder findModulesViaServiceLoader( boolean findModules )
    {
        this.findModulesViaServiceLoader = findModules;
        return this;
    }

    /**
     * Set the ClassLoader to use for loading Jackson extension modules.
     */
    public JacksonObjectMapperBuilder moduleClassLoader( ClassLoader moduleClassLoader )
    {
        this.moduleClassLoader = moduleClassLoader;
        return this;
    }

    /**
     * Customize the construction of Jackson handlers ({@link com.fasterxml.jackson.databind.JsonSerializer}, {@link com.fasterxml.jackson.databind.JsonDeserializer},
     * {@code KeyDeserializer}, {@code TypeResolverBuilder} and {@code TypeIdResolver}).
     *
     * @see JacksonObjectMapperBuilder#applicationContext(ApplicationContext)
     */
    public JacksonObjectMapperBuilder handlerInstantiator( HandlerInstantiator handlerInstantiator )
    {
        this.handlerInstantiator = handlerInstantiator;
        return this;
    }

    /**
     * Set the Spring {@link ApplicationContext} in order to autowire Jackson handlers ({@link com.fasterxml.jackson.databind.JsonSerializer},
     * {@link com.fasterxml.jackson.databind.JsonDeserializer}, {@code KeyDeserializer}, {@code TypeResolverBuilder} and {@code TypeIdResolver}).
     *
     * @see selenium.boot.spring.support.jackson.SpringHandlerInstantiator
     */
    public JacksonObjectMapperBuilder applicationContext( ApplicationContext applicationContext )
    {
        this.applicationContext = applicationContext;
        return this;
    }

    /**
     * Build a new {@link com.fasterxml.jackson.databind.ObjectMapper} instance.
     * <p>Each build operation produces an independent {@link com.fasterxml.jackson.databind.ObjectMapper} instance.
     * The builder's settings can get modified, with a subsequent build operation
     * then producing a new {@link com.fasterxml.jackson.databind.ObjectMapper} based on the most recent settings.
     *
     * @return the newly built ObjectMapper
     */
    @SuppressWarnings( "unchecked" )
    public <T extends ObjectMapper> T build()
    {
        ObjectMapper mapper;
        if( this.createXmlMapper )
        {
            mapper = ( this.defaultUseWrapper != null ?
                               new XmlObjectMapperInitializer().create( this.defaultUseWrapper ) :
                                                                                                         new XmlObjectMapperInitializer().create() );
        }
        else
        {
            mapper = ( this.factory != null ? new ObjectMapper( this.factory ) : new ObjectMapper() );
        }
        configure( mapper );
        return ( T ) mapper;
    }

    /**
     * Configure an existing {@link com.fasterxml.jackson.databind.ObjectMapper} instance with this builder's
     * settings. This can be applied to any number of {@code ObjectMappers}.
     *
     * @param objectMapper the ObjectMapper to configure
     */
    public void configure( ObjectMapper objectMapper )
    {
        Assert.notNull( objectMapper, "ObjectMapper must not be null" );

        if( this.findModulesViaServiceLoader )
        {
            objectMapper.registerModules( ObjectMapper.findModules( this.moduleClassLoader ) );
        }
        else if( this.findWellKnownModules )
        {
            registerWellKnownModulesIfAvailable( objectMapper );
        }

        if( this.modules != null )
        {
            for( Module module : this.modules )
            {
                objectMapper.registerModule( module );
            }
        }
        if( this.moduleClasses != null )
        {
            for( Class<? extends Module> module : this.moduleClasses )
            {
                objectMapper.registerModule( BeanUtils.instantiateClass( module ) );
            }
        }

        if( this.dateFormat != null )
        {
            objectMapper.setDateFormat( this.dateFormat );
        }
        if( this.locale != null )
        {
            objectMapper.setLocale( this.locale );
        }
        if( this.timeZone != null )
        {
            objectMapper.setTimeZone( this.timeZone );
        }

        if( this.annotationIntrospector != null )
        {
            objectMapper.setAnnotationIntrospector( this.annotationIntrospector );
        }
        if( this.propertyNamingStrategy != null )
        {
            objectMapper.setPropertyNamingStrategy( this.propertyNamingStrategy );
        }
        if( this.defaultTyping != null )
        {
            objectMapper.setDefaultTyping( this.defaultTyping );
        }
        if( this.serializationInclusion != null )
        {
            objectMapper.setSerializationInclusion( this.serializationInclusion );
        }

        if( this.filters != null )
        {
            objectMapper.setFilterProvider( this.filters );
        }

        for( Class<?> target : this.mixIns.keySet() )
        {
            objectMapper.addMixIn( target, this.mixIns.get( target ) );
        }

        if( !this.serializers.isEmpty() || !this.deserializers.isEmpty() )
        {
            SimpleModule module = new SimpleModule();
            addSerializers( module );
            addDeserializers( module );
            objectMapper.registerModule( module );
        }

        customizeDefaultFeatures( objectMapper );
        for( Object feature : this.features.keySet() )
        {
            configureFeature( objectMapper, feature, this.features.get( feature ) );
        }

        if( this.handlerInstantiator != null )
        {
            objectMapper.setHandlerInstantiator( this.handlerInstantiator );
        }
        else if( this.applicationContext != null )
        {
            objectMapper.setHandlerInstantiator(
                    new SpringHandlerInstantiator( this.applicationContext.getAutowireCapableBeanFactory() ) );
        }
    }

    @SuppressWarnings( "unchecked" )
    private void registerWellKnownModulesIfAvailable( ObjectMapper objectMapper )
    {
        log.trace( "Registering module jsr310 JavaTimeModule" );
        objectMapper.registerModule( new JavaTimeModule() );
        log.trace( "Registering module Jdk8Module" );
        objectMapper.registerModule( new Jdk8Module() );

        final String guavaModuleClassName =  "com.fasterxml.jackson.datatype.guava.GuavaModule";

        try
        {
            Class<? extends Module> guavaModule = ( Class<? extends Module> ) ClassUtils.forName( guavaModuleClassName, this.moduleClassLoader );
            objectMapper.registerModule( BeanUtils.instantiateClass( guavaModule ) );
        }
        catch( ClassNotFoundException ex )
        {
            log.warn( "GuavaModule is not present. skip module registration" );
        }
    }


    // Convenience factory methods

    @SuppressWarnings( "unchecked" )
    private <T> void addSerializers( SimpleModule module )
    {
        for( Class<?> type : this.serializers.keySet() )
        {
            module.addSerializer( ( Class<? extends T> ) type, ( JsonSerializer<T> ) this.serializers.get( type ) );
        }
    }

    @SuppressWarnings( "unchecked" )
    private <T> void addDeserializers( SimpleModule module )
    {
        for( Class<?> type : this.deserializers.keySet() )
        {
            module.addDeserializer( ( Class<T> ) type, ( JsonDeserializer<? extends T> ) this.deserializers.get( type ) );
        }
    }


    private void customizeDefaultFeatures( ObjectMapper objectMapper )
    {
        if( !this.features.containsKey( MapperFeature.DEFAULT_VIEW_INCLUSION ) )
        {
            configureFeature( objectMapper, MapperFeature.DEFAULT_VIEW_INCLUSION, false );
        }
        if( !this.features.containsKey( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES ) )
        {
            configureFeature( objectMapper, DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );
        }
    }

    private void configureFeature( ObjectMapper objectMapper, Object feature, boolean enabled )
    {
        if( feature instanceof JsonParser.Feature )
        {
            objectMapper.configure( ( JsonParser.Feature ) feature, enabled );
        }
        else if( feature instanceof JsonGenerator.Feature )
        {
            objectMapper.configure( ( JsonGenerator.Feature ) feature, enabled );
        }
        else if( feature instanceof SerializationFeature )
        {
            objectMapper.configure( ( SerializationFeature ) feature, enabled );
        }
        else if( feature instanceof DeserializationFeature )
        {
            objectMapper.configure( ( DeserializationFeature ) feature, enabled );
        }
        else if( feature instanceof MapperFeature )
        {
            objectMapper.configure( ( MapperFeature ) feature, enabled );
        }
        else
        {
            throw new FatalBeanException( "Unknown feature class: " + feature.getClass().getName() );
        }
    }


    private static class XmlObjectMapperInitializer
    {

        public ObjectMapper create()
        {
            return new XmlMapper( StaxUtils.createDefensiveInputFactory() );
        }

        public ObjectMapper create( boolean defaultUseWrapper )
        {
            JacksonXmlModule module = new JacksonXmlModule();
            module.setDefaultUseWrapper( defaultUseWrapper );
            return new XmlMapper( new XmlFactory( StaxUtils.createDefensiveInputFactory() ), module );
        }
    }
}
