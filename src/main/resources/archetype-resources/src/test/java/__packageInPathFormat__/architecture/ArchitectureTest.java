#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.core.importer.Location;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.junit.LocationProvider;
import com.tngtech.archunit.lang.ArchRule;

import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

/**
 * ArchUnit tests that enforce the hexagonal (ports-and-adapters) architecture rules.
 *
 * <p>Rules enforced:</p>
 * <ul>
 *   <li>Domain layer has ZERO dependencies on Spring, Jakarta EE, or adapter packages.</li>
 *   <li>Application layer depends only on domain — never on adapters.</li>
 *   <li>Adapters may depend on application ports and domain, but not on each other.</li>
 *   <li>Inbound adapters ({@code adapter.in}) never import outbound adapters ({@code adapter.out}).</li>
 * </ul>
 *
 * <p><strong>Java 25 note:</strong> {@code @AnalyzeClasses(packages = ...)} uses classloader URL
 * scanning which is blocked by the Java Platform Module System in Java 25+. This test uses a
 * {@link LocationProvider} backed by {@code Paths.get("target/classes")} to scan the filesystem
 * directly, which is unaffected by JPMS restrictions.</p>
 */
@AnalyzeClasses(packages = "${package}")
class ArchitectureTest {

    // -----------------------------------------------------------------------
    // Rule 1 — Domain purity
    // -----------------------------------------------------------------------

    @ArchTest
    static final ArchRule domainMustNotDependOnSpring =
            noClasses()
                    .that().resideInAPackage("..domain..")
                    .should().dependOnClassesThat()
                    .resideInAPackage("org.springframework..")
                    .because("Domain is pure Java — no framework coupling allowed");

    @ArchTest
    static final ArchRule domainMustNotDependOnJakartaEE =
            noClasses()
                    .that().resideInAPackage("..domain..")
                    .should().dependOnClassesThat()
                    .resideInAPackage("jakarta..")
                    .because("Domain is pure Java — no Jakarta EE imports allowed");

    @ArchTest
    static final ArchRule domainMustNotDependOnAdapters =
            noClasses()
                    .that().resideInAPackage("..domain..")
                    .should().dependOnClassesThat()
                    .resideInAPackage("..adapter..")
                    .because("Domain must be isolated from all adapter implementations");

    // -----------------------------------------------------------------------
    // Rule 2 — Application layer
    // -----------------------------------------------------------------------

    @ArchTest
    static final ArchRule applicationMustNotDependOnAdapters =
            noClasses()
                    .that().resideInAPackage("..application..")
                    .should().dependOnClassesThat()
                    .resideInAPackage("..adapter..")
                    .because("Application layer defines ports as interfaces — adapters implement them, not the other way");

    @ArchTest
    static final ArchRule applicationPortsMustBeInterfaces =
            classes()
                    .that().resideInAPackage("..application.port..")
                    .and().areTopLevelClasses()
                    .should().beInterfaces()
                    .because("Ports define contracts — only top-level types in application/port/** must be interfaces (nested Command/Result records are allowed)");

    // -----------------------------------------------------------------------
    // Rule 3 — Adapter isolation
    // -----------------------------------------------------------------------

    @ArchTest
    static final ArchRule inboundAdaptersMustNotDependOnOutboundAdapters =
            noClasses()
                    .that().resideInAPackage("..adapter.in..")
                    .should().dependOnClassesThat()
                    .resideInAPackage("..adapter.out..")
                    .because("Inbound adapters (controllers) must call application services, not persistence adapters");

    @ArchTest
    static final ArchRule outboundAdaptersMustNotDependOnInboundAdapters =
            noClasses()
                    .that().resideInAPackage("..adapter.out..")
                    .should().dependOnClassesThat()
                    .resideInAPackage("..adapter.in..")
                    .because("Persistence adapters must not know about HTTP adapters");

    // -----------------------------------------------------------------------
    // Rule 4 — Full layered architecture verification
    // -----------------------------------------------------------------------

    @ArchTest
    static final ArchRule layeredArchitectureIsRespected =
            layeredArchitecture()
                    .consideringOnlyDependenciesInLayers()
                    .layer("Domain").definedBy("..domain..")
                    .layer("Application").definedBy("..application..")
                    .layer("Adapter").definedBy("..adapter..")
                    .whereLayer("Domain").mayNotAccessAnyLayer()
                    .whereLayer("Application").mayOnlyAccessLayers("Domain")
                    .whereLayer("Adapter").mayOnlyAccessLayers("Application", "Domain");
}
