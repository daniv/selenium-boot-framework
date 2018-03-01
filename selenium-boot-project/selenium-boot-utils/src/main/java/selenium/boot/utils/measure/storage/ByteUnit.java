package selenium.boot.utils.measure.storage;




import java.math.BigInteger;
import java.util.function.Function;



/**
 * Byte as specified in ISO IEC 80000-13:2008 ( 1 Byte ).
 * source: de.xn__ho_hia.storage_unit
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @see selenium.boot.utils.measure.storage.NullSafe
 * @since 1.0
 */
public class ByteUnit extends StorageUnit<ByteUnit>
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    ByteUnit( final BigInteger numberOfBytes )
    {
        super( numberOfBytes );
    }

    //endregion

    /**
     * @param numberOfBytes The amount of bytes the Byte contains.
     *
     * @return A new Byte unit with the given value.
     */
    public static ByteUnit valueOf( final Long numberOfBytes )
    {
        return valueOf( numberOfBytes.longValue() );
    }

    /**
     * @param numberOfBytes The amount of bytes the Byte contains.
     *
     * @return A new ByteUnit unit with the given value.
     */
    public static ByteUnit valueOf( final long numberOfBytes )
    {
        return valueOf( BigInteger.valueOf( numberOfBytes ) );
    }

    /**
     * @param numberOfBytes The amount of bytes the Byte contains.
     *
     * @return A new Byte unit with the given value.
     */
    public static ByteUnit valueOf( final BigInteger numberOfBytes )
    {
        return new ByteUnit( numberOfBytes );
    }

    @Override
    protected Function<BigInteger, StorageUnit<?>> converter()
    {
        return StorageUnits:: binaryValueOf;
    }

    @Override
    protected BigInteger getNumberOfBytesPerUnit()
    {
        return BigInteger.ONE;
    }

    @Override
    protected String getSymbol()
    {
        return "B";
    }

    @Override
    public ByteUnit add( final long bytesToAdd )
    {
        return new ByteUnit( bytes.add( BigInteger.valueOf( bytesToAdd ) ) );
    }

    @Override
    public ByteUnit add( final StorageUnit<?> storageAmount )
    {
        return new ByteUnit(  bytes.add( storageAmount.bytes ) );
    }

    @Override
    public ByteUnit divide( final long divisor )
    {
        return new ByteUnit(  bytes.divide( BigInteger.valueOf( divisor ) ) );
    }

    @Override
    public ByteUnit multiply( final long factor )
    {
        return new ByteUnit( bytes.multiply( BigInteger.valueOf( factor ) ) );
    }

    @Override
    public ByteUnit subtract( final long bytesToSubtract )
    {
        return new ByteUnit( bytes.subtract( BigInteger.valueOf( bytesToSubtract ) ) );
    }

    @Override
    public ByteUnit subtract( final StorageUnit<?> storageAmount )
    {
        return new ByteUnit( bytes.subtract( storageAmount.bytes ) );
    }
}
