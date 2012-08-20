package net.zxq.rastrosgonegriefing.actions;

import net.zxq.rastrosgonegriefing.util.PageManager;
import net.zxq.rastrosgonegriefing.util.RBPlayer;

public class SearchAction extends BaseAction {

	RBPlayer player;

	public SearchAction(RBPlayer player) {
		this.player = player;
	}

	@Override
	public void start() {
		player.setSearchResult(result);
		player.setSearching(false);
		PageManager.printPage(player, 0);
	}
}