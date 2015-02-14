package it.fago.dynasecurity;

import it.fago.dynasecurity.dsl.DynaSecurityConfiguration;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.Provider;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 
 * @author Stefano Fago
 * 
 */
public class DynaSecurityContext {

	private static boolean isPossibleToUseInternal;
	private static Method fullProvs;
	private static Method pListAdd;
	private static Method pListProviders;
	private static Method threadBegin;
	private static Method threadEnd;
	private static Object EMPTY;
	private Object oldList;
	private boolean initialized;
	private String description;
	private HashMap<String, Object> providersList = new HashMap<String, Object>();
	public static final String EMPTY_CONTEXT_ID = "EMPTY";
	public static final String DEFAULT_CONTEXT_ID = "DEFAULT";

	static {
		setupIfPossible();
	}

	public static DynaSecurityConfiguration newDynaSecurityCtx() {
		if (!isPossibleToUseInternal) {
			throw new RuntimeException(
					"It's not possible to use this infrastructure due to some internal change of Java!");
		}
		return new DynaSecurityConfiguration(new DynaSecurityContext());
	}
	
	// =================================================
	//
	//
	//
	// =================================================

	private DynaSecurityContext() {
	}

	/**
	 * 
	 */
	public void init(HashMap<String, String[]> ctxResult,
			HashMap<String, Provider> pList) {
		try {
			oldList = fullProvs.invoke(null, (Object[]) null);
		} catch (Exception e) {
			throw new RuntimeException("Something wrong!", e);
		}
		createAggregateProvidersContextAndDescription(ctxResult, pList);
		initialized = true;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public String description() {
		return isInitialized() ? description : super.toString();
	}

	public void beginProvidersForCtx(String ctxId) {
		if (!initialized) {
			throw new RuntimeException(this + " not initialized! ");
		}
		try {
			threadBegin.invoke(null, providersList.get(ctxId));
		} catch (Exception e) {
			throw new RuntimeException(this + " not operating ", e);
		}
	}

	public void endCtxProviders() {
		if (!initialized) {
			throw new RuntimeException(this + " not initialized! ");
		}
		try {
			threadEnd.invoke(null, oldList);
		} catch (Exception e) {
			throw new RuntimeException(this + " not operating ", e);
		}
	}
	
	public List<Provider> providersSetForCtx(String ctxId) {
		try {
			return (List<Provider>) pListProviders.invoke(
					providersList.get(ctxId), (Object[]) null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 */
	public void destroy() {
		initialized = false;
		description = null;
		providersList.clear();
		providersList = null;
	}


	// =================================================
	//
	//
	//
	// =================================================

	private static void setupIfPossible() {
		try {
			Class<?> clzProviderList = ClassLoader.getSystemClassLoader()
					.loadClass("sun.security.jca.ProviderList");
			Class<?> clzProviders = ClassLoader.getSystemClassLoader()
					.loadClass("sun.security.jca.Providers");
			fullProvs = clzProviders.getMethod("getFullProviderList",
					(Class<?>[]) null);
			pListAdd = clzProviderList.getMethod("add", new Class<?>[] {
					clzProviderList, java.security.Provider.class });
			pListProviders = clzProviderList.getMethod("providers",
					(Class<?>[]) null);
			threadBegin = clzProviders.getMethod("beginThreadProviderList",
					new Class[] { clzProviderList });
			threadEnd = clzProviders.getMethod("endThreadProviderList",
					new Class[] { clzProviderList });
			Field empty = clzProviderList.getDeclaredField("EMPTY");
			empty.setAccessible(true);
			EMPTY = empty.get(null);
		} catch (Exception exc) {
			isPossibleToUseInternal = false;
		}
		isPossibleToUseInternal = true;
	}

	// =================================================
	//
	//
	//
	// =================================================

	protected void createAggregateProvidersContextAndDescription(
			HashMap<String, String[]> ctxResult, HashMap<String, Provider> plist) {

		StringBuilder sb = new StringBuilder();
		sb.append(getClass()).append("[").append(hashCode()).append("]")
				.append("(");
		Set<String> keySet = ctxResult.keySet();
		int kLen = keySet.size();
		for (Iterator<String> iterator = keySet.iterator(); iterator.hasNext();) {
			String ctxId = iterator.next();
			String[] provIds = ctxResult.get(ctxId);

			Object newList = oldList;

			for (int i = 0; i < provIds.length; i++) {
				Provider provider = plist.get(provIds[i]);
				try {
					newList = pListAdd.invoke(null, new Object[] { newList,
							provider });
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			providersList.put(ctxId, newList);
			sb.append(ctxId).append("=").append(newList).append(",");
		}
		if (kLen > 0) {
			int len = sb.length();
			sb.delete(len - 1, len);
		}
		sb.append(")");
		description = sb.toString();
		sb.setLength(0);
		sb = null;
		providersList.put(EMPTY_CONTEXT_ID, EMPTY);
		providersList.put(DEFAULT_CONTEXT_ID, oldList);
	}

}// END