package selenium.boot.utils.measure.storage;


import java.math.BigInteger;
import java.util.function.Function;



/**
 * Yobibyte as specified in ISO IEC 80000-13:2008 (1 Yobibyte = 1 208 925 819 614 629 174 706 176 Byte).
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 1.0
 */
public final class Yobibyte extends StorageUnit<Yobibyte>
{
    //region initialization and constructors section

    Yobibyte( final BigInteger bytes )
    {
        super( bytes );
    }

    //endregion  

    /**
     * @param numberOfBytes The amount of bytes the Yobibyte contains.
     *
     * @return A new Yobibyte unit with the given value.
     */
    
    public static Yobibyte valueOf( final BigInteger numberOfBytes )
    {
        return new Yobibyte( numberOfBytes );
    }

    /**
     * @param numberOfBytes The amount of bytes the Yobibyte contains.
     *
     * @return A new Yobibyte unit with the given value.
     */
    
    public static Yobibyte valueOf( final Long numberOfBytes )
    {
        return valueOf( numberOfBytes.longValue() );
    }

    /**
     * @param numberOfBytes The amount of bytes the Yobibyte contains.
     *
     * @return A new Yobibyte unit with the given value.
     */
    
    public static Yobibyte valueOf( final long numberOfBytes )
    {
        return new Yobibyte( BigInteger.valueOf( numberOfBytes ) );
    }

    @Override
    protected Function< BigInteger,  StorageUnit<?>> converter()
    {
        return StorageUnits:: binaryValueOf;
    }

    @Override
    protected BigInteger getNumberOfBytesPerUnit()
    {
        return StorageUnit.BYTES_IN_A_YOBIBYTE;
    }

    @Override
    protected String getSymbol()
    {
        return "YiB";
    }

    @Override
    public Yobibyte add( final long bytesToAdd )
    {
        return new Yobibyte( bytes.add( BigInteger.valueOf( bytesToAdd ) ) );
    }

    @Override
    public Yobibyte add( final StorageUnit<?> storageAmount )
    {
        return new Yobibyte( bytes.add( storageAmount.bytes ) );
    }

    @Override
    public Yobibyte divide( final long divisor )
    {
        return new Yobibyte( bytes.divide( BigInteger.valueOf( divisor ) ) );
    }

    @Override
    public Yobibyte multiply( final long factor )
    {
        return new Yobibyte( bytes.multiply( BigInteger.valueOf( factor ) ) );
    }

    @Override
    public Yobibyte subtract( final long bytesToSubtract )
    {
        return new Yobibyte( bytes.subtract( BigInteger.valueOf( bytesToSubtract ) ) );
    }

    @Override
    public Yobibyte subtract( final StorageUnit<?> storageAmount )
    {
        return new Yobibyte( bytes.subtract( storageAmount.bytes ) );
    }
}
