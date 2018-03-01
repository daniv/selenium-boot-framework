package selenium.boot.utils.measure.storage;


import java.math.BigInteger;
import java.util.function.Function;



/**
 * Zettabyte as specified in ISO IEC 80000-13:2008 (1 Zettabyte = 1 000 000 000 000 000 000 000 Byte).
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 2.0
 */
public final class Zettabyte extends StorageUnit<Zettabyte>
{
    //region initialization and constructors section

    Zettabyte( final BigInteger bytes )
    {
        super( bytes );
    }


    //endregion  

    /**
     * @param numberOfBytes The amount of bytes the Zettabyte contains.
     *
     * @return A new Zettabyte unit with the given value.
     */

    public static Zettabyte valueOf( final BigInteger numberOfBytes )
    {
        return new Zettabyte( numberOfBytes );
    }

    /**
     * @param numberOfBytes The amount of bytes the Zettabyte contains.
     *
     * @return A new Zettabyte unit with the given value.
     */

    public static Zettabyte valueOf( final Long numberOfBytes )
    {
        return valueOf( numberOfBytes.longValue() );
    }

    /**
     * @param numberOfBytes The amount of bytes the Zettabyte contains.
     *
     * @return A new Zettabyte unit with the given value.
     */

    public static Zettabyte valueOf( final long numberOfBytes )
    {
        return new Zettabyte( BigInteger.valueOf( numberOfBytes ) );
    }

    @Override
    protected Function<BigInteger, StorageUnit<?>> converter()
    {
        return StorageUnits:: decimalValueOf;
    }

    @Override
    protected BigInteger getNumberOfBytesPerUnit()
    {
        return StorageUnit.BYTES_IN_A_ZETTABYTE;
    }

    @Override
    protected String getSymbol()
    {
        return "ZB"; //$NON-NLS-1$
    }

    @Override
    public Zettabyte add( final long bytesToAdd )
    {
        return new Zettabyte( bytes.add( BigInteger.valueOf( bytesToAdd ) ) );
    }

    @Override
    public Zettabyte add( final StorageUnit<?> storageAmount )
    {
        return new Zettabyte( bytes.add( storageAmount.bytes ) );
    }

    @Override
    public Zettabyte divide( final long divisor )
    {
        return new Zettabyte( bytes.divide( BigInteger.valueOf( divisor ) ) );
    }

    @Override
    public Zettabyte multiply( final long factor )
    {
        return new Zettabyte( bytes.multiply( BigInteger.valueOf( factor ) ) );
    }

    @Override
    public Zettabyte subtract( final long bytesToSubtract )
    {
        return new Zettabyte( bytes.subtract( BigInteger.valueOf( bytesToSubtract ) ) );
    }

    @Override
    public Zettabyte subtract( final StorageUnit<?> storageAmount )
    {
        return new Zettabyte( bytes.subtract( storageAmount.bytes ) );
    }

}
