package selenium.boot.spring.convert;


import org.springframework.format.Formatter;
import org.springframework.lang.NonNull;

import java.text.ParseException;
import java.util.Locale;



/**
 * {@link java.util.Formatter} for {@code char[]}.
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 2.0
 */
final class CharArrayFormatter implements Formatter<char[]>
{
    @Override
    public String print( @NonNull char[] object, @NonNull Locale locale )
    {
        return new String( object );
    }

    @Override
    public char[] parse( @NonNull  String text, @NonNull Locale locale ) throws ParseException
    {
        return text.toCharArray();
    }
}
