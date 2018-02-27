package selenium.boot.spring.convert;


import org.springframework.format.Formatter;
import org.springframework.lang.NonNull;

import java.text.ParseException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;



/**
 * A {@link Formatter} for {@link OffsetDateTime} that uses
 * {@link DateTimeFormatter#ISO_OFFSET_DATE_TIME ISO offset formatting}.
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 1.0
 */
final class IsoOffsetFormatter implements Formatter<OffsetDateTime>
{
    @Override
    public String print( @NonNull OffsetDateTime object, @NonNull Locale locale )
    {
        return DateTimeFormatter.ISO_OFFSET_DATE_TIME.format( object );
    }

    @Override
    public OffsetDateTime parse( @NonNull String text, @NonNull Locale locale ) throws ParseException
    {
        return OffsetDateTime.parse( text, DateTimeFormatter.ISO_OFFSET_DATE_TIME );
    }

}
