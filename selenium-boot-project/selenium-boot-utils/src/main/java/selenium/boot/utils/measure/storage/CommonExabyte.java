package selenium.boot.utils.measure.storage;


import java.math.BigInteger;
import java.util.function.Function;



/**
 * Exabyte as commonly found in the wild (1 Exabyte = 1 152 921 504 606 846 976 Byte).
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 2.0
 */
public final class CommonExabyte extends StorageUnit<CommonExabyte>
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    CommonExabyte( final BigInteger bytes )
    {
        super( bytes );
    }

    //endregion  

    /**
     * @param numberOfBytes The amount of bytes the Exabyte contains.
     *
     * @return A new Exabyte unit with the given value.
     */

    public static CommonExabyte valueOf( final BigInteger numberOfBytes )
    {
        return new CommonExabyte( numberOfBytes );
    }

    /**
     * @param numberOfBytes The amount of bytes the Exabyte contains.
     *
     * @return A new Exabyte unit with the given value.
     */

    public static CommonExabyte valueOf( final Long numberOfBytes )
    {
        return valueOf( numberOfBytes.longValue() );
    }

    /**
     * @param numberOfBytes The amount of bytes the Exabyte contains.
     *
     * @return A new Exabyte unit with the given value.
     */

    public static CommonExabyte valueOf( final long numberOfBytes )
    {
        return new CommonExabyte( BigInteger.valueOf( numberOfBytes ) );
    }

    @Override
    protected Function<BigInteger, StorageUnit<?>> converter()
    {
        return StorageUnits:: commonValueOf;
    }

    @Override
    protected BigInteger getNumberOfBytesPerUnit()
    {
        return StorageUnit.BYTES_IN_A_EXBIBYTE;
    }

    @Override
    protected String getSymbol()
    {
        return "EB";
    }

    @Override
    public CommonExabyte add( final long bytesToAdd )
    {
        return new CommonExabyte( bytes.add( BigInteger.valueOf( bytesToAdd ) ) );
    }

    @Override
    public CommonExabyte add( final StorageUnit<?> storageAmount )
    {
        return new CommonExabyte( bytes.add( storageAmount.bytes ) );
    }

    @Override
    public CommonExabyte divide( final long divisor )
    {
        return new CommonExabyte( bytes.divide( BigInteger.valueOf( divisor ) ) );
    }

    @Override
    public CommonExabyte multiply( final long factor )
    {
        return new CommonExabyte( bytes.multiply( BigInteger.valueOf( factor ) ) );
    }

    @Override
    public CommonExabyte subtract( final long bytesToSubtract )
    {
        return new CommonExabyte( bytes.subtract( BigInteger.valueOf( bytesToSubtract ) ) );
    }

    @Override
    public CommonExabyte subtract( final StorageUnit<?> storageAmount )
    {
        return new CommonExabyte( bytes.subtract( storageAmount.bytes ) );
    }
}
