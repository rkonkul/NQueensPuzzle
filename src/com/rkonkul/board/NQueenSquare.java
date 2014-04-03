package com.rkonkul.board;

public class NQueenSquare {
	private boolean isQueenPresent;
	private boolean isSafe;

	public NQueenSquare() {
		isQueenPresent = false;
		isSafe = true;
	}

	public boolean isSafe() {
		return isSafe;
	}

	public boolean isQueenPresent() {
		return isQueenPresent;
	}

	public void setSafe(boolean s) {
		isSafe = s;
	}

	public void setQueen(boolean q) {
		isQueenPresent = q;
	}
}
