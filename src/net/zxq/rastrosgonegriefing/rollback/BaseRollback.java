package net.zxq.rastrosgonegriefing.rollback;

import org.bukkit.World;

public abstract class BaseRollback {

	static World world;

	public abstract boolean rollback(String line);
}