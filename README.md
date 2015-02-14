## Dynamic Context for Security Providers ##


### Description ###

This a simple (raw) project to create a dynamic set of contexts<br/>
where you can use different scopes for different providers<br/> 
<br/>
Normally a security provider is added (removed) on java.security.Security<br/>
that has a global scope (JVM): sometimes this can be a problem especially<br/>
in an environment that has dynamic needs<br/>
<br/>

<pre><code>
Security.add( [some provider] )

...

Security.remove( [ some provider ] )
</code></pre>

### Simple solution: Thread scope ###

DynamicContext make possible to define and use different<br/>
security providers. You can choose one of the context defined,<br/>
and use it deciding when the executing Thread can see the n-th context.<br/>
Then you can come back to original state, as nothing was happen.</br>
This approach is compatiple with normal use of the java Security class</br>


### Example ###

First we define the DynaSecurityContext instance<br/>

<pre><code>

String demoCtx = "DEMO_CTX";
String demoProviderName = "BC";
Provider demoProvider = new BouncyCastleProvider();
		
DynaSecurityContext 
     sec1 = DynaSecurityContext
	         .newDynaSecurityCtx()
			    .providersDeclarations()
				  .addProvider(demoProvider)
			   .end()
				 .contextDeclarations()
				   .addContext(demoCtx, demoProviderName)
			   .end()
		    .create();
			
</code></pre>

then we use this instance and we define the Thread scope where we operate<br/>


<pre><code>

sec1.beginProvidersForCtx(demoCtx);

	System.out.println("\ninside ctx [" + demoCtx + "] i can see: "
		+ Security.getProvider(demoProviderName));

sec1.endCtxProviders();

</code></pre>

We can do more?!?!? YES! Classloading features can be another help<br/>
in a dynamic security world! ;-) <br/>
