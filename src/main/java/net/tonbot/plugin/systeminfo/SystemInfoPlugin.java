package net.tonbot.plugin.systeminfo;

import java.util.Set;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;

import net.tonbot.common.Activity;
import net.tonbot.common.TonbotPlugin;
import net.tonbot.common.TonbotPluginArgs;

public class SystemInfoPlugin extends TonbotPlugin {

	private Injector injector;

	public SystemInfoPlugin(TonbotPluginArgs args) {
		super(args);

		this.injector = Guice.createInjector(new SystemInfoModule(args.getPrefix(), args.getBotUtils()));
	}

	@Override
	public String getFriendlyName() {
		return "System Info";
	}

	@Override
	public String getActionDescription() {
		return "Display System Vitals";
	}

	@Override
	public boolean isHidden() {
		return false;
	}

	@Override
	public Set<Activity> getActivities() {
		return injector.getInstance(Key.get(new TypeLiteral<Set<Activity>>() {
		}));
	}
}
