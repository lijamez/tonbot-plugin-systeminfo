package net.tonbot.plugin.systeminfo;

import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import net.tonbot.common.Activity;
import net.tonbot.common.BotUtils;
import net.tonbot.common.Prefix;

class SystemInfoModule extends AbstractModule {

	private final String prefix;
	private final BotUtils botUtils;

	public SystemInfoModule(String prefix, BotUtils botUtils) {
		this.prefix = Preconditions.checkNotNull(prefix, "prefix must be non-null.");
		this.botUtils = Preconditions.checkNotNull(botUtils, "botUtils must be non-null.");
	}

	public void configure() {
		bind(String.class).annotatedWith(Prefix.class).toInstance(prefix);
		bind(BotUtils.class).toInstance(botUtils);
	}

	@Provides
	@Singleton
	Set<Activity> activities(SystemInfoActivity systemInfoActivity) {
		return ImmutableSet.of(systemInfoActivity);
	}

	@Provides
	@Singleton
	Runtime runtime() {
		return Runtime.getRuntime();
	}
}
