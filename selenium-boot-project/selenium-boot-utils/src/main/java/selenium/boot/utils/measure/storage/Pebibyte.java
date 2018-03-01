package selenium.boot.utils.measure.storage;


import java.math.BigInteger;
import java.util.function.Function;



/**
 * Pebibyte as specified in ISO IEC 80000-13:2008 (1 Pebibyte = 1 125 899 906 842 624 Byte).
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 1.0
 */
public final class Pebibyte extends StorageUnit<Pebibyte>
{
    //region initialization and constructors section

    Pebibyte( final BigInteger bytes )
    {
        super( bytes );
    }


    //endregion

    /**
     * @param numberOfBytes The amount of bytes the Pebibyte contains.
     *
     * @return A new Pebibyte unit with the given value.
     */

    public static Pebibyte valueOf( final BigInteger numberOfBytes )
    {
        return new Pebibyte( numberOfBytes );
    }

    /**
     * @param numberOfBytes The amount of bytes the Pebibyte contains.
     *
     * @return A new Pebibyte unit with the given value.
     */

    public static Pebibyte valueOf( final Long numberOfBytes )
    {
        return valueOf( numberOfBytes.longValue() );
    }

    /**
     * @param numberOfBytes The amount of bytes the Pebibyte contains.
     *
     * @return A new Pebibyte unit with the given value.
     */

    public static Pebibyte valueOf( final long numberOfBytes )
    {
        return new Pebibyte( BigInteger.valueOf( numberOfBytes ) );
    }

    @Override
    protected Function<BigInteger, StorageUnit<?>> converter()
    {
        return StorageUnits:: binaryValueOf;
    }

    @Override
    protected BigInteger getNumberOfBytesPerUnit()
    {
        return StorageUnit.BYTES_IN_A_PEBIBYTE;
    }

    @Override
    protected String getSymbol()
    {
        return "PiB";
    }

    @Override
    public Pebibyte add( final long bytesToAdd )
    {
        return new Pebibyte( bytes.add( BigInteger.valueOf( bytesToAdd ) ) );
    }

    @Override
    public Pebibyte add( final StorageUnit<?> storageAmount )
    {
        return new Pebibyte( bytes.add( storageAmount.bytes ) );
    }

    @Override
    public Pebibyte divide( final long divisor )
    {
        return new Pebibyte( bytes.divide( BigInteger.valueOf( divisor ) ) );
    }

    @Override
    public Pebibyte multiply( final long factor )
    {
        return new Pebibyte( bytes.multiply( BigInteger.valueOf( factor ) ) );
    }

    @Override
    public Pebibyte subtract( final long bytesToSubtract )
    {
        return new Pebibyte( bytes.subtract( BigInteger.valueOf( bytesToSubtract ) ) );
    }

    @Override
    public Pebibyte subtract( final StorageUnit<?> storageAmount )
    {
        return new Pebibyte( bytes.subtract( storageAmount.bytes ) );
    }

}
