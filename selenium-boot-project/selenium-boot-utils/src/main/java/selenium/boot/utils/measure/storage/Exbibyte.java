package selenium.boot.utils.measure.storage;


import java.math.BigInteger;
import java.util.function.Function;



/**
 * Exbibyte as specified in ISO IEC 80000-13:2008 (1 Exbibyte = 1 152 921 504 606 846 976 Byte).
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 2.0
 */
public class Exbibyte extends StorageUnit<Exbibyte>
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    Exbibyte( final BigInteger bytes )
    {
        super( bytes );
    }

    //endregion  


    /**
     * @param numberOfBytes The amount of bytes the Exbibyte contains.
     *
     * @return A new Exbibyte unit with the given value.
     */

    public static Exbibyte valueOf( final BigInteger numberOfBytes )
    {
        return new Exbibyte( numberOfBytes );
    }

    /**
     * @param numberOfBytes The amount of bytes the ExbibyteUnit contains.
     *
     * @return A new ExbibyteUnit unit with the given value.
     */

    public static Exbibyte valueOf( final Long numberOfBytes )
    {
        return valueOf( numberOfBytes.longValue() );
    }

    /**
     * @param numberOfBytes The amount of bytes the ExbibyteUnit contains.
     *
     * @return A new ExbibyteUnit unit with the given value.
     */

    public static Exbibyte valueOf( final long numberOfBytes )
    {
        return new Exbibyte( BigInteger.valueOf( numberOfBytes ) );
    }

    @Override
    protected Function<BigInteger, StorageUnit<?>> converter()
    {
        return StorageUnits:: binaryValueOf;
    }

    @Override
    protected BigInteger getNumberOfBytesPerUnit()
    {
        return StorageUnit.BYTES_IN_A_EXBIBYTE;
    }

    @Override
    protected String getSymbol()
    {
        return "EiB";
    }

    @Override
    public Exbibyte add( final long bytesToAdd )
    {
        return new Exbibyte( bytes.add( BigInteger.valueOf( bytesToAdd ) ) );
    }

    @Override
    public Exbibyte add( final StorageUnit<?> storageAmount )
    {
        return new Exbibyte( bytes.add( storageAmount.bytes ) );
    }

    @Override
    public Exbibyte divide( final long divisor )
    {
        return new Exbibyte( bytes.divide( BigInteger.valueOf( divisor ) ) );
    }

    @Override
    public Exbibyte multiply( final long factor )
    {
        return new Exbibyte( bytes.multiply( BigInteger.valueOf( factor ) ) );
    }

    @Override
    public Exbibyte subtract( final long bytesToSubtract )
    {
        return new Exbibyte( bytes.subtract( BigInteger.valueOf( bytesToSubtract ) ) );
    }

    @Override
    public Exbibyte subtract( final StorageUnit<?> storageAmount )
    {
        return new Exbibyte( bytes.subtract( storageAmount.bytes ) );
    }
}
