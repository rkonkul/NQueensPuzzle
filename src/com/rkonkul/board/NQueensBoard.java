package com.rkonkul.board;

import java.util.ArrayList;
import java.util.List;

public class NQueensBoard {

	private List<List<NQueenSquare>> board;
	private int size_;

	public NQueensBoard(int size) {
		if (size <= 0) {
			throw new IllegalArgumentException(
					"Board size must be greater than zero");
		}
		size_ = size;
		board = new ArrayList<List<NQueenSquare>>();
		for (int i = 0; i < size; i++) {
			List<NQueenSquare> row = new ArrayList<NQueenSquare>();
			for (int j = 0; j < size; j++) {
				row.add(new NQueenSquare());
			}
			board.add(row);
		}
	}

	public boolean equals(Object o) {
		if (o instanceof NQueensBoard && size_ == ((NQueensBoard) o).getSize()) {
			for (int i = 0; i < size_; i++) {
				for (int j = 0; j < size_; j++) {
					if (isQueenPresent(i, j) != ((NQueensBoard) o)
							.isQueenPresent(i, j)) {
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}
	
	public int hashCode() {
		return board.hashCode();
	}

	public int getSize() {
		return size_;
	}

	public boolean removeQueen(int row, int col) {
		if (!board.get(row).get(col).isQueenPresent()) {
			return false;
		}
		board.get(row).get(col).setQueen(false);
		//lazy way: reset every cell to safe and re add the queens. Let setQueen() recalculate all of the safe cells
		for (int i = 0; i < size_; i++) {
			for (int j = 0; j < size_; j++) {
				board.get(i).get(j).setSafe(true);
			}
		}
		for (int i = 0; i < size_; i++) {
			for (int j = 0; j < size_; j++) {
				if (board.get(i).get(j).isQueenPresent()) {
					setQueen(i, j);
				}
			}
		}
		return true;
	}

	public void setQueen(int row, int col) {
		if (row < 0 || row >= size_ || col < 0 || col >= size_) {
			throw new IllegalArgumentException(
					"Row or col index is incorrect. row: " + row + " col: "
							+ col + " size: " + size_);
		}
		NQueenSquare cell = board.get(row).get(col);
		cell.setQueen(true);
		//search all 8 directions
		//-1 decrements, +1 increments
		searchDirection(row, col, -1, -1);
		searchDirection(row, col, -1, 0);
		searchDirection(row, col, -1, 1);
		searchDirection(row, col, 0, -1);
		searchDirection(row, col, 0, 1);
		searchDirection(row, col, 1, -1);
		searchDirection(row, col, 1, 0);
		searchDirection(row, col, 1, 1);
	}

	/**
	 * Old but equivalent version to searchDirection. Does not use a anonymous
	 * inner classes to abstract the apply on each cell function
	 * 
	 * @param row
	 * @param col
	 * @param rowMovement
	 * @param colMovement
	 */
	public void searchDirectionOriginal(int row, int col, int rowMovement,
			int colMovement) {
		boolean isQueenAttacking = false;
		int r = row + rowMovement;
		int c = col + colMovement;
		while (r >= 0 && r < size_ && c >= 0 && c < size_) {
			NQueenSquare cell = board.get(r).get(c);
			if (cell.isQueenPresent()) {
				isQueenAttacking = true;
			}
			r += rowMovement;
			c += colMovement;
		}
		r = row;
		c = col;
		if (isQueenAttacking) {
			while (r >= 0 && r < size_ && c >= 0 && c < size_) {
				NQueenSquare cell = board.get(r).get(c);
				cell.setSafe(false);
				r += rowMovement;
				c += colMovement;
			}
			r = row;
			c = col;
			while (r >= 0 && r < size_ && c >= 0 && c < size_) {
				NQueenSquare cell = board.get(r).get(c);
				cell.setSafe(false);
				r -= rowMovement;
				c -= colMovement;
			}
		}
	}

	static boolean isQueenAttacking = false;

	public void searchDirection(int row, int col, int rowMovement,
			int colMovement) {

		isQueenAttacking = false;
		//check if there is an attacking queen
		searchAndApply(row + rowMovement, col + colMovement, rowMovement,
				colMovement, new CellFunction() {
					@Override
					public void applyOnCell(int row, int col) {
						NQueenSquare cell = board.get(row).get(col);
						if (cell.isQueenPresent()) {
							isQueenAttacking = true;
						}
					}
				});
		if (isQueenAttacking) {
			//mark all cells in this direction as unsafe
			//first search in positive direction
			searchAndApply(row, col, rowMovement, colMovement,
					new CellFunction() {
						@Override
						public void applyOnCell(int row, int col) {
							NQueenSquare cell = board.get(row).get(col);
							cell.setSafe(false);
						}
					});
			//then negate the movement to search in the opposite direction
			searchAndApply(row, col, -rowMovement, -colMovement,
					new CellFunction() {
						@Override
						public void applyOnCell(int row, int col) {
							NQueenSquare cell = board.get(row).get(col);
							cell.setSafe(false);
						}
					});
		}
	}

	public boolean playerWon() {
		int numQueens = 0;
		for (int i = 0; i < size_; i++) {
			for (int j = 0; j < size_; j++) {
				if (isQueenPresent(i, j)) {
					numQueens++;
				}
			}
		}
		if (numQueens == size_) {
			boolean playerWon = true;
			for (int i = 0; i < size_; i++) {
				for (int j = 0; j < size_; j++) {
					if (!isQueenPresent(i, j) && !isSafe(i, j)) {
						playerWon = false;
					}
				}
			}
			if (playerWon) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Searches in a particular direction and applies a particular function on each cell
	 * @param row
	 * @param col
	 * @param rowMovement
	 * @param colMovement
	 * @param ftn
	 */
	private void searchAndApply(int row, int col, int rowMovement,
			int colMovement, CellFunction ftn) {
		while (row >= 0 && row < size_ && col >= 0 && col < size_) {
			ftn.applyOnCell(row, col);
			row += rowMovement;
			col += colMovement;
		}
	}

	public boolean isSafe(int row, int col) {
		return board.get(row).get(col).isSafe();
	}

	public boolean isQueenPresent(int row, int col) {
		return board.get(row).get(col).isQueenPresent();
	}

	public interface CellFunction {
		public void applyOnCell(int row, int col);
	}

	public String toString() {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < size_; i++) {
			for (int j = 0; j < size_; j++) {
				if (board.get(i).get(j).isQueenPresent()) {
					b.append("q");
				} else {
					b.append("_");
				}
				if (board.get(i).get(j).isSafe()) {
					b.append("s");
				} else {
					b.append("n");
				}
				b.append(" ");
			}
			b.append("\n");
		}
		return b.toString();
	}

	public boolean isValid() {
		for (int i = 0; i < size_; i++) {
			for (int j = 0; j < size_; j++) {
				if (!isSafe(i, j)) {
					return false;
				}
			}
		}
		return true;
	}

	public NQueensBoard copy() {
		NQueensBoard result = new NQueensBoard(this.getSize());
		for (int i = 0; i < size_; i++) {
			for (int j = 0; j < size_; j++) {
				if (isQueenPresent(i, j)) {
					result.setQueen(i, j);
				}
			}
		}
		return result;
	}

}
