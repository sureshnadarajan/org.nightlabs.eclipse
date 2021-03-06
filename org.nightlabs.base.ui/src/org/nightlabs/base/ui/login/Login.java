package org.nightlabs.base.ui.login;

import javax.security.auth.login.LoginException;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.nightlabs.eclipse.extension.AbstractEPProcessor;
import org.nightlabs.eclipse.extension.EPProcessorException;
import org.nightlabs.singleton.ISingletonProvider;
import org.nightlabs.singleton.SingletonProviderFactory;
import org.nightlabs.singleton.ISingletonProvider.ISingletonFactory;

/**
 * Base class for handling login authentication in applications.
 * You can register an implementation of {@link ILoginDelegate} via the extension-point "org.nightlabs.base.ui.login",
 * which will be processed by this class.
 * Per application only one registered {@link ILoginDelegate} is allowed, otherwise an exception is thrown.
 * You can trigger login or logout via the static methods {@link #login()} or {@link #logout()}.
 */
public final class Login
extends AbstractEPProcessor
{
	private static ISingletonProvider<Login> sharedInstance = SingletonProviderFactory.createProviderForFactory(new ISingletonFactory<Login>() {
		@Override
		public synchronized Login makeInstance() {
			Login login = new Login();
			login.process();
			return login;
		}
		
	});

	public static Login sharedInstance() {
		return sharedInstance.getInstance();
	}

	private Login() { }

	/**
	 * Is a shortcut to {@link #sharedInstance()} and {@link #_login()}.
	 *
	 * @return Returns the shared instance after login was performed.
	 * @throws LoginException If the login fails (usually the login will retry until the user cancels, but then, a WorkOfflineException or sth. similar will be thrown)
	 */
	public static Login login()
	throws LoginException
	{
		Login login = sharedInstance();
		login._login();
		return login;
	}

	/**
	 * Is a shortcut to {@link #sharedInstance()} and {@link #_logout()}.
	 *
	 * @return Returns the shared instance after logout was performed.
	 */
	public static Login logout()
	{
		Login login = sharedInstance();
		login._logout();
		return login;
	}

	public static LoginState getLoginState()
	{
		return sharedInstance()._getLoginState();
	}

//	private void lazyProcess()
//	{
//		if (isProcessed())
//			return;
//
//		try {
//			process();
//		} catch (EPProcessorException e) {
//			throw new RuntimeException(e);
//		}
//	}

	private void assertLoginDelegateExists()
	{
		if (loginDelegate == null)
			throw new IllegalStateException("There is no ILoginDelegate registered! Exactly one plugin must contribute an extension to the extension point \"" + getExtensionPointID() + "\"!"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public synchronized void _login() throws LoginException
	{
//		lazyProcess();
		assertLoginDelegateExists();
		loginDelegate.login();
	}

	public synchronized void _logout()
	{
//		lazyProcess();
		assertLoginDelegateExists();
		loginDelegate.logout();
	}

	public synchronized LoginState _getLoginState()
	{
//		lazyProcess();
		assertLoginDelegateExists();
		return loginDelegate.getLoginState();
	}

	private String contributingPluginId = null;
	private ILoginDelegate loginDelegate = null;

	@Override
	public String getExtensionPointID() {
		return "org.nightlabs.base.ui.login"; //$NON-NLS-1$
	}

	public static final String LOGIN_DELEGATE_ELEMENT = "login";
	
	@Override
	public void processElement(IExtension extension, IConfigurationElement element)
	throws Exception
	{
		if (Login.LOGIN_DELEGATE_ELEMENT.equals(element.getName())) 
		{
			ILoginDelegate loginDelegate;
			try {
				loginDelegate = (ILoginDelegate) element.createExecutableExtension("class"); //$NON-NLS-1$
			} catch (Exception e) {
				throw new EPProcessorException(e.getMessage(), extension, e);
			}

			if (this.loginDelegate != null)
				throw new EPProcessorException("More than one plugin provide an extension to \""+getExtensionPointID()+"\": The plugin \"" + contributingPluginId + "\" did already initialize loginDelegate and the plugin \"" + extension.getNamespaceIdentifier() + "\" collides with this previous contribution!", extension); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

			this.contributingPluginId = extension.getNamespaceIdentifier();
			this.loginDelegate = loginDelegate;			
		}
	}
}
