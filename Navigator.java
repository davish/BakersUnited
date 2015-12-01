package bugplayer;

import battlecode.common.*;

/*
 * Courtesy of http://blog.stevearc.com/2011/12/30/code-snippets.html
 */

public class Navigator {

	enum State {
		BUGGING, FLOCKING
	}
	State state;
	MapLocation startBug;
	Direction startDesiredDir;
	Direction myDir;
	
	boolean goneAround;
	boolean hugLeft;
	
	int[] myProhibitedDirs;
	final int NO_DIR;
	boolean[][][] BLOCK_DIRS;
	
	RobotController rc;
	
	public Navigator(RobotController rc) {
		// TODO Auto-generated constructor stub
		NO_DIR = Direction.NONE.ordinal();
		myProhibitedDirs = new int[2];
		goneAround = false;
		hugLeft = true;
		state = State.FLOCKING;
		BLOCK_DIRS = new boolean[16][16][16];
		startDesiredDir = Direction.NORTH;
		
		this.rc = rc;
		
		for (Direction d: Direction.values()) {
		    if (d == Direction.NONE || d == Direction.OMNI || d.isDiagonal())
		        continue;
		    for (Direction b: Direction.values()) {
		        // Blocking a dir that is the first prohibited dir, or one
		        // rotation to the side
		        BLOCK_DIRS[d.ordinal()][b.ordinal()][d.ordinal()] = true;
		        BLOCK_DIRS[d.ordinal()][b.ordinal()][d.rotateLeft().ordinal()] = true;
		        BLOCK_DIRS[d.ordinal()][b.ordinal()][d.rotateRight().ordinal()] = true;
		        // b is diagonal, ignore it
		        if (!b.isDiagonal() && b != Direction.NONE && b != Direction.OMNI) {
		            // Blocking a dir that is the second prohibited dir, or one
		            // rotation to the side
		            BLOCK_DIRS[d.ordinal()][b.ordinal()][b.ordinal()] = true;
		            BLOCK_DIRS[d.ordinal()][b.ordinal()][b.rotateLeft().ordinal()] = true;
		            BLOCK_DIRS[d.ordinal()][b.ordinal()][b.rotateRight().ordinal()] = true;
		        }
		    }
		}
	}
	
	public Direction getNextMove(MapLocation target) {
		Direction desiredDir = rc.getLocation().directionTo(target);
		if (desiredDir == Direction.NONE || desiredDir == Direction.OMNI)
			return desiredDir;

		if (state == State.BUGGING) {
			if (rc.getLocation().distanceSquaredTo(target) < startBug.distanceSquaredTo(target) 
					&& canMove(desiredDir)) {
				myProhibitedDirs = new int[] {NO_DIR, NO_DIR};
				goneAround = false;
				state = State.FLOCKING;

			}
		}

		Direction d = desiredDir;
		switch(state) {
		case FLOCKING:
			Direction newDir = flockInDir(desiredDir, target);
			if (newDir != null)
				d = newDir;
			else {
				state = State.BUGGING;
				startBug = rc.getLocation();
				startDesiredDir = desiredDir;
			}
		case BUGGING:
			if (goneAround && (desiredDir == startDesiredDir.rotateLeft().rotateLeft() || 
			desiredDir == startDesiredDir.rotateRight().rotateRight())) {
				myProhibitedDirs[0] = NO_DIR;
			}
			if (desiredDir == startDesiredDir.opposite()) {
				myProhibitedDirs[0] = NO_DIR;
				goneAround = true;
			}

			Direction moveDir = hug(desiredDir, false);
			if (moveDir == null)
				moveDir = desiredDir;

			d = moveDir;
		}
		return d;
	}


	Direction hug(Direction desiredDir, boolean recursed) {
		if (canMove(desiredDir))
			return desiredDir;
		Direction tryDir = turn(desiredDir);

		for (int i = 0; i < 8 && !canMove(tryDir); i++)
			tryDir = turn(tryDir);

		if (!canMove(tryDir)) {
			hugLeft = !hugLeft;
			if (recursed) {
				if (myProhibitedDirs[0] != NO_DIR && myProhibitedDirs[1] != NO_DIR) {
					myProhibitedDirs[1] = NO_DIR;
					return hug(desiredDir, false);
				}
			}
			return hug(desiredDir, true);
		}

		if (tryDir != myDir && !tryDir.isDiagonal()) {
			if (turn(turn(Direction.values()[myProhibitedDirs[0]])) == tryDir) {
				myProhibitedDirs[0] = tryDir.opposite().ordinal();
	            myProhibitedDirs[1] = NO_DIR;
			} else {
				myProhibitedDirs[1] = myProhibitedDirs[0];
	            myProhibitedDirs[0] = tryDir.opposite().ordinal();
			}
		}
		return tryDir;

	}

	Direction turn(Direction dir) {
		return (hugLeft ? dir.rotateRight() : dir.rotateLeft());
	}


	Direction flockInDir(Direction desiredDir, MapLocation target) {
		Direction[] directions = new Direction[3];
		directions[0] = desiredDir;
		Direction left = desiredDir.rotateLeft();
		Direction right = desiredDir.rotateRight();
		boolean leftIsBetter = (rc.getLocation().add(left).distanceSquaredTo(target) < rc.getLocation().add(right).distanceSquaredTo(target));
		directions[1] = (leftIsBetter ? left : right);
		directions[2] = (leftIsBetter ? right : left);

		for (int i = 0; i < directions.length; i++){
			if (canMove(directions[i])){
				return directions[i];
			}
		}
		return null;
	}

	boolean canMove(Direction dir) {
		if (BLOCK_DIRS[myProhibitedDirs[0]][myProhibitedDirs[1]][dir.ordinal()])
	        return false;
	    
		return rc.canMove(dir);
	}

}
