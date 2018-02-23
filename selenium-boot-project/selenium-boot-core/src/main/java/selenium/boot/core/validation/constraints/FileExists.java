package selenium.boot.core.validation.constraints;


import selenium.boot.core.validation.constraints.FileExists.FileExistsValidator;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;



/**
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 2.0
 */
@Target( { FIELD, METHOD, ANNOTATION_TYPE, PARAMETER } )
@Retention( RUNTIME )
@Constraint( validatedBy = FileExistsValidator.class )
@NotNull
@Documented
public @interface FileExists
{
    String message() default "{selenium.boot.validator.constraints.FileExists.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    class FileExistsValidator implements ConstraintValidator<FileExists,Path>
    {
        private Path testedPath;

        @Override
        public boolean isValid( Path path, ConstraintValidatorContext constraintValidatorContext )
        {
            if( path == null )
            {
                return false;
            }

            return Files.exists( path );
        }
    }
}
