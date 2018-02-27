package selenium.boot.spring.convert;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import selenium.boot.utils.text.StringUtils;

import javax.annotation.Nonnull;
import java.util.concurrent.TimeUnit;



/**
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 2.0
 */
class StringToTimeUnitConverter implements Converter<String, TimeUnit>
{
    //region initialization and constructors section

    private static final Logger log = LoggerFactory.getLogger( StringToTimeUnitConverter.class );

    StringToTimeUnitConverter()
    {
        super();

    }

    //endregion


    /**
     * Convert the source object of type {@code S} to target type {@code T}.
     *
     * @param source the source object to convert, which must be an instance of {@code S} (never {@code null})
     *
     * @return the converted object, which must be an instance of {@code T} (potentially {@code null})
     *
     * @throws IllegalArgumentException if the source cannot be converted to the desired target type
     */
    @Nullable
    @Override
    public TimeUnit convert( @Nonnull String source )
    {
        if( StringUtils.containsIgnoreCase( source, "hour" ) )
        {
            return TimeUnit.HOURS;
        }
        else if( StringUtils.containsIgnoreCase( source, "day" ) )
        {
            return TimeUnit.DAYS;
        }
        else if( StringUtils.containsIgnoreCase( source, "minute" ) )
        {
            return TimeUnit.MINUTES;
        }
        else if( StringUtils.containsIgnoreCase( source, "second" ) )
        {
            return TimeUnit.SECONDS;
        }
        else if( StringUtils.containsIgnoreCase( source, "millis" ) )
        {
            return TimeUnit.MILLISECONDS;
        }

        throw new IllegalArgumentException( "TimeUnit " + source + " is not supported." );
    }
}
