package selenium.boot.utils.measure.storage;


import java.math.BigInteger;
import java.util.function.Function;



/**
 * Gigabyte as specified in ISO IEC 80000-13:2008 (1 Gigabyte = 1 000 000 000 Byte).
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 2.0
 */
public class Gigabyte extends StorageUnit<Gigabyte>
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    Gigabyte( final BigInteger bytes )
    {
        super( bytes );
    }

    //endregion  

    /**
     * @param numberOfBytes The amount of bytes the Gigabyte contains.
     *
     * @return A new GigabyteUnit unit with the given value.
     */

    public static Gigabyte valueOf( final BigInteger numberOfBytes )
    {
        return new Gigabyte( numberOfBytes );
    }

    /**
     * @param numberOfBytes The amount of bytes the Gigabyte contains.
     *
     * @return A new GigabyteUnit unit with the given value.
     */

    public static Gigabyte valueOf( final Long numberOfBytes )
    {
        return valueOf( numberOfBytes.longValue() );
    }

    /**
     * @param numberOfBytes The amount of bytes the Gigabyte contains.
     *
     * @return A new GigabyteUnit unit with the given value.
     */

    public static Gigabyte valueOf( final long numberOfBytes )
    {
        return new Gigabyte( BigInteger.valueOf( numberOfBytes ) );
    }

    @Override
    protected Function<BigInteger, StorageUnit<?>> converter()
    {
        return StorageUnits:: decimalValueOf;
    }

    @Override
    protected BigInteger getNumberOfBytesPerUnit()
    {
        return StorageUnit.BYTES_IN_A_GIGABYTE;
    }

    @Override
    protected String getSymbol()
    {
        return "GB";
    }

    @Override
    public Gigabyte add( final long bytesToAdd )
    {
        return new Gigabyte( bytes.add( BigInteger.valueOf( bytesToAdd ) ) );
    }

    @Override
    public Gigabyte add( final StorageUnit<?> storageAmount )
    {
        return new Gigabyte( bytes.add( storageAmount.bytes ) );
    }

    @Override
    public Gigabyte divide( final long divisor )
    {
        return new Gigabyte( bytes.divide( BigInteger.valueOf( divisor ) ) );
    }

    @Override
    public Gigabyte multiply( final long factor )
    {
        return new Gigabyte( bytes.multiply( BigInteger.valueOf( factor ) ) );
    }

    @Override
    public Gigabyte subtract( final long bytesToSubtract )
    {
        return new Gigabyte( bytes.subtract( BigInteger.valueOf( bytesToSubtract ) ) );
    }

    @Override
    public Gigabyte subtract( final StorageUnit<?> storageAmount )
    {
        return new Gigabyte( bytes.subtract( storageAmount.bytes ) );
    }
}
