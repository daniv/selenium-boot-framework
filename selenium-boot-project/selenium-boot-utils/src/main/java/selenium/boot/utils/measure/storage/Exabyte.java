package selenium.boot.utils.measure.storage;



import java.math.BigInteger;
import java.util.function.Function;



/**
 * Exabyte as specified in ISO IEC 80000-13:2008 (1 Exabyte = 1 000 000 000 000 000 000 Byte).
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @see selenium.boot.utils.measure.storage.NullSafe
 * @since 1.0
 */
public class Exabyte extends StorageUnit<Exabyte>
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    Exabyte( final BigInteger bytes )
    {
        super( bytes );
    }


    //endregion  

    /**
     * @param numberOfBytes The amount of bytes the Exabyte contains.
     *
     * @return A new ExabyteUnit unit with the given value.
     */
    public static Exabyte valueOf( final BigInteger numberOfBytes )
    {
        return new Exabyte( numberOfBytes );
    }

    /**
     * @param numberOfBytes The amount of bytes the Exabyte contains.
     *
     * @return A new Exabyte unit with the given value.
     */
    public static Exabyte valueOf( final Long numberOfBytes )
    {
        return valueOf( numberOfBytes.longValue() );
    }

    /**
     * @param numberOfBytes The amount of bytes the Exabyte contains.
     *
     * @return A new ExabyteUnit unit with the given value.
     */
    public static Exabyte valueOf( final long numberOfBytes )
    {
        return new Exabyte( BigInteger.valueOf( numberOfBytes ) );
    }

    @Override
    protected Function<BigInteger, StorageUnit<?>> converter()
    {
        return StorageUnits:: decimalValueOf;
    }

    @Override
    protected BigInteger getNumberOfBytesPerUnit()
    {
        return StorageUnit.BYTES_IN_A_EXABYTE;
    }

    @Override
    protected String getSymbol()
    {
        return "EB";
    }

    @Override
    public Exabyte add( final long bytesToAdd )
    {
        return new Exabyte( bytes.add( BigInteger.valueOf( bytesToAdd ) ) );
    }

    @Override
    public Exabyte add( final StorageUnit<?> storageAmount )
    {
        return new Exabyte( bytes.add( storageAmount.bytes ) );
    }

    @Override
    public Exabyte divide( final long divisor )
    {
        return new Exabyte( bytes.divide( BigInteger.valueOf( divisor ) ) );
    }

    @Override
    public Exabyte multiply( final long factor )
    {
        return new Exabyte( bytes.multiply( BigInteger.valueOf( factor ) ) );
    }

    @Override
    public Exabyte subtract( final long bytesToSubtract )
    {
        return new Exabyte( bytes.subtract( BigInteger.valueOf( bytesToSubtract ) ) );
    }

    @Override
    public Exabyte subtract( final StorageUnit<?> storageAmount )
    {
        return new Exabyte( bytes.subtract( storageAmount.bytes ) );
    }

}
