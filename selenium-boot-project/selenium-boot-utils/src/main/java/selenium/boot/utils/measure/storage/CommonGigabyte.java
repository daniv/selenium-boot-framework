package selenium.boot.utils.measure.storage;


import java.math.BigInteger;
import java.util.function.Function;



/**
 * Gigabyte as commonly found in the wild (1 Gigabyte = 1 073 741 824 Byte).
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 1.0
 */
final class CommonGigabyte extends StorageUnit<CommonGigabyte>
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    CommonGigabyte( final BigInteger bytes )
    {
        super( bytes );
    }

    //endregion

    /**
     * @param numberOfBytes The amount of bytes the Gigabyte contains.
     *
     * @return A new Gigabyte unit with the given value.
     */
    public static CommonGigabyte valueOf( final BigInteger numberOfBytes )
    {
        return new CommonGigabyte( numberOfBytes );
    }

    /**
     * @param numberOfBytes The amount of bytes the Gigabyte contains.
     *
     * @return A new Gigabyte unit with the given value.
     */
    public static CommonGigabyte valueOf( final Long numberOfBytes )
    {
        return valueOf( numberOfBytes.longValue() );
    }

    /**
     * @param numberOfBytes The amount of bytes the Gigabyte contains.
     *
     * @return A new Gigabyte unit with the given value.
     */
    public static CommonGigabyte valueOf( final long numberOfBytes )
    {
        return new CommonGigabyte( BigInteger.valueOf( numberOfBytes ) );
    }

    @Override
    protected Function<BigInteger, StorageUnit<?>> converter()
    {
        return StorageUnits:: commonValueOf;
    }

    @Override
    protected BigInteger getNumberOfBytesPerUnit()
    {
        return StorageUnit.BYTES_IN_A_GIBIBYTE;
    }

    @Override
    protected String getSymbol()
    {
        return "GB";
    }

    @Override
    public CommonGigabyte add( final long bytesToAdd )
    {
        return new CommonGigabyte( bytes.add( BigInteger.valueOf( bytesToAdd ) ) );
    }

    @Override
    public CommonGigabyte add( final StorageUnit<?> storageAmount )
    {

        return new CommonGigabyte( bytes.add( storageAmount.bytes ) );
    }

    @Override
    public CommonGigabyte divide( final long divisor )
    {
        return new CommonGigabyte( bytes.divide( BigInteger.valueOf( divisor ) ) );
    }

    @Override
    public CommonGigabyte multiply( final long factor )
    {
        return new CommonGigabyte( bytes.multiply( BigInteger.valueOf( factor ) ) );
    }

    @Override
    public CommonGigabyte subtract( final long bytesToSubtract )
    {
        return new CommonGigabyte( bytes.subtract( BigInteger.valueOf( bytesToSubtract ) ) );
    }

    @Override
    public CommonGigabyte subtract( final StorageUnit<?> storageAmount )
    {
        return new CommonGigabyte( bytes.subtract( storageAmount.bytes ) );
    }

}
