package selenium.boot.utils.measure.storage;


import java.math.BigInteger;
import java.util.function.Function;



/**
 * Megabyte as commonly found in the wild (1 Megabyte = 1 048 576 Byte).
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 1.0
 */
public class CommonMegabyte extends StorageUnit<CommonMegabyte>
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    CommonMegabyte( final BigInteger bytes )
    {
        super( bytes );
    }

    //endregion 

    /**
     * @param numberOfBytes The amount of bytes the Megabyte contains.
     *
     * @return A new Megabyte unit with the given value.
     */

    public static CommonMegabyte valueOf( final BigInteger numberOfBytes )
    {
        return new CommonMegabyte( numberOfBytes );
    }

    /**
     * @param numberOfBytes The amount of bytes the Megabyte contains.
     *
     * @return A new Megabyte unit with the given value.
     */

    public static CommonMegabyte valueOf( final Long numberOfBytes )
    {
        return valueOf( numberOfBytes.longValue() );
    }

    /**
     * @param numberOfBytes The amount of bytes the Megabyte contains.
     *
     * @return A new Megabyte unit with the given value.
     */

    public static CommonMegabyte valueOf( final long numberOfBytes )
    {
        return new CommonMegabyte( BigInteger.valueOf( numberOfBytes ) );
    }

    @Override
    protected Function<BigInteger, StorageUnit<?>> converter()
    {
        return StorageUnits:: commonValueOf;
    }

    @Override
    protected BigInteger getNumberOfBytesPerUnit()
    {
        return StorageUnit.BYTES_IN_A_MEBIBYTE;
    }

    @Override
    protected String getSymbol()
    {
        return "MB";
    }

    @Override
    public CommonMegabyte add( final long bytesToAdd )
    {
        return new CommonMegabyte( bytes.add( BigInteger.valueOf( bytesToAdd ) ) );
    }

    @Override
    public CommonMegabyte add( final StorageUnit<?> storageAmount )
    {
        return new CommonMegabyte( bytes.add( storageAmount.bytes ) );
    }

    @Override
    public CommonMegabyte divide( final long divisor )
    {
        return new CommonMegabyte( bytes.divide( BigInteger.valueOf( divisor ) ) );
    }

    @Override
    public CommonMegabyte multiply( final long factor )
    {
        return new CommonMegabyte( bytes.multiply( BigInteger.valueOf( factor ) ) );
    }

    @Override
    public CommonMegabyte subtract( final long bytesToSubtract )
    {
        return new CommonMegabyte( bytes.subtract( BigInteger.valueOf( bytesToSubtract ) ) );
    }

    @Override
    public CommonMegabyte subtract( final StorageUnit<?> storageAmount )
    {
        return new CommonMegabyte( bytes.subtract( storageAmount.bytes ) );
    }

}
