package selenium.boot.utils.measure.storage;


import java.math.BigInteger;
import java.util.function.Function;



/**
 * Kibibyte as specified in ISO IEC 80000-13:2008 (1 Kibibyte = 1 024 Byte).
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 1.0
 */
public final class Kibibyte extends StorageUnit<Kibibyte>
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    Kibibyte( final BigInteger bytes )
    {
        super( bytes );
    }
    
    //endregion 

    /**
     * @param numberOfBytes The amount of bytes the Kibibyte contains.
     *
     * @return A new Kibibyte unit with the given value.
     */

    public static Kibibyte valueOf( final BigInteger numberOfBytes )
    {
        return new Kibibyte( numberOfBytes );
    }

    /**
     * @param numberOfBytes The amount of bytes the Kibibyte contains.
     *
     * @return A new Kibibyte unit with the given value.
     */

    public static Kibibyte valueOf( final long numberOfBytes )
    {
        return valueOf( BigInteger.valueOf( numberOfBytes ) );
    }

    /**
     * @param numberOfBytes The amount of bytes the Kibibyte contains.
     *
     * @return A new Kibibyte unit with the given value.
     */

    public static Kibibyte valueOf( final Long numberOfBytes )
    {
        return valueOf( numberOfBytes.longValue() );
    }

    @Override
    protected Function<BigInteger, StorageUnit<?>> converter()
    {
        return StorageUnits:: binaryValueOf;
    }

    @Override
    protected BigInteger getNumberOfBytesPerUnit()
    {
        return StorageUnit.BYTES_IN_A_KIBIBYTE;
    }

    @Override
    protected String getSymbol()
    {
        return "KiB";
    }

    @Override
    public Kibibyte add( final long bytesToAdd )
    {
        return new Kibibyte( bytes.add( BigInteger.valueOf( bytesToAdd ) ) );
    }

    @Override
    public Kibibyte add( final StorageUnit<?> storageAmount )
    {
        return new Kibibyte( bytes.add( storageAmount.bytes ) );
    }

    @Override
    public Kibibyte divide( final long divisor )
    {
        return new Kibibyte( bytes.divide( BigInteger.valueOf( divisor ) ) );
    }

    @Override
    public Kibibyte multiply( final long factor )
    {
        return new Kibibyte( bytes.multiply( BigInteger.valueOf( factor ) ) );
    }

    @Override
    public Kibibyte subtract( final long bytesToSubtract )
    {
        return new Kibibyte( bytes.subtract( BigInteger.valueOf( bytesToSubtract ) ) );
    }

    @Override
    public Kibibyte subtract( final StorageUnit<?> storageAmount )
    {
        return new Kibibyte( bytes.subtract( storageAmount.bytes ) );
    }
}
