package it.fago.dynasecurity.dsl;

import java.security.Provider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 
 * @author Stefano Fago
 * 
 */
public class DynaSecurityProviderConfigurations {
	//
	private DynaSecurityConfiguration parent;
	//
	private ArrayList<Provider> providers = new ArrayList<Provider>();

	public DynaSecurityProviderConfigurations(DynaSecurityConfiguration parent) {
		this.parent = parent;
	}

	public DynaSecurityProviderConfigurations addProvider(Provider provider) {
		if (provider == null) {
			return this;
		}
		providers.add(provider);
		return this;
	}

	public DynaSecurityConfiguration end() {
		return parent;
	}

	// =======================================================
	//
	//
	//
	// =======================================================

	HashMap<String, Provider> buildProvidersList() {
		HashMap<String, Provider> list = new HashMap<String, Provider>();
		for (Iterator<Provider> iterator = providers.iterator(); iterator
				.hasNext();) {
			Provider p = iterator.next();
			list.put(p.getName(), p);
		}
		return list;
	}

	void destroy() {
		providers.clear();
		providers = null;
	}

}// END
