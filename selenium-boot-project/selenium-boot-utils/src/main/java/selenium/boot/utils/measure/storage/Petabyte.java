package selenium.boot.utils.measure.storage;


import java.math.BigInteger;
import java.util.function.Function;



/**
 * Petabyte as specified in ISO IEC 80000-13:2008 (1 Petabyte = 1 000 000 000 000 000 Byte).
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 1.0
 */
public class Petabyte extends StorageUnit<Petabyte>
{
    //region initialization and constructors section

    Petabyte( final BigInteger bytes )
    {
        super( bytes );
    }

    //endregion  

    /**
     * @param numberOfBytes The amount of bytes the Petabyte contains.
     *
     * @return A new Petabyte unit with the given value.
     */

    public static Petabyte valueOf( final BigInteger numberOfBytes )
    {
        return new Petabyte( numberOfBytes );
    }

    /**
     * @param numberOfBytes The amount of bytes the Petabyte contains.
     *
     * @return A new Petabyte unit with the given value.
     */

    public static Petabyte valueOf( final Long numberOfBytes )
    {
        return valueOf( numberOfBytes.longValue() );
    }

    /**
     * @param numberOfBytes The amount of bytes the Petabyte contains.
     *
     * @return A new Petabyte unit with the given value.
     */

    public static Petabyte valueOf( final long numberOfBytes )
    {
        return new Petabyte( BigInteger.valueOf( numberOfBytes ) );
    }

    @Override
    protected Function<BigInteger, StorageUnit<?>> converter()
    {
        return StorageUnits:: decimalValueOf;
    }

    @Override
    protected BigInteger getNumberOfBytesPerUnit()
    {
        return StorageUnit.BYTES_IN_A_PETABYTE;
    }

    @Override
    protected String getSymbol()
    {
        return "PB";
    }

    @Override
    public Petabyte add( final long bytesToAdd )
    {
        return new Petabyte( bytes.add( BigInteger.valueOf( bytesToAdd ) ) );
    }

    @Override
    public Petabyte add( final StorageUnit<?> storageAmount )
    {
        return new Petabyte( bytes.add( storageAmount.bytes ) );
    }

    @Override
    public Petabyte divide( final long divisor )
    {
        return new Petabyte( bytes.divide( BigInteger.valueOf( divisor ) ) );
    }

    @Override
    public Petabyte multiply( final long factor )
    {
        return new Petabyte( bytes.multiply( BigInteger.valueOf( factor ) ) );
    }

    @Override
    public Petabyte subtract( final long bytesToSubtract )
    {
        return new Petabyte( bytes.subtract( BigInteger.valueOf( bytesToSubtract ) ) );
    }

    @Override
    public Petabyte subtract( final StorageUnit<?> storageAmount )
    {
        return new Petabyte( bytes.subtract( storageAmount.bytes ) );
    }
}
