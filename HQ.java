package team082;

import battlecode.common.*;


public class HQ extends BaseRobot {
	RobotInfo[] myRobots;
	public HQ(RobotController rc) {
		super(rc);
		// TODO Auto-generated constructor stub
	}

	public void execute() throws GameActionException {

		/*
		 * Broadcast Key:
		 * 
		 * 1 - Beaver
		 * 2 - Basher
		 * 3 - Barracks
		 * 4 - Soldier
		 * 5 - Helipad
		 * 6 - Drone
		 * 7 - Launcher
		 * 8 - Missile
		 * 9 - Handwashstation
		 * 10- Supplydepot
		 * 11- Tankfactory
		 * 12- Tank
		 * 13- Tower
		 * 14- Aerospace lab
		 * 15- Miner
		 * 16- Minerfactory
		 * 
		 * 100-110: Flags
		 * 
		 * 150-200: Waypoints
		 * */

		myRobots = rc.senseNearbyRobots(999999, myTeam);

		int[] numU = new int[17];
		for (RobotInfo r : myRobots) {
			RobotType type = r.type;
			switch (r.type) {
			case BEAVER:
				numU[1]++;
				break;
			case BASHER:
				numU[2]++;
				break;
			case SOLDIER:
				numU[4]++;
				break;
			case HANDWASHSTATION:
				numU[9]++;
				break;
			case AEROSPACELAB:
				numU[14]++;
				break;
			case BARRACKS:
				numU[3]++;
				break;
			case COMMANDER:
				break;
			case COMPUTER:
				break;
			case DRONE:
				numU[6]++;
				break;
			case HELIPAD:
				numU[5]++;
				break;
			case HQ:
				break;
			case LAUNCHER:
				numU[7]++;
				break;
			case MINER:
				numU[15]++;
				break;
			case MINERFACTORY:
				numU[16]++;
				break;
			case MISSILE:
				numU[8]++;
				break;
			case SUPPLYDEPOT:
				numU[10]++;
				break;
			case TANK:
				numU[12]++;
				break;
			case TANKFACTORY:
				numU[11]++;
				break;
			case TECHNOLOGYINSTITUTE:
				break;
			case TOWER:
				numU[13]++;
				break;
			case TRAININGFIELD:
				break;
			default:
				break;

			}
		}
		for (int i = 0; i < numU.length; i++) {
			rc.broadcast(i, numU[i]);
		}

		MapLocation rallyPoint;

		int charge = rc.readBroadcast(100);

		if (numU[12] > 40)
			charge = 1;
		else if (numU[12] < 5)
			charge = 0;

		if (Clock.getRoundNum() > 1700) {
			charge = 1;
		}
		
		rc.broadcast(100, charge);

		if (charge == 0) {
			rallyPoint = new MapLocation( (this.myHQ.x*2 + this.theirHQ.x) / 3,
					(this.myHQ.y*2 + this.theirHQ.y) / 3);
		}
		else {
			MapLocation[] ti = rc.senseEnemyTowerLocations();
			int min = 99999;
			MapLocation t;
			if (ti.length > 0) {
				t = ti[0];
				for (MapLocation m : ti) {
					int temp = this.myHQ.distanceSquaredTo(m);
					if (temp < min) {
						min = temp;
						t = m;
					}
				}
				rallyPoint = t;
			} else {
				rallyPoint = this.theirHQ;
			}
			rc.setIndicatorString(0, rallyPoint.toString());

			
		}

		rc.broadcast(150, rallyPoint.x);
		rc.broadcast(151, rallyPoint.y);

		attackEnemyZero();

		if (numU[1] < 2)
			spawnUnit(RobotType.BEAVER);
		rc.yield();

	}

}
