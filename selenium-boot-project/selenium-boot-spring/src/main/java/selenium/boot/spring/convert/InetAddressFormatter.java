package selenium.boot.spring.convert;


import org.springframework.format.Formatter;
import org.springframework.lang.NonNull;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.Locale;



/**
 * {@link org.springframework.format.Formatter} for {@link java.net.InetAddress}.
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 1.0
 */
final class InetAddressFormatter implements Formatter<InetAddress>
{
    @Override
    public String print( @NonNull InetAddress object, @NonNull Locale locale )
    {
        return object.getHostAddress();
    }

    @Override
    public InetAddress parse( @NonNull String text, @NonNull Locale locale ) throws ParseException
    {
        try
        {
            return InetAddress.getByName( text );
        }
        catch( UnknownHostException ex )
        {
            throw new IllegalStateException( "Unknown host " + text, ex );
        }
    }
}
