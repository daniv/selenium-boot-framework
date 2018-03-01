package selenium.boot.utils.measure.storage;


import java.math.BigInteger;
import java.util.function.Function;



/**
 * Kilobyte as specified in ISO IEC 80000-13:2008 (1 Kilobyte = 1 000 Byte).
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 1.0
 */
public class Kilobyte extends StorageUnit<Kilobyte>
{
    //region initialization and constructors section

    Kilobyte( final BigInteger numberOfBytes )
    {
        super( numberOfBytes );
    }

    //endregion  

    /**
     * @param numberOfBytes The amount of bytes the Kilobyte contains.
     *
     * @return A new Kilobyte unit with the given value.
     */

    public static Kilobyte valueOf( final Long numberOfBytes )
    {
        return valueOf( numberOfBytes.longValue() );
    }

    /**
     * @param numberOfBytes The amount of bytes the Kilobyte contains.
     *
     * @return A new Kilobyte unit with the given value.
     */

    public static Kilobyte valueOf( final long numberOfBytes )
    {
        return valueOf( BigInteger.valueOf( numberOfBytes ) );
    }

    /**
     * @param numberOfBytes The amount of bytes the Kilobyte contains.
     *
     * @return A new Kilobyte unit with the given value.
     */

    public static Kilobyte valueOf( final BigInteger numberOfBytes )
    {
        return new Kilobyte( numberOfBytes );
    }

    @Override
    protected Function<BigInteger, StorageUnit<?>> converter()
    {
        return StorageUnits:: decimalValueOf;
    }

    @Override
    protected BigInteger getNumberOfBytesPerUnit()
    {
        return StorageUnit.BYTES_IN_A_KILOBYTE;
    }

    @Override
    protected String getSymbol()
    {
        return "kB";
    }

    @Override
    public Kilobyte add( final long bytesToAdd )
    {
        return new Kilobyte( bytes.add( BigInteger.valueOf( bytesToAdd ) ) );
    }

    @Override
    public Kilobyte add( final StorageUnit<?> storageAmount )
    {
        return new Kilobyte( bytes.add( storageAmount.bytes ) );
    }

    @Override
    public Kilobyte divide( final long divisor )
    {
        return new Kilobyte( bytes.divide( BigInteger.valueOf( divisor ) ) );
    }

    @Override
    public Kilobyte multiply( final long factor )
    {
        return new Kilobyte( bytes.multiply( BigInteger.valueOf( factor ) ) );
    }

    @Override
    public Kilobyte subtract( final long bytesToSubtract )
    {
        return new Kilobyte( bytes.subtract( BigInteger.valueOf( bytesToSubtract ) ) );
    }

    @Override
    public Kilobyte subtract( final StorageUnit<?> storageAmount )
    {
        return new Kilobyte( bytes.subtract( storageAmount.bytes ) );
    }
}
