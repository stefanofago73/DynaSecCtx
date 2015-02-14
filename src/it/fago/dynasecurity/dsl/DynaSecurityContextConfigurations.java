package it.fago.dynasecurity.dsl;

import java.util.HashMap;

/**
 * 
 * @author Stefano Fago
 *
 */
public class DynaSecurityContextConfigurations {
	//
	private DynaSecurityConfiguration parent;
	//
	private HashMap<String, String[]> context = new HashMap<String, String[]>();

	public DynaSecurityContextConfigurations(DynaSecurityConfiguration parent) {
		this.parent = parent;
	}

	public DynaSecurityContextConfigurations addContext(String id,
			String... providersId) {
		if (id != null) {
			if (providersId != null && providersId.length > 0) {
				context.put(id, providersId);
			}
		}
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

	HashMap<String, String[]> context() {
		return new HashMap<String, String[]>(
				context);
	}

	void destroy(){
		parent=null;
		context.clear();
		context = null;
	}
	
}// END
