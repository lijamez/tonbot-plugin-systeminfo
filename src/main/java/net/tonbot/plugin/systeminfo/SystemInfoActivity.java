package net.tonbot.plugin.systeminfo;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;

import net.tonbot.common.Activity;
import net.tonbot.common.ActivityDescriptor;
import net.tonbot.common.BotUtils;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.ComputerSystem;
import oshi.hardware.GlobalMemory;
import oshi.software.os.OperatingSystem;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

public class SystemInfoActivity implements Activity {

	private static final ActivityDescriptor ACTIVITY_DESCRIPTOR = ActivityDescriptor.builder()
			.route("systeminfo")
			.description("Displays system information.")
			.build();

	private final BotUtils botUtils;
	private final SystemInfo systemInfo;
	private final Runtime runtime;

	@Inject
	public SystemInfoActivity(
			BotUtils botUtils,
			SystemInfo systemInfo,
			Runtime runtime) {
		this.botUtils = Preconditions.checkNotNull(botUtils, "botUtils must be non-null.");
		this.systemInfo = Preconditions.checkNotNull(systemInfo, "systemInfo must be non-null.");
		this.runtime = Preconditions.checkNotNull(runtime, "runtime must be non-null.");
	}

	@Override
	public ActivityDescriptor getDescriptor() {
		return ACTIVITY_DESCRIPTOR;
	}

	@Override
	public void enact(MessageReceivedEvent event, String args) {

		EmbedBuilder embedBuilder = new EmbedBuilder();
		embedBuilder.withTitle("System Information");

		ComputerSystem cs = systemInfo.getHardware().getComputerSystem();
		embedBuilder.appendField("Manufacturer", cs.getManufacturer(), true);
		embedBuilder.appendField("Model", cs.getModel(), true);

		OperatingSystem os = systemInfo.getOperatingSystem();
		embedBuilder.appendField("Operating System", os.getFamily() + " " + os.getVersion(), true);

		CentralProcessor processor = systemInfo.getHardware().getProcessor();
		embedBuilder.appendField("Processor", processor.getName(), true);

		// System Memory Usage
		GlobalMemory mem = systemInfo.getHardware().getMemory();
		long memUsedMb = (mem.getTotal() - mem.getAvailable()) / 1000000;
		long memTotalMb = mem.getTotal() / 1000000;
		int memUsedPercent = (int) (((double) memUsedMb / memTotalMb) * 100);
		embedBuilder.appendField("System Memory Usage",
				memUsedMb + " / " + memTotalMb + " MB (" + memUsedPercent + "%)",
				true);

		// JVM Memory Usage
		long heapUsedMb = (runtime.totalMemory() - runtime.freeMemory()) / 1000000;
		long heapMaxMb = runtime.maxMemory() / 1000000;
		int heapUsedPercent = (int) (((double) heapUsedMb / heapMaxMb) * 100);
		embedBuilder.appendField("JVM Memory Usage",
				heapUsedMb + " MB out of " + heapMaxMb + " MB (" + heapUsedPercent + "%)", true);

		// CPU Load
		double cpuLoad = processor.getSystemCpuLoad();
		String cpuLoadStr;
		if (cpuLoad < 0) {
			cpuLoadStr = "Unknown";
		} else {
			int cpuLoadPercent = (int) (processor.getSystemCpuLoad() * 100);
			cpuLoadStr = Integer.toString(cpuLoadPercent) + "%";
		}
		embedBuilder.appendField("CPU Load", cpuLoadStr, true);

		botUtils.sendEmbed(event.getChannel(), embedBuilder.build());
	}
}
