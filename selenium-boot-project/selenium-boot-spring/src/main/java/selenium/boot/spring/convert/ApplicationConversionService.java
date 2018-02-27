package selenium.boot.spring.convert;


import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.lang.Nullable;
import org.springframework.util.StringValueResolver;




/**
 * A specialization of {@link org.springframework.format.support.FormattingConversionService} configured by default with
 * converters and formatters appropriate for most Spring Boot applications.
 * <p>
 * Designed for direct instantiation but also exposes the static
 * {@link #addApplicationConverters} and
 * {@link #addApplicationFormatters(FormatterRegistry)} utility methods for ad-hoc use
 * against registry instance.
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 2.0
 */
public class ApplicationConversionService extends FormattingConversionService
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    @Nullable
    private static volatile ApplicationConversionService sharedInstance;

    public ApplicationConversionService()
    {
        this( null );
    }

    public ApplicationConversionService( @Nullable StringValueResolver embeddedValueResolver )
    {
        if( embeddedValueResolver != null )
        {
            setEmbeddedValueResolver( embeddedValueResolver );
        }

        DefaultConversionService.addDefaultConverters( this );
        DefaultFormattingConversionService.addDefaultFormatters( this );
        addApplicationConverters( this );
        addApplicationFormatters( this );
    }

    //endregion

    public void addApplicationConverters( ConverterRegistry registry )
    {
        ConversionService service = ( ConversionService ) registry;

        registry.removeConvertible( String.class, Boolean.class );
        registry.addConverter( new ArrayToDelimitedStringConverter( service ) );
        registry.addConverter( new CollectionToDelimitedStringConverter( service ) );
        registry.addConverter( new DelimitedStringToArrayConverter( service ) );
        registry.addConverter( new DelimitedStringToCollectionConverter( service ) );
        registry.addConverter( new StringToDurationConverter() );
        registry.addConverter( new DurationToStringConverter() );
        registry.addConverter( new NumberToDurationConverter() );
        registry.addConverter( new DurationToNumberConverter() );
        registry.addConverter( new StringToBooleanConverter() );
        registry.addConverter( new StringToTimeUnitConverter() );
        registry.addConverter( new StringToPathConverter() );
        registry.addConverter( new PathToStringConverter() );
        registry.addConverter( new UUIDConverter() );
        registry.addConverter( new ObjectToPathConverter( ( ConversionService ) registry ) );
        registry.addConverterFactory( new StringToEnumIgnoringCaseConverterFactory() );
    }

    public void addApplicationFormatters( FormatterRegistry registry )
    {
        registry.addFormatter( new CharArrayFormatter() );
        registry.addFormatter( new InetAddressFormatter() );
        registry.addFormatter( new IsoOffsetFormatter() );
    }

    /**
     * Return a shared default {@code ApplicationConversionService} instance, lazily
     * building it once needed.
     *
     * @return the shared {@code ConversionService} instance (never {@code null})
     */
    public static ConversionService getSharedInstance()
    {
        ApplicationConversionService sharedInstance = ApplicationConversionService.sharedInstance;
        if( sharedInstance == null )
        {
            synchronized( ApplicationConversionService.class )
            {
                sharedInstance = ApplicationConversionService.sharedInstance;
                if( sharedInstance == null )
                {
                    sharedInstance = new ApplicationConversionService();
                    ApplicationConversionService.sharedInstance = sharedInstance;
                }
            }
        }

        return sharedInstance;
    }

}
