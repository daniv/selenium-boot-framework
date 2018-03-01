package selenium.boot.utils.measure.storage;


import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.util.Locale;



/**
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 2.0
 */
public class UnitFormat
{
    //region initialization and constructors section

    private UnitFormat()
    {
        super();
    }

    //endregion

    static Format asFormat( final String pattern, final Locale locale )
    {
        final NumberFormat localizedFormat = NumberFormat.getNumberInstance( locale );
        final DecimalFormat outputFormat = ( DecimalFormat ) localizedFormat;
        outputFormat.applyPattern( pattern );
        return outputFormat;
    }
}
