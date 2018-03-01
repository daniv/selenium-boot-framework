package selenium.boot.utils.measure.storage;


import java.math.BigInteger;
import java.util.function.Function;



/**
 * Terabyte as commonly found in the wild (1 Terabyte = 1 099 511 627 776 Byte).
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 1.0
 */
public final class CommonTerabyte extends StorageUnit<CommonTerabyte>
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    CommonTerabyte( final BigInteger bytes )
    {
        super( bytes );
    }

    //endregion  

    /**
     * @param numberOfBytes The amount of bytes the Terabyte contains.
     *
     * @return A new Terabyte unit with the given value.
     */
    public static CommonTerabyte valueOf( final BigInteger numberOfBytes )
    {
        return new CommonTerabyte( numberOfBytes );
    }

    /**
     * @param numberOfBytes The amount of bytes the Terabyte contains.
     *
     * @return A new Terabyte unit with the given value.
     */
    public static CommonTerabyte valueOf( final Long numberOfBytes )
    {
        return valueOf( numberOfBytes.longValue() );
    }

    /**
     * @param numberOfBytes The amount of bytes the Terabyte contains.
     *
     * @return A new Terabyte unit with the given value.
     */
    public static CommonTerabyte valueOf( final long numberOfBytes )
    {
        return new CommonTerabyte( BigInteger.valueOf( numberOfBytes ) );
    }

    @Override
    protected Function<BigInteger, StorageUnit<?>> converter()
    {
        return StorageUnits:: commonValueOf;
    }

    @Override
    protected BigInteger getNumberOfBytesPerUnit()
    {
        return StorageUnit.BYTES_IN_A_TEBIBYTE;
    }

    @Override
    protected String getSymbol()
    {
        return "TB";
    }

    @Override
    public CommonTerabyte add( final long bytesToAdd )
    {
        return new CommonTerabyte( bytes.add( BigInteger.valueOf( bytesToAdd ) ) );
    }

    @Override
    public CommonTerabyte add( final StorageUnit<?> storageAmount )
    {
        return new CommonTerabyte( bytes.add( storageAmount.bytes ) );
    }

    @Override
    public CommonTerabyte divide( final long divisor )
    {
        return new CommonTerabyte( bytes.divide( BigInteger.valueOf( divisor ) ) );
    }

    @Override
    public CommonTerabyte multiply( final long factor )
    {
        return new CommonTerabyte( bytes.multiply( BigInteger.valueOf( factor ) ) );
    }

    @Override
    public CommonTerabyte subtract( final long bytesToSubtract )
    {
        return new CommonTerabyte( bytes.subtract( BigInteger.valueOf( bytesToSubtract ) ) );
    }

    @Override
    public CommonTerabyte subtract( final StorageUnit<?> storageAmount )
    {
        return new CommonTerabyte( bytes.subtract( storageAmount.bytes ) );
    }
}
