package selenium.boot.utils.measure.storage;



import java.math.BigInteger;
import java.util.function.Function;



/**
 * Yottabyte as commonly found in the wild (1 Yottabyte = 1 208 925 819 614 629 174 706 176 Byte).
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 2.0
 */
public final class CommonYottabyte extends StorageUnit<CommonYottabyte>
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    CommonYottabyte( final BigInteger bytes )
    {
        super( bytes );
    }

    //endregion 

    /**
     * @param numberOfBytes The amount of bytes the Yottabyte contains.
     *
     * @return A new Yottabyte unit with the given value.
     */
    public static CommonYottabyte valueOf( final BigInteger numberOfBytes )
    {
        return new CommonYottabyte( numberOfBytes );
    }

    /**
     * @param numberOfBytes The amount of bytes the Yottabyte contains.
     *
     * @return A new Yottabyte unit with the given value.
     */

    public static CommonYottabyte valueOf( final Long numberOfBytes )
    {
        return valueOf( numberOfBytes.longValue() );
    }

    /**
     * @param numberOfBytes The amount of bytes the Yottabyte contains.
     *
     * @return A new Yottabyte unit with the given value.
     */
    public static CommonYottabyte valueOf( final long numberOfBytes )
    {
        return new CommonYottabyte( BigInteger.valueOf( numberOfBytes ) );
    }

    @Override
    protected Function<BigInteger, StorageUnit<?>> converter()
    {
        return StorageUnits:: commonValueOf;
    }

    @Override
    protected BigInteger getNumberOfBytesPerUnit()
    {
        return StorageUnit.BYTES_IN_A_YOBIBYTE;
    }

    @Override
    protected String getSymbol()
    {
        return "YB";
    }

    @Override
    public CommonYottabyte add( final long bytesToAdd )
    {
        return new CommonYottabyte( bytes.add( BigInteger.valueOf( bytesToAdd ) ) );
    }

    @Override
    public CommonYottabyte add( final StorageUnit<?> storageAmount )
    {
        return new CommonYottabyte( bytes.add( storageAmount.bytes ) );
    }

    @Override
    public CommonYottabyte divide( final long divisor )
    {
        return new CommonYottabyte( bytes.divide( BigInteger.valueOf( divisor ) ) );
    }

    @Override
    public CommonYottabyte multiply( final long factor )
    {
        return new CommonYottabyte( bytes.multiply( BigInteger.valueOf( factor ) ) );
    }

    @Override
    public CommonYottabyte subtract( final long bytesToSubtract )
    {
        return new CommonYottabyte( bytes.subtract( BigInteger.valueOf( bytesToSubtract ) ) );
    }

    @Override
    public CommonYottabyte subtract( final StorageUnit<?> storageAmount )
    {
        return new CommonYottabyte( bytes.subtract( storageAmount.bytes ) );
    }

}
