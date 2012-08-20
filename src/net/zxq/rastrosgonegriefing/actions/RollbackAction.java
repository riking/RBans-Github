package net.zxq.rastrosgonegriefing.actions;

import net.zxq.rastrosgonegriefing.rollback.Rollback;
import net.zxq.rastrosgonegriefing.util.RBPlayer;

public class RollbackAction extends BaseAction {

	RBPlayer player;

	public RollbackAction(RBPlayer player) {
		this.player = player;
	}

	@Override
	public void start() {
		player.setDoingRollback(true);
		player.setSearchResult(result);
		new Rollback(player);
	}
}