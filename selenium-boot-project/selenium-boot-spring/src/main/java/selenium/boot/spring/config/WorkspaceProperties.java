package selenium.boot.spring.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Description;
import selenium.boot.spring.context.StaticApplicationContextHolder;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Set;



/**
 * Workspace properties that provides paths locations to the framework used directories.
 * the class annotated as {@link org.springframework.boot.context.properties.ConfigurationProperties} with prefix {@code workspace}
 *
 * uses {@code @Validated annotation} Variant of JSR-303's Valid,
 * supporting the specification of validation groups. Designed for convenient use with Spring's JSR-303 support but not JSR-303 specific.
 *
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 1.0
 */
@ConfigurationProperties( value = "workspace", ignoreInvalidFields = true )
@Description( "Properties for all the locations used by the framework." )
public class WorkspaceProperties implements InitializingBean
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger( WorkspaceProperties.class.getName() );

    /**
     * this constants defines the environment variable for the HOME directory
     */
    public static final String HOME_DIR_ENV_NAME = "SELENIUM_BOOT_HOME";

    /**
     * this property indicates the location the home projects and external configurations folder.
     */
    public static final String HOME_DIR_PROPERTY = "root.home";

    /**
     *  this property indicates the location the executables drivers  configurations folder.
     */
    public static final String DRIVERS_DIR_PROPERTY  = "workspace.drivers-directory";

    /**
     *  this property indicates the location the browsers extensions configurations folder.
     */
    public static final String EXTENSIONS_DIR_PROPERTY  = "workspace.extensions-directory";

    /**
     *  this property indicates the location the report resources folder.
     */
    public static final String RESOURCES_DIR_PROPERTY  = "workspace.resources-directory";

    /**
     *  this property indicates the location the logs folder.
     */
    public static final String LOGS_DIR_PROPERTY = "workspace.test-logs-directory";

    /**
     *  this property indicates the location the temporary folder.
     */
    public static final String TEMP_DIR_PROPERTY = "workspace.temp-directory";

    /**
     *  this property indicates the location the downloads folder.
     */
    public static final String DOWNLOADS_DIR_PROPERTY = "workspace.downloads-directory";

    /**
     *  this property indicates the location the projects folder.
     */
    public static final String PROJECTS_DIR_PROPERTY = "workspace.projects-directory";

    /**
     *  this property indicates the location the current project folder.
     */
    public static final String CURRENT_PROJECT_DIR_PROPERTY = "workspace.current-project-directory";

    /**
     *  this property indicates the location the shared assets folder.
     */
    public static final String SHARED_ASSETS_DIR_PROPERTY = "workspace.shared-assets-directory";

    /**
     *  this property indicates the location the performance files folder ( usually har files ).
     */
    public static final String PERFORMANCE_DIR_PROPERTY = "workspace.performance-directory";

    /**
     *  this property indicates the location the screenshots files folder.
     */
    public static final String SCREENSHOTS_DIR_PROPERTY = "workspace.screenshots-directory";

    /**
     *  this property indicates the location the runt-time exceptions reports folder.
     */
    public static final String EXCEPTIONS_DIR_PROPERTY = "workspace.exceptions-directory";

    /**
     *  this property indicates the location the runt-time attachments exception folder.
     */
    public static final String ATTACHMENTS_DIR_PROPERTY = "workspace.attachments-directory";

    /**
     *  this property indicates the location the last-report folder.
     */
    public static final String LAST_REPORT_DIR_PROPERTY = "workspace.last-report-directory";

    /**
     *  this property indicates the location the last-report folder.
     */
    public static final String ERROR_ANALYZER_DIR_PROPERTY = "workspace.error-analyzers-directory";

    /**
     *  this property indicates the location the fx IDE resources.
     */
    public static final String FX_RESOURCES_DIR_PROPERTY = "workspace.fx-resources-directory";

    /**
     *  this property indicates the location the fx working directory.
     */
    public static final String FX_WORKING_DIR_PROPERTY = "workspace.fx-working-directory";

    /**
     *  this property indicates the location the fx working directory.
     */
    public static final String PROJECT_RUNTIME_DIR_PROPERTY = "workspace.project-runtime-directory";

    /**
     * this property indicates the home directory location. the home directory location must be
     * specified {@code SELENIUM_BOOT_HOME}
     */
    private Path frameworkHome = FileSystems.getDefault().getPath( System.getenv().get( "SELENIUM_BOOT_HOME" ) );

    /**
     * this property indicates the root folder of the current project
     */
   // @FileExists
    private Path currentProjectDirectory;

    /**
     * this property indicates the location of the last-reports directory.
     */
    private Path lastReportDirectory;

    /**
     * this property indicates the location of the reports run-time attachments
     */
    private Path attachmentsDirectory;

    /**
     * this property indicates the location of the reports screenshots directory
     */
    private Path screenshotsDirectory;

    /**
     * this property indicates the location of the exceptions reports directory
     */
    private Path exceptionsDirectory;

    /**
     * this property indicates the location of the performance reports directory
     */
    private Path performanceDirectory;

    /**
     * This property indicates the location of a specific assets directory.
     */
    private Path projectAssetsDirectory;

    /**
     * this property indicates the location of the test logs files directory
     */
    private Path testLogsDirectory;

    /**
     * this property indicates the location of the archived results directory
     */
    private Path resultsArchiveDirectory;

    /**
     * this property indicates the location of the shared assets directory
     */
   // @FileExists
    private Path sharedAssetsDirectory;

    /**
     * this property indicates the location of the report resources directory
     */
   // @FileExists
    private Path resourcesDirectory;

    /**
     * this property indicates the location of the all projects directory
     */
    //@FileExists
    private Path projectsDirectory;

    /**
     * this property indicates the location of the shared downloads directory
     */
    private Path downloadsDirectory;

    /**
     * this property indicates the location of the shared temporary directory
     */
    private Path tempDirectory;

    /**
     * this property indicates the location of the executable web-drivers directory
     */
    private Path driversDirectory;

    /**
     * this property indicates the location of the executable extensions directory
     */
    private Path extensionsDirectory;

    private Path errorAnalyzersDirectory;

    private Path fxResourcesDirectory;

    private Path fxWorkingDirectory;

    /**
     * this property indicates the location of the runtime info files directory
     */
    private Path projectRuntimeDirectory;

    //endregion


    public Path getFrameworkHome()
    {
        return frameworkHome;
    }

    /**
     * Sets the location of the {@code home} directory
     *
     * @param frameworkHome the path location
     */
    public void setFrameworkHome( Path frameworkHome )
    {
        log.trace( "frameworkHome was set to: {}", frameworkHome );
        this.frameworkHome = frameworkHome.normalize();
    }

    /**
     * @return The location of the {@code home/projects/project current project} directory
     *
     * @see #CURRENT_PROJECT_DIR_PROPERTY
     */
    public Path getCurrentProjectDirectory()
    {
        return currentProjectDirectory;
    }

    /**
     * Sets the location of the {@code home/projects/project current project} directory
     *
     * @param currentProjectDirectory the path location
     *
     * @see #CURRENT_PROJECT_DIR_PROPERTY
     */
    public void setCurrentProjectDirectory( Path currentProjectDirectory )
    {
        log.trace( "currentProjectDirectory was set to: {}", currentProjectDirectory );
        this.currentProjectDirectory = currentProjectDirectory.normalize();
    }

    /**
     * @return The location of the {@code home/projects/project/last-report} directory
     *
     * @see #LAST_REPORT_DIR_PROPERTY
     */
    public Path getLastReportDirectory()
    {
        return lastReportDirectory;
    }

    /**
     * Sets the location of the {@code home/projects/project/last-report} directory
     *
     * @param lastReportDirectory the path location
     *
     * @see #LAST_REPORT_DIR_PROPERTY
     */
    public void setLastReportDirectory( Path lastReportDirectory )
    {
        log.trace( "lastReportDirectory was set to: {}", lastReportDirectory );
        this.lastReportDirectory = lastReportDirectory.normalize();
    }

    /**
     * @return The location of the {@code home/projects/project/last-report/attachments} directory
     *
     * @see #ATTACHMENTS_DIR_PROPERTY
     */
    public Path getAttachmentsDirectory()
    {
        return attachmentsDirectory;
    }

    /**
     * Sets the location of the {@code home/projects/project/last-report/attachments} directory
     *
     * @param attachmentsDirectory the path location
     *
     * @see #ATTACHMENTS_DIR_PROPERTY
     */
    public void setAttachmentsDirectory( Path attachmentsDirectory )
    {
        log.trace( "attachmentsDirectory was set to: {}", attachmentsDirectory );
        this.attachmentsDirectory = attachmentsDirectory.normalize();
    }

    /**
     * @return The location of the {@code home/projects/project/last-report/screenshots} directory
     *
     * @see #SCREENSHOTS_DIR_PROPERTY
     */
    public Path getScreenshotsDirectory()
    {
        return screenshotsDirectory;
    }

    /**
     * Sets the location of the {@code home/projects/project/last-report/screenshots} directory
     *
     * @param screenshotsDirectory the path location
     *
     * @see #SCREENSHOTS_DIR_PROPERTY
     */
    public void setScreenshotsDirectory( Path screenshotsDirectory )
    {
        log.trace( "screenshotsDirectory was set to: {}", screenshotsDirectory );
        this.screenshotsDirectory = screenshotsDirectory.normalize();
    }

    /**
     * @return The location of the {@code home/projects/project/last-report/exceptions} directory
     *
     * @see #EXCEPTIONS_DIR_PROPERTY
     */
    public Path getExceptionsDirectory()
    {
        return exceptionsDirectory;
    }

    /**
     * Sets the location of the {@code home/projects/project/last-report/exceptions} directory
     *
     * @param exceptionsDirectory the path location
     *
     * @see #EXCEPTIONS_DIR_PROPERTY
     */
    public void setExceptionsDirectory( Path exceptionsDirectory )
    {
        log.trace( "exceptionsDirectory was set to: {}", exceptionsDirectory );
        this.exceptionsDirectory = exceptionsDirectory.normalize();
    }

    /**
     * @return The location of the {@code home/projects/project/last-report/performance ( har files )} directory
     *
     * @see #PERFORMANCE_DIR_PROPERTY
     */
    public Path getPerformanceDirectory()
    {
        return performanceDirectory;
    }

    /**
     * Sets the location of the {@code home/projects/project/last-report/performance ( har files )} directory
     *
     * @param performanceDirectory the path location
     *
     * @see #PERFORMANCE_DIR_PROPERTY
     */
    public void setPerformanceDirectory( Path performanceDirectory )
    {
        log.trace( "performanceDirectory was set to: {}", performanceDirectory );
        this.performanceDirectory = performanceDirectory.normalize();
    }

    /**
     * @return The location of the {@code home/projects/project current project} directory
     *
     * @see #CURRENT_PROJECT_DIR_PROPERTY
     */
    public Path getProjectAssetsDirectory()
    {
        return projectAssetsDirectory;
    }

    /**
     * Sets the location of the {@code home/projects/project current project} directory
     *
     * @param projectAssetsDirectory the path location
     *
     * @see #CURRENT_PROJECT_DIR_PROPERTY
     */
    public void setProjectAssetsDirectory( Path projectAssetsDirectory )
    {
        log.trace( "projectAssetsDirectory was set to: {}", projectAssetsDirectory );
        this.projectAssetsDirectory = projectAssetsDirectory.normalize();
    }

    /**
     * @return The location of the {@code home/projects/project/last-report/logs} directory
     *
     * @see #LOGS_DIR_PROPERTY
     */
    public Path getTestLogsDirectory()
    {
        return testLogsDirectory;
    }

    /**
     * Sets the location of the {@code home/projects/project/last-report/logs} directory
     *
     * @param testLogsDirectory the path location
     */
    public void setTestLogsDirectory( Path testLogsDirectory )
    {
        log.trace( "testLogsDirectory was set to: {}", testLogsDirectory );
        this.testLogsDirectory = testLogsDirectory.normalize();
    }

    public Path getResultsArchiveDirectory()
    {
        return resultsArchiveDirectory;
    }

    public void setResultsArchiveDirectory( Path resultsArchiveDirectory )
    {
        this.resultsArchiveDirectory = resultsArchiveDirectory;
    }

    /**
     * @return  The location of the {@code home/projects/shared-assets} directory
     *
     * @see #SHARED_ASSETS_DIR_PROPERTY
     */
    public Path getSharedAssetsDirectory()
    {
        return sharedAssetsDirectory;
    }

    /**
     * Sets the location of the {@code home/projects/shared-assets} directory
     *
     * @param sharedAssetsDirectory the path location
     *
     * @see #SHARED_ASSETS_DIR_PROPERTY
     */
    public void setSharedAssetsDirectory( Path sharedAssetsDirectory )
    {
        log.trace( "sharedAssetsDirectory was set to: {}", sharedAssetsDirectory );
        this.sharedAssetsDirectory = sharedAssetsDirectory.normalize();
    }

    /**
     * @return The location of the {@code home/resources} directory
     *
     * @see #RESOURCES_DIR_PROPERTY
     */
    public Path getResourcesDirectory()
    {
        return resourcesDirectory;
    }

    /**
     * Sets the location of the {@code home/resources} directory
     *
     * This directory is required for the reports
     *
     * @param resourcesDirectory the path location
     *
     * @see #RESOURCES_DIR_PROPERTY
     */
    public void setResourcesDirectory( Path resourcesDirectory )
    {
        log.trace( "resourcesDirectory was set to: {}", resourcesDirectory );
        this.resourcesDirectory = resourcesDirectory.normalize();
    }

    /**
     * @return The location of the {@code home/projects} directory
     *
     * @see #PROJECTS_DIR_PROPERTY
     */
    public Path getProjectsDirectory()
    {
        return projectsDirectory;
    }

    /**
     * Sets the location of the {@code home/projects} directory
     *
     * @param projectsDirectory the path location
     *
     * @see #PROJECTS_DIR_PROPERTY
     */
    public void setProjectsDirectory( Path projectsDirectory )
    {
        log.trace( "projectsDirectory was set to: {}", projectsDirectory );
        this.projectsDirectory = projectsDirectory.normalize();
    }

    /**
     * @return The location of the {@code home/downloads} directory
     *
     * @see #DOWNLOADS_DIR_PROPERTY
     */
    public Path getDownloadsDirectory()
    {
        return downloadsDirectory;
    }

    /**
     * Sets the location of the {@code home/downloads} directory
     *
     * @param downloadsDirectory the path location
     *
     * @see #DOWNLOADS_DIR_PROPERTY
     */
    public void setDownloadsDirectory( Path downloadsDirectory )
    {
        log.trace( "downloadsDirectory was set to: {}", downloadsDirectory );
        this.downloadsDirectory = downloadsDirectory.normalize();
    }

    /**
     * @return The location of the {@code home/temp} directory
     *
     * @see #TEMP_DIR_PROPERTY
     */
    public Path getTempDirectory()
    {
        return tempDirectory;
    }

    /**
     * Sets the location of the {@code home/temp} directory
     *
     * @param tempDirectory the path location
     *
     * @apiNote contents of this directory will be removed every start of test
     * @see #TEMP_DIR_PROPERTY
     */
    public void setTempDirectory( Path tempDirectory )
    {
        log.trace( "tempDirectory was set to: {}", tempDirectory );
        this.tempDirectory = tempDirectory.normalize();
    }

    /**
     * @return The location of the {@code home/drivers executables} directory
     *
     * @see #DRIVERS_DIR_PROPERTY
     */
    public Path getDriversDirectory()
    {
        return driversDirectory;
    }

    /**
     * Sets the location of the {@code home/drivers executables} directory
     *
     * @param driversDirectory the path location
     *
     * @apiNote contents of this directory depends on configuration drivers.yaml
     *
     * @see #DRIVERS_DIR_PROPERTY
     */
    public void setDriversDirectory( Path driversDirectory )
    {
        log.trace( "driversDirectory was set to: {}", driversDirectory );
        this.driversDirectory = driversDirectory.normalize();
    }

    /**
     * @return The location of the {@code home/extensions browser extensions} directory
     *
     * @see #EXTENSIONS_DIR_PROPERTY
     */
    public Path getExtensionsDirectory()
    {
        return extensionsDirectory;
    }

    /**
     * Sets the location of the {@code home/extensions browser extensions} directory
     *
     * @param extensionsDirectory the path location
     *
     * @apiNote contents of this directory depends on configuration drivers.yaml
     *
     * @see #EXTENSIONS_DIR_PROPERTY
     */
    public void setExtensionsDirectory( Path extensionsDirectory )
    {
        log.trace( "extensionsDirectory was set to: {}", extensionsDirectory );
        this.extensionsDirectory = extensionsDirectory.normalize();
    }

    public Path getErrorAnalyzersDirectory()
    {
        return errorAnalyzersDirectory;
    }

    /**
     * Sets the location of the {@code home/errors analyzers} directory
     *
     * @param errorAnalyzersDirectory the path location
     */
    public void setErrorAnalyzersDirectory( Path errorAnalyzersDirectory )
    {
        log.trace( "errorAnalyzersDirectory was set to: {}", errorAnalyzersDirectory );
        this.errorAnalyzersDirectory = errorAnalyzersDirectory.normalize();
    }

    public Path getFxResourcesDirectory()
    {
        return fxResourcesDirectory;
    }

    /**
     * Sets the location of the {@code resources/fx/icons} directory
     *
     * @param fxResourcesDirectory the path location
     */
    public void setFxResourcesDirectory( Path fxResourcesDirectory )
    {
        this.fxResourcesDirectory = fxResourcesDirectory;
    }

    public Path getFxWorkingDirectory()
    {
        return fxWorkingDirectory;
    }

    /**
     * Sets the location of the {@code fx/} directory
     *
     * @param fxWorkingDirectory the path location
     */
    public void setFxWorkingDirectory( Path fxWorkingDirectory )
    {
        this.fxWorkingDirectory = fxWorkingDirectory;
    }

    public Path getProjectRuntimeDirectory()
    {
        return projectRuntimeDirectory;
    }

    public void setProjectRuntimeDirectory( Path projectRuntimeDirectory )
    {
        log.trace( "projectRuntimeDirectory was set to: {}", projectRuntimeDirectory );
        this.projectRuntimeDirectory = projectRuntimeDirectory;
    }

    /**
     * Invoked by a BeanFactory after it has set all bean properties supplied
     * (and satisfied BeanFactoryAware and ApplicationContextAware).
     *
     * This method allows the bean instance to perform initialization only possible when all bean
     * properties have been set and to throw an exception in the event of misconfiguration.
     *
     * @throws Exception in the event of misconfiguration (such
     *                   as failure to set an essential property) or if initialization fails.
     */
    @Override
    public void afterPropertiesSet() throws Exception
    {
        ApplicationContext context = StaticApplicationContextHolder.getInstance().getApplicationContext();
        Validator validator = context.getBean( "failFastValidator", Validator.class );
        Set<ConstraintViolation<WorkspaceProperties>> violations = validator.validate( this, WorkspaceProperties.class );
        if( violations.size() > 0 )
        {
            ConstraintViolationException ex =
                    new ConstraintViolationException( "Failed to validate Workspace properties", violations );
            throw new ValidationException( ex );

        }
    }
}
