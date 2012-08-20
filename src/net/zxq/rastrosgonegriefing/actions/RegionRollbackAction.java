package net.zxq.rastrosgonegriefing.actions;

import net.zxq.rastrosgonegriefing.rollback.Rollback;
import net.zxq.rastrosgonegriefing.util.RBPlayer;

public class RegionRollbackAction extends BaseAction {

	RBPlayer player;

	public RegionRollbackAction(RBPlayer player) {
		this.player = player;
	}

	@Override
	public void start() {
		player.setDoingRollback(true);
		new Rollback(player);
	}
}