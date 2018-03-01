package selenium.boot.utils.measure.storage;


import java.math.BigInteger;
import java.util.function.Function;



/**
 * Kilobyte as commonly found in the wild (1 Kilobyte = 1 024 Byte).
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 1.0
 */
final class CommonKilobyte extends StorageUnit<CommonKilobyte>
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    CommonKilobyte( final BigInteger bytes )
    {
        super( bytes );
    }


    //endregion

    /**
     * @param numberOfBytes The amount of bytes the Kilobyte contains.
     *
     * @return A new Kilobyte unit with the given value.
     */
    public static CommonKilobyte valueOf( final Long numberOfBytes )
    {
        return valueOf( numberOfBytes.longValue() );
    }

    /**
     * @param numberOfBytes The amount of bytes the Kilobyte contains.
     *
     * @return A new Kilobyte unit with the given value.
     */
    public static CommonKilobyte valueOf( final long numberOfBytes )
    {
        return valueOf( BigInteger.valueOf( numberOfBytes ) );
    }

    /**
     * @param numberOfBytes The amount of bytes the Kilobyte contains.
     *
     * @return A new Kilobyte unit with the given value.
     */
    public static CommonKilobyte valueOf( final BigInteger numberOfBytes )
    {
        return new CommonKilobyte( numberOfBytes );
    }

    @Override
    protected Function<BigInteger, StorageUnit<?>> converter()
    {
        return StorageUnits:: commonValueOf;
    }

    @Override
    protected BigInteger getNumberOfBytesPerUnit()
    {
        return StorageUnit.BYTES_IN_A_KIBIBYTE;
    }

    @Override
    protected String getSymbol()
    {
        return "kB";
    }

    @Override
    public CommonKilobyte add( final long bytesToAdd )
    {
        return new CommonKilobyte( bytes.add( BigInteger.valueOf( bytesToAdd ) ) );
    }

    @Override
    public CommonKilobyte add( final StorageUnit<?> storageAmount )
    {
        return new CommonKilobyte( bytes.add( storageAmount.bytes ) );
    }

    @Override
    public CommonKilobyte divide( final long divisor )
    {
        return new CommonKilobyte( bytes.divide( BigInteger.valueOf( divisor ) ) );
    }

    @Override
    public CommonKilobyte multiply( final long factor )
    {
        return new CommonKilobyte( bytes.multiply( BigInteger.valueOf( factor ) ) );
    }

    @Override
    public CommonKilobyte subtract( final long bytesToSubtract )
    {
        return new CommonKilobyte( bytes.subtract( BigInteger.valueOf( bytesToSubtract ) ) );
    }

    @Override
    public CommonKilobyte subtract( final StorageUnit<?> storageAmount )
    {
        return new CommonKilobyte( bytes.subtract( storageAmount.bytes ) );
    }
}
