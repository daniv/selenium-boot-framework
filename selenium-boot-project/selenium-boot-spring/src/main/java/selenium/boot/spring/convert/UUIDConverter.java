package selenium.boot.spring.convert;


import org.springframework.core.convert.converter.Converter;
import org.springframework.util.ClassUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.UUID;



/**
 * Utility to help generate UUID instances from generic objects.
 *
 * @author Dave Syer
 */
class UUIDConverter implements Converter<Object, UUID>
{

    public static final String DEFAULT_CHARSET = "UTF-8";


    /**
     * Convert the input to a UUID using the convenience method
     * {@link #getUUID(Object)}.
     *
     * @see org.springframework.core.convert.converter.Converter#convert(Object)
     */
    public UUID convert( Object source )
    {
        return getUUID( source );
    }

    /**
     * Convenient utility to convert an object to a UUID. If the input is
     * <ul>
     * <li>null: returns null</li>
     * <li>a UUID: returns the input unchanged</li>
     * <li>a String formatted as a UUID: returns the result of
     * {@link java.util.UUID#fromString(String)}</li>
     * <li>any other String: returns {@link java.util.UUID#nameUUIDFromBytes(byte[])} with
     * bytes generated from the input</li>
     * <li>a primitive or primitive wrapper: converts to a String ans then uses
     * the previous conversion method</li>
     * <li>Serializable: returns the {@link java.util.UUID#nameUUIDFromBytes(byte[])} with
     * the serialized bytes of the input</li>
     * </ul>
     * If none of the above applies there will be an exception trying to serialize.
     *
     * @param input an Object
     *
     * @return a UUID constructed from the input
     */
    public static UUID getUUID( Object input )
    {
        if( input == null )
        {
            return null;
        }
        if( input instanceof UUID )
        {
            return ( UUID ) input;
        }
        if( input instanceof String )
        {
            try
            {
                return UUID.fromString( ( String ) input );
            }
            catch( Exception e )
            {
                try
                {
                    return UUID.nameUUIDFromBytes( ( ( String ) input ).getBytes( DEFAULT_CHARSET ) );
                }
                catch( UnsupportedEncodingException ex )
                {
                    throw new IllegalStateException( "Cannot convert String using charset=" + DEFAULT_CHARSET, ex );
                }
            }
        }
        if( ClassUtils.isPrimitiveOrWrapper( input.getClass() ) )
        {
            try
            {
                return UUID.nameUUIDFromBytes( input.toString().getBytes( DEFAULT_CHARSET ) );
            }
            catch( UnsupportedEncodingException e )
            {
                throw new IllegalStateException( "Cannot convert primitive using charset=" + DEFAULT_CHARSET, e );
            }
        }
        byte[] bytes = serialize( input );
        return UUID.nameUUIDFromBytes( bytes );
    }

    private static byte[] serialize( Object object )
    {
        if( object == null )
        {
            return null;
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try
        {
            new ObjectOutputStream( stream ).writeObject( object );
        }
        catch( IOException e )
        {
            throw new IllegalArgumentException( "Could not serialize object of type: " + object.getClass(), e );
        }
        return stream.toByteArray();
    }

}
