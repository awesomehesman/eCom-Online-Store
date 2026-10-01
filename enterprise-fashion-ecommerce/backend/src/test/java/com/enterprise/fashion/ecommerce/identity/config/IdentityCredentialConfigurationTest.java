package com.enterprise.fashion.ecommerce.identity.config;

import java.util.Set;

import com.enterprise.fashion.ecommerce.identity.adapter.out.credential.Argon2CustomerPasswordVerificationAdapter;
import com.enterprise.fashion.ecommerce.identity.application.CredentialVerificationOutcome;
import com.enterprise.fashion.ecommerce.identity.application.port.out.CustomerPasswordVerificationPort;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static org.assertj.core.api.Assertions.assertThat;

class IdentityCredentialConfigurationTest {

    @Test
    void exposesTheGovernedAdapterThroughTheProjectOwnedPort() {
        try (AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(IdentityCredentialConfiguration.class)) {
            CustomerPasswordVerificationPort port = context.getBean(CustomerPasswordVerificationPort.class);

            assertThat(port).isExactlyInstanceOf(Argon2CustomerPasswordVerificationAdapter.class);
        }
    }

    @Test
    void applicationCredentialTypesRemainFrameworkIndependent() {
        JavaClasses applicationClasses = new ClassFileImporter().importClasses(
                CredentialVerificationOutcome.class,
                CustomerPasswordVerificationPort.class);

        noClasses()
                .that()
                .resideInAPackage("com.enterprise.fashion.ecommerce.identity.application..")
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage(
                        "org.springframework..",
                        "org.bouncycastle..",
                        "java.sql..",
                        "javax.sql..",
                        "org.postgresql..",
                        "org.flywaydb..",
                        "org.springframework.data..",
                        "org.springframework.jdbc..",
                        "org.springframework.web..")
                .check(applicationClasses);

        assertThat(applicationClasses)
                .extracting(javaClass -> javaClass.getPackageName())
                .containsOnlyElementsOf(Set.of(
                        "com.enterprise.fashion.ecommerce.identity.application",
                        "com.enterprise.fashion.ecommerce.identity.application.port.out"));
    }
}
