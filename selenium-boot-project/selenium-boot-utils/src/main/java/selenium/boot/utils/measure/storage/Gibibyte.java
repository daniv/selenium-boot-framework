package selenium.boot.utils.measure.storage;


import java.math.BigInteger;
import java.util.function.Function;



/**
 * Gibibyte as specified in ISO IEC 80000-13:2008 (1 Gibibyte = 1 073 741 824 Byte).
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 1.0
 */
public final class Gibibyte extends StorageUnit<Gibibyte>
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //--------------------------------------------------------------------

    Gibibyte( final BigInteger bytes )
    {
        super( bytes );
    }

    //endregion  

    /**
     * @param numberOfBytes The amount of bytes the Gibibyte contains.
     *
     * @return A new Gibibyte unit with the given value.
     */
    
    public static Gibibyte valueOf( final BigInteger numberOfBytes )
    {
        return new Gibibyte( numberOfBytes );
    }

    /**
     * @param numberOfBytes The amount of bytes the Gibibyte contains.
     *
     * @return A new Gibibyte unit with the given value.
     */
    
    public static Gibibyte valueOf( final Long numberOfBytes )
    {
        return valueOf( numberOfBytes.longValue() );
    }

    /**
     * @param numberOfBytes The amount of bytes the Gibibyte contains.
     *
     * @return A new Gibibyte unit with the given value.
     */
    
    public static Gibibyte valueOf( final long numberOfBytes )
    {
        return new Gibibyte( BigInteger.valueOf( numberOfBytes ) );
    }

    @Override
    protected Function< BigInteger,  StorageUnit<?>> converter()
    {
        return StorageUnits:: binaryValueOf;
    }

    @Override
    protected BigInteger getNumberOfBytesPerUnit()
    {
        return StorageUnit.BYTES_IN_A_GIBIBYTE;
    }

    @Override
    protected String getSymbol()
    {
        return "GiB";
    }

    @Override
    public Gibibyte add( final long bytesToAdd )
    {
        return new Gibibyte( bytes.add( BigInteger.valueOf( bytesToAdd ) ) );
    }

    @Override
    public Gibibyte add( final StorageUnit<?> storageAmount )
    {
        return new Gibibyte( bytes.add( storageAmount.bytes ) );
    }

    @Override
    public Gibibyte divide( final long divisor )
    {
        return new Gibibyte( bytes.divide( BigInteger.valueOf( divisor ) ) );
    }

    @Override
    public Gibibyte multiply( final long factor )
    {
        return new Gibibyte( bytes.multiply( BigInteger.valueOf( factor ) ) );
    }

    @Override
    public Gibibyte subtract( final long bytesToSubtract )
    {
        return new Gibibyte( bytes.subtract( BigInteger.valueOf( bytesToSubtract ) ) );
    }

    @Override
    public Gibibyte subtract( final StorageUnit<?> storageAmount )
    {
        return new Gibibyte( bytes.subtract( storageAmount.bytes ) );
    }
}
