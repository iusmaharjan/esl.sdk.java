package com.silanis.esl.sdk.examples;

import com.silanis.esl.sdk.DocumentPackage;
import com.silanis.esl.sdk.DocumentType;

import static com.silanis.esl.sdk.builder.DocumentBuilder.newDocumentWithName;
import static com.silanis.esl.sdk.builder.FieldBuilder.textField;
import static com.silanis.esl.sdk.builder.PackageBuilder.newPackageNamed;
import static com.silanis.esl.sdk.builder.SignatureBuilder.signatureFor;
import static com.silanis.esl.sdk.builder.SignerBuilder.newSignerWithEmail;

/**
 * Stamps populate a field of a document prior to it being presented to the signer. 
 * The value of the input is decided upon based on the integrator's needs.
 * 
 */
public class StampFieldValueExample extends SDKSample {

    public static final String INJECTED_FIELD_VALUE = "Céline Lelièvre";
    public static final String DOCUMENT_NAME = "First Document";

    public static void main( String... args ) {
        new StampFieldValueExample().run();
    }

    public StampFieldValueExample() {
        documentInputStream1 = this.getClass().getClassLoader().getResourceAsStream( "document-with-fields.pdf" );
    }

    @Override
    public void execute() {
        DocumentPackage superDuperPackage = newPackageNamed(getPackageName())
                .withSigner(newSignerWithEmail(email1)
                    .withFirstName("John")
                    .withLastName("Smith"))
                .withDocument(newDocumentWithName(DOCUMENT_NAME)
                    .fromStream(documentInputStream1, DocumentType.PDF)
                    .withSignature(signatureFor(email1)
                        .withName("AGENT_SIG_1"))
                    .withInjectedField(textField().withName("AGENT_SIG_2").withValue(INJECTED_FIELD_VALUE)))
                .build();

        packageId = eslClient.createPackage( superDuperPackage );
        eslClient.sendPackage( packageId );
        retrievedPackage = eslClient.getPackage( packageId );
    }
}