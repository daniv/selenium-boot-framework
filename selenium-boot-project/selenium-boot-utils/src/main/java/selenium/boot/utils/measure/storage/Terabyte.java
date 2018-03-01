package selenium.boot.utils.measure.storage;


import java.math.BigInteger;
import java.util.function.Function;



/**
 * Terabyte as specified in ISO IEC 80000-13:2008 (1 Terabyte = 1 000 000 000 000 Byte).
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 2.0
 */
public final class Terabyte extends StorageUnit<Terabyte>
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    Terabyte( final BigInteger bytes )
    {
        super( bytes );
    }

    //endregion  

    /**
     * @param numberOfBytes The amount of bytes the Terabyte contains.
     *
     * @return A new Kilobyte unit with the given value.
     */

    public static Terabyte valueOf( final BigInteger numberOfBytes )
    {
        return new Terabyte( numberOfBytes );
    }

    /**
     * @param numberOfBytes The amount of bytes the Terabyte contains.
     *
     * @return A new Terabyte unit with the given value.
     */

    public static Terabyte valueOf( final Long numberOfBytes )
    {
        return valueOf( numberOfBytes.longValue() );
    }

    /**
     * @param numberOfBytes The amount of bytes the Terabyte contains.
     *
     * @return A new Terabyte unit with the given value.
     */

    public static Terabyte valueOf( final long numberOfBytes )
    {
        return new Terabyte( BigInteger.valueOf( numberOfBytes ) );
    }

    @Override
    protected Function<BigInteger, StorageUnit<?>> converter()
    {
        return StorageUnits:: decimalValueOf;
    }

    @Override
    protected BigInteger getNumberOfBytesPerUnit()
    {
        return StorageUnit.BYTES_IN_A_TERABYTE;
    }

    @Override
    protected String getSymbol()
    {
        return "TB";
    }

    @Override
    public Terabyte add( final long bytesToAdd )
    {
        return new Terabyte( bytes.add( BigInteger.valueOf( bytesToAdd ) ) );
    }

    @Override
    public Terabyte add( final StorageUnit<?> storageAmount )
    {
        return new Terabyte( bytes.add( storageAmount.bytes ) );
    }

    @Override
    public Terabyte divide( final long divisor )
    {
        return new Terabyte( bytes.divide( BigInteger.valueOf( divisor ) ) );
    }

    @Override
    public Terabyte multiply( final long factor )
    {
        return new Terabyte( bytes.multiply( BigInteger.valueOf( factor ) ) );
    }

    @Override
    public Terabyte subtract( final long bytesToSubtract )
    {
        return new Terabyte( bytes.subtract( BigInteger.valueOf( bytesToSubtract ) ) );
    }

    @Override
    public Terabyte subtract( final StorageUnit<?> storageAmount )
    {
        return new Terabyte( bytes.subtract( storageAmount.bytes ) );
    }
}
