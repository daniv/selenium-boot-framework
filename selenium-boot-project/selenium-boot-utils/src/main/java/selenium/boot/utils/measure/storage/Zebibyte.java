package selenium.boot.utils.measure.storage;


import java.math.BigInteger;
import java.util.function.Function;



/**
 * Zebibyte as specified in ISO IEC 80000-13:2008 (1 Zebibyte = 1 180 591 620 717 411 303 424 Byte).
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 2.0
 */
public final class Zebibyte extends StorageUnit<Zebibyte>
{
    //region initialization and constructors section

    Zebibyte( final BigInteger bytes )
    {
        super( bytes );
    }


    //endregion  

    /**
     * @param numberOfBytes The amount of bytes the Zebibyte contains.
     *
     * @return A new Zebibyte unit with the given value.
     */

    public static Zebibyte valueOf( final BigInteger numberOfBytes )
    {
        return new Zebibyte( numberOfBytes );
    }

    /**
     * @param numberOfBytes The amount of bytes the Zebibyte contains.
     *
     * @return A new Zebibyte unit with the given value.
     */

    public static Zebibyte valueOf( final Long numberOfBytes )
    {
        return valueOf( numberOfBytes.longValue() );
    }

    /**
     * @param numberOfBytes The amount of bytes the Zebibyte contains.
     *
     * @return A new Zebibyte unit with the given value.
     */

    public static Zebibyte valueOf( final long numberOfBytes )
    {
        return new Zebibyte( BigInteger.valueOf( numberOfBytes ) );
    }

    @Override
    protected Function<BigInteger, StorageUnit<?>> converter()
    {
        return StorageUnits:: binaryValueOf;
    }

    @Override
    protected BigInteger getNumberOfBytesPerUnit()
    {
        return StorageUnit.BYTES_IN_A_ZEBIBYTE;
    }

    @Override
    protected String getSymbol()
    {
        return "ZiB";
    }

    @Override
    public Zebibyte add( final long bytesToAdd )
    {
        return new Zebibyte( bytes.add( BigInteger.valueOf( bytesToAdd ) ) );
    }

    @Override
    public Zebibyte add( final StorageUnit<?> storageAmount )
    {
        return new Zebibyte( bytes.add( storageAmount.bytes ) );
    }

    @Override
    public Zebibyte divide( final long divisor )
    {
        return new Zebibyte( bytes.divide( BigInteger.valueOf( divisor ) ) );
    }

    @Override
    public Zebibyte multiply( final long factor )
    {
        return new Zebibyte( bytes.multiply( BigInteger.valueOf( factor ) ) );
    }

    @Override
    public Zebibyte subtract( final long bytesToSubtract )
    {
        return new Zebibyte( bytes.subtract( BigInteger.valueOf( bytesToSubtract ) ) );
    }

    @Override
    public Zebibyte subtract( final StorageUnit<?> storageAmount )
    {
        return new Zebibyte( bytes.subtract( storageAmount.bytes ) );
    }
}
