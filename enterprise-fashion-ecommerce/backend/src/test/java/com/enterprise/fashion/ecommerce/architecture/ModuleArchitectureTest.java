package com.enterprise.fashion.ecommerce.architecture;

import java.util.List;
import java.util.stream.Stream;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ModuleArchitectureTest {

    private static final String ROOT_PACKAGE = "com.enterprise.fashion.ecommerce";
    private static final String SHARED_PACKAGE = ROOT_PACKAGE + ".shared";
    private static final String BOOTSTRAP_CLASS =
            ROOT_PACKAGE + ".EnterpriseFashionEcommerceApplication";

    private static final List<String> BUSINESS_MODULE_SEGMENTS = List.of(
            "identity",
            "customer",
            "product",
            "category",
            "inventory",
            "pricing",
            "cart",
            "checkout",
            "payment",
            "order",
            "shipping",
            "notifications",
            "cms",
            "administration",
            "reporting",
            "search",
            "returns");

    private static final JavaClasses PRODUCTION_CLASSES = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages(ROOT_PACKAGE);

    @Test
    void productionCodeExistsForArchitectureValidation() {
        assertFalse(PRODUCTION_CLASSES.isEmpty(), "Architecture checks require production classes");
    }

    @Test
    void productionClassesStayWithinTheBootstrapOrGovernedModules() {
        String[] permittedPackages = Stream.concat(
                        Stream.of(ROOT_PACKAGE),
                        Stream.concat(
                                BUSINESS_MODULE_SEGMENTS.stream()
                                        .map(segment -> ROOT_PACKAGE + "." + segment + ".."),
                                Stream.of(SHARED_PACKAGE + "..")))
                .toArray(String[]::new);

        classes()
                .that()
                .resideInAPackage(ROOT_PACKAGE + "..")
                .should()
                .resideInAnyPackage(permittedPackages)
                .as("production classes stay in the root bootstrap package or a governed Module package")
                .check(PRODUCTION_CLASSES);
    }

    @Test
    void theRootPackageContainsOnlyTheApplicationBootstrap() {
        classes()
                .that()
                .resideInAPackage(ROOT_PACKAGE)
                .should()
                .haveFullyQualifiedName(BOOTSTRAP_CLASS)
                .as("the canonical root package contains only the Spring Boot application bootstrap")
                .check(PRODUCTION_CLASSES);
    }

    @Test
    void domainCodeDoesNotDependOnFrameworksOrOutwardImplementations() {
        noClasses()
                .that()
                .resideInAPackage(ROOT_PACKAGE + "..domain..")
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage(
                        "org.springframework..",
                        "org.flywaydb..",
                        "org.postgresql..",
                        "com.azure..",
                        "java.sql..",
                        "javax.sql..",
                        "jakarta.persistence..",
                        ROOT_PACKAGE + "..adapter..",
                        ROOT_PACKAGE + "..config..")
                .allowEmptyShould(true)
                .as("Domain code remains independent of frameworks, persistence, Adapters, and configuration")
                .check(PRODUCTION_CLASSES);
    }

    @Test
    void loginEmailPreparationDependsOnlyOnBasicJavaAndItsOwnType() {
        classes()
                .that()
                .haveFullyQualifiedName(ROOT_PACKAGE + ".identity.domain.model.CustomerLoginEmailInput")
                .should()
                .onlyDependOnClassesThat()
                .resideInAnyPackage(
                        "java.lang",
                        "java.lang.invoke",
                        "java.util",
                        "java.util.function",
                        "java.util.stream",
                        ROOT_PACKAGE + ".identity.domain.model")
                .as("Login email preparation has no infrastructure, security, web, or IDNA dependencies")
                .check(PRODUCTION_CLASSES);
    }

    @Test
    void applicationCodeDoesNotDependOnConcreteAdapters() {
        noClasses()
                .that()
                .resideInAPackage(ROOT_PACKAGE + "..application..")
                .should()
                .dependOnClassesThat()
                .resideInAPackage(ROOT_PACKAGE + "..adapter..")
                .allowEmptyShould(true)
                .as("Application code does not depend on concrete Adapters")
                .check(PRODUCTION_CLASSES);
    }

    @Test
    void domainAndApplicationCodeDoNotDependOnConfigurationOrBootstrap() {
        noClasses()
                .that()
                .resideInAnyPackage(
                        ROOT_PACKAGE + "..domain..",
                        ROOT_PACKAGE + "..application..")
                .should()
                .dependOnClassesThat()
                .resideInAPackage(ROOT_PACKAGE + "..config..")
                .orShould()
                .dependOnClassesThat()
                .haveFullyQualifiedName(BOOTSTRAP_CLASS)
                .allowEmptyShould(true)
                .as("Domain and Application code do not depend on configuration or bootstrap implementation")
                .check(PRODUCTION_CLASSES);
    }

    @Test
    void domainAndApplicationCodeDoNotDependOnPersistenceTechnology() {
        noClasses()
                .that()
                .resideInAnyPackage(
                        ROOT_PACKAGE + "..domain..",
                        ROOT_PACKAGE + "..application..")
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage(
                        "org.springframework.data..",
                        "org.springframework.jdbc..",
                        "org.postgresql..",
                        "org.flywaydb..",
                        "java.sql..",
                        "javax.sql..",
                        "jakarta.persistence..",
                        ROOT_PACKAGE + "..adapter..persistence..")
                .allowEmptyShould(true)
                .as("Domain and Application code remain independent of persistence technology")
                .check(PRODUCTION_CLASSES);
    }

    @Test
    void modulesDoNotDependOnAnotherModulesInternalImplementation() {
        for (String targetModule : BUSINESS_MODULE_SEGMENTS) {
            String[] otherModules = BUSINESS_MODULE_SEGMENTS.stream()
                    .filter(module -> !module.equals(targetModule))
                    .map(module -> ROOT_PACKAGE + "." + module + "..")
                    .toArray(String[]::new);

            ArchRule rule = noClasses()
                    .that()
                    .resideInAnyPackage(otherModules)
                    .should()
                    .dependOnClassesThat()
                    .resideInAnyPackage(
                            ROOT_PACKAGE + "." + targetModule + ".domain..",
                            ROOT_PACKAGE + "." + targetModule + ".adapter..",
                            ROOT_PACKAGE + "." + targetModule + ".config..")
                    .allowEmptyShould(true)
                    .as("Modules do not access " + targetModule
                            + " Domain, Adapter, or configuration internals");

            rule.check(PRODUCTION_CLASSES);
        }
    }

    @Test
    void sharedCodeDoesNotDependOnBusinessModuleInternals() {
        String[] businessModuleInternals = BUSINESS_MODULE_SEGMENTS.stream()
                .flatMap(module -> Stream.of(
                        ROOT_PACKAGE + "." + module + ".domain..",
                        ROOT_PACKAGE + "." + module + ".adapter..",
                        ROOT_PACKAGE + "." + module + ".config.."))
                .toArray(String[]::new);

        noClasses()
                .that()
                .resideInAPackage(SHARED_PACKAGE + "..")
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage(businessModuleInternals)
                .allowEmptyShould(true)
                .as("shared technical code does not access business Module internals")
                .check(PRODUCTION_CLASSES);
    }
}
