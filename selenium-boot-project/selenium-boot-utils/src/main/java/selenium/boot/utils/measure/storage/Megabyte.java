package selenium.boot.utils.measure.storage;


import java.math.BigInteger;
import java.util.function.Function;



/**
 * Megabyte as specified in ISO IEC 80000-13:2008 (1 Megabyte = 1 000 000 Byte).
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 2.0
 */
public final class Megabyte extends StorageUnit<Megabyte>
{
    //region initialization and constructors section

    Megabyte( final BigInteger bytes )
    {
        super( bytes );
    }

    //endregion  

    /**
     * @param numberOfBytes The amount of bytes the Megabyte contains.
     *
     * @return A new Megabyte unit with the given value.
     */
    
    public static Megabyte valueOf( final BigInteger numberOfBytes )
    {
        return new Megabyte( numberOfBytes );
    }

    /**
     * @param numberOfBytes The amount of bytes the Megabyte contains.
     *
     * @return A new Megabyte unit with the given value.
     */
    
    public static Megabyte valueOf( final Long numberOfBytes )
    {
        return valueOf( numberOfBytes.longValue() );
    }

    /**
     * @param numberOfBytes The amount of bytes the Megabyte contains.
     *
     * @return A new Megabyte unit with the given value.
     */
    
    public static Megabyte valueOf( final long numberOfBytes )
    {
        return new Megabyte( BigInteger.valueOf( numberOfBytes ) );
    }

    @Override
    protected Function< BigInteger,  StorageUnit<?>> converter()
    {
        return StorageUnits:: decimalValueOf;
    }

    @Override
    protected BigInteger getNumberOfBytesPerUnit()
    {
        return StorageUnit.BYTES_IN_A_MEGABYTE;
    }

    @Override
    protected String getSymbol()
    {
        return "MB";
    }

    @Override
    public Megabyte add( final long bytesToAdd )
    {
        return new Megabyte( bytes.add( BigInteger.valueOf( bytesToAdd ) ) );
    }

    @Override
    public Megabyte add( final StorageUnit<?> storageAmount )
    {
        return new Megabyte( bytes.add( storageAmount.bytes ) );
    }

    @Override
    public Megabyte divide( final long divisor )
    {
        return new Megabyte( bytes.divide( BigInteger.valueOf( divisor ) ) );
    }

    @Override
    public Megabyte multiply( final long factor )
    {
        return new Megabyte( bytes.multiply( BigInteger.valueOf( factor ) ) );
    }

    @Override
    public Megabyte subtract( final long bytesToSubtract )
    {
        return new Megabyte( bytes.subtract( BigInteger.valueOf( bytesToSubtract ) ) );
    }

    @Override
    public Megabyte subtract( final StorageUnit<?> storageAmount )
    {
        return new Megabyte( bytes.subtract( storageAmount.bytes ) );
    }
}
