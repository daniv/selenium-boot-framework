package selenium.boot.utils.measure.storage;


import java.math.BigInteger;
import java.util.function.Function;



/**
 * Mebibyte as specified in ISO IEC 80000-13:2008 (1 Mebibyte = 1 048 576 Byte).
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 1.0
 */
public final class Mebibyte extends StorageUnit<Mebibyte>
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    Mebibyte( final BigInteger bytes )
    {
        super( bytes );
    }

    //endregion

    /**
     * @param numberOfBytes The amount of bytes the Mebibyte contains.
     *
     * @return A new Mebibyte unit with the given value.
     */

    public static Mebibyte valueOf( final BigInteger numberOfBytes )
    {
        return new Mebibyte( numberOfBytes );
    }

    /**
     * @param numberOfBytes The amount of bytes the Mebibyte contains.
     *
     * @return A new Mebibyte unit with the given value.
     */

    public static Mebibyte valueOf( final Long numberOfBytes )
    {
        return valueOf( numberOfBytes.longValue() );
    }

    /**
     * @param numberOfBytes The amount of bytes the Mebibyte contains.
     *
     * @return A new Mebibyte unit with the given value.
     */

    public static Mebibyte valueOf( final long numberOfBytes )
    {
        return new Mebibyte( BigInteger.valueOf( numberOfBytes ) );
    }

    @Override
    protected Function<BigInteger, StorageUnit<?>> converter()
    {
        return StorageUnits:: binaryValueOf;
    }

    @Override
    protected BigInteger getNumberOfBytesPerUnit()
    {
        return StorageUnit.BYTES_IN_A_MEBIBYTE;
    }

    @Override
    protected String getSymbol()
    {
        return "MiB";
    }

    @Override
    public Mebibyte add( final long bytesToAdd )
    {
        return new Mebibyte( bytes.add( BigInteger.valueOf( bytesToAdd ) ) );
    }

    @Override
    public Mebibyte add( final StorageUnit<?> storageAmount )
    {
        return new Mebibyte( bytes.add( storageAmount.bytes ) );
    }

    @Override
    public Mebibyte divide( final long divisor )
    {
        return new Mebibyte( bytes.divide( BigInteger.valueOf( divisor ) ) );
    }

    @Override
    public Mebibyte multiply( final long factor )
    {
        return new Mebibyte( bytes.multiply( BigInteger.valueOf( factor ) ) );
    }

    @Override
    public Mebibyte subtract( final long bytesToSubtract )
    {
        return new Mebibyte( bytes.subtract( BigInteger.valueOf( bytesToSubtract ) ) );
    }

    @Override
    public Mebibyte subtract( final StorageUnit<?> storageAmount )
    {
        return new Mebibyte( bytes.subtract( storageAmount.bytes ) );
    }

}
