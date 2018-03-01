package selenium.boot.utils.measure.storage;


import java.math.BigInteger;
import java.util.function.Function;



/**
 * Tebibyte as specified in ISO IEC 80000-13:2008 (1 Tebibyte = 1 099 511 627 776 Byte).
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 2.0
 */
public final class Tebibyte extends StorageUnit<Tebibyte>
{
    //region initialization and constructors section

    Tebibyte( final BigInteger bytes )
    {
        super( bytes );
    }

    //endregion  

    /**
     * @param numberOfBytes The amount of bytes the Tebibyte contains.
     *
     * @return A new Tebibyte unit with the given value.
     */
    
    public static Tebibyte valueOf( final BigInteger numberOfBytes )
    {
        return new Tebibyte( numberOfBytes );
    }

    /**
     * @param numberOfBytes The amount of bytes the Tebibyte contains.
     *
     * @return A new Tebibyte unit with the given value.
     */
    
    public static Tebibyte valueOf( final Long numberOfBytes )
    {
        return valueOf( numberOfBytes.longValue() );
    }

    /**
     * @param numberOfBytes The amount of bytes the Tebibyte contains.
     *
     * @return A new Tebibyte unit with the given value.
     */
    
    public static Tebibyte valueOf( final long numberOfBytes )
    {
        return new Tebibyte( BigInteger.valueOf( numberOfBytes ) );
    }

    @Override
    protected Function< BigInteger,  StorageUnit<?>> converter()
    {
        return StorageUnits:: binaryValueOf;
    }

    @Override
    protected BigInteger getNumberOfBytesPerUnit()
    {
        return StorageUnit.BYTES_IN_A_TEBIBYTE;
    }

    @Override
    protected String getSymbol()
    {
        return "TiB";
    }

    @Override
    public Tebibyte add( final long bytesToAdd )
    {
        return new Tebibyte( bytes.add( BigInteger.valueOf( bytesToAdd ) ) );
    }

    @Override
    public Tebibyte add( final StorageUnit<?> storageAmount )
    {
        return new Tebibyte( bytes.add( storageAmount.bytes ) );
    }

    @Override
    public Tebibyte divide( final long divisor )
    {
        return new Tebibyte( bytes.divide( BigInteger.valueOf( divisor ) ) );
    }

    @Override
    public Tebibyte multiply( final long factor )
    {
        return new Tebibyte( bytes.multiply( BigInteger.valueOf( factor ) ) );
    }

    @Override
    public Tebibyte subtract( final long bytesToSubtract )
    {
        return new Tebibyte( bytes.subtract( BigInteger.valueOf( bytesToSubtract ) ) );
    }

    @Override
    public Tebibyte subtract( final StorageUnit<?> storageAmount )
    {
        return new Tebibyte( bytes.subtract( storageAmount.bytes ) );
    }
}
