package net.zxq.rastrosgonegriefing.actions;

import net.zxq.rastrosgonegriefing.util.RBPlayer;
import net.zxq.rastrosgonegriefing.util.WorldEditFilter;

public class WorldEditFilterAction extends BaseAction {

	RBPlayer player;

	public WorldEditFilterAction(RBPlayer player) {
		this.player = player;
	}

	@Override
	public void start() {
		player.setSearchResult(result);
		new WorldEditFilter(player.getSearchResult(), new RegionRollbackAction(player), player);
	}
}