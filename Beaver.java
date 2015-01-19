package team082;

import battlecode.common.*;

public class Beaver extends BaseRobot {

	public Beaver(RobotController rc) {
		super(rc);
		// TODO Auto-generated constructor stub
	}

	public void execute() throws GameActionException {
//		attackLeastHealthEnemy();
		
		bugMove(theirHQ);
		
		/*int numMinerFactories = rc.readBroadcast(16);
		int numHandwash = rc.readBroadcast(9);
		int numBarracks = rc.readBroadcast(3);
		int numTankFactories = rc.readBroadcast(11);
		int numHelipads = rc.readBroadcast(5);
		int numSupplyCenters = rc.readBroadcast(10);
		
		if (numMinerFactories < 2) {
			buildUnit(RobotType.MINERFACTORY);
			rc.setIndicatorString(0, "Minerfactories");
		}
		else if (numBarracks < 1) {
			buildUnit(RobotType.BARRACKS);
			rc.setIndicatorString(0, "Barracks");
		}
		else if (numTankFactories < 4) {
			buildUnit(RobotType.TANKFACTORY);
			rc.setIndicatorString(0, "Tanks");
		}
//		else if (numHelipads < 3) {
//			buildUnit(RobotType.HELIPAD);
//			rc.setIndicatorString(0, "Helipads");
//		}
		else if (numSupplyCenters < 2) {
			buildUnit(RobotType.SUPPLYDEPOT);
			rc.setIndicatorString(0, "Depot");
		}
		else if (numHandwash < 3) {
			buildUnit(RobotType.HANDWASHSTATION);
			rc.setIndicatorString(0, "Handwash");
		}
		mineAndMove(); */
		rc.yield();
	}

}
