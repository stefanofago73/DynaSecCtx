package it.fago.dynasecurity.dsl;

import it.fago.dynasecurity.DynaSecurityContext;

/**
 * 
 * @author Stefano Fago
 * 
 */
public class DynaSecurityConfiguration {
	//
	private DynaSecurityProviderConfigurations providers;
	//
	private DynaSecurityContextConfigurations context;
	//
	private DynaSecurityContext sec;

	public DynaSecurityConfiguration(DynaSecurityContext sec) {
		this.sec = sec;
	}

	public DynaSecurityProviderConfigurations providersDeclarations() {
		providers = new DynaSecurityProviderConfigurations(this);
		return providers;
	}

	public DynaSecurityContextConfigurations contextDeclarations() {
		context = new DynaSecurityContextConfigurations(this);
		return context;
	}

	public DynaSecurityContext create() {
		sec.init(context.context(), providers.buildProvidersList());
		context.destroy();
		providers.destroy();
		context = null;
		providers = null;
		return sec;
	}

}// END