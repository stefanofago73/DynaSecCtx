package it.fago.dynasecurity.dsl.tests;

import it.fago.dynasecurity.DynaSecurityContext;

import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * 
 * @author Stefano Fago
 * 
 */
public class FastTutorial {

	public static void main(String[] args) {
		simpleExample();
	}

	/**
	 * 
	 */
	public static void simpleExample() {

		String demoCtx = "DEMO_CTX";
		String demoProvder = "BC";

		DynaSecurityContext sec1 = DynaSecurityContext.newDynaSecurityCtx()
			  .providersDeclarations()
				  .addProvider(new BouncyCastleProvider())
			   .end()
				.contextDeclarations()
				   .addContext(demoCtx, demoProvder)
			   .end()
		      .create();

		System.out.println( sec1 + " initialized: "
				+ sec1.isInitialized() + "\ndescription: " + sec1.description()
				+ "\n");

		System.out
				.println("EMPTY CONTEXT: n"
						+ sec1.providersSetForCtx(DynaSecurityContext.EMPTY_CONTEXT_ID));
		System.out
				.println("DEFAULT CONTEXT: \n"
						+ sec1.providersSetForCtx(DynaSecurityContext.DEFAULT_CONTEXT_ID));

		sec1.beginProvidersForCtx(demoCtx);

		System.out.println("\ninside ctx [" + demoCtx + "] i can see: "
				+ Security.getProvider(demoProvder));

		sec1.endCtxProviders();

		System.out.println("\noutside ctx [" + demoCtx + "] i can't see: "
				+ demoProvder);

		sec1.destroy();
	}

}// END