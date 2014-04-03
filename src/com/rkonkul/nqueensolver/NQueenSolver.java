package com.rkonkul.nqueensolver;

import java.util.ArrayList;
import java.util.List;

import com.rkonkul.board.NQueensBoard;

public class NQueenSolver {
	private int size_;

	public NQueenSolver(int size) {
		size_ = size;
	}

	public List<NQueensBoard> findSolution() {
		return findSolution(new SearchCheckpoint() {
			@Override
			public void checkStatus(NQueensBoard currentBoard) {
			}
		});
	}

	public List<NQueensBoard> findSolution(SearchCheckpoint checkpoint) {
		solutionList = new ArrayList<NQueensBoard>();
		NQueensBoard board = new NQueensBoard(size_);
		findSolutionRecurse(board, 0, checkpoint);
		return solutionList;
	}

	private List<NQueensBoard> solutionList;

	public boolean findSolutionRecurse(NQueensBoard board, int column,
			SearchCheckpoint checkpoint) {
		if (column >= size_) { // if column advanced past max col
			if (board.isValid()) {
				solutionList.add(board.copy()); //found a solution
			}
			return false;
		}
		for (int i = 0; i < size_; i++) {
			board.setQueen(i, column); // set a queen, if still safe and is a
										// possible solution, this is a correct
										// move
			checkpoint.checkStatus(board);
			if (board.isValid()
					&& findSolutionRecurse(board, column + 1, checkpoint)) {
				return true;
			} else { // remove it and try the next row
				board.removeQueen(i, column);
			}
		}
		return false;
	}

	public interface SearchCheckpoint {
		public void checkStatus(NQueensBoard currentBoard);
	}

	public static void main(String[] args) {
		NQueenSolver solver = new NQueenSolver(8);
		List<NQueensBoard> solutions = solver
				.findSolution(new SearchCheckpoint() {
					@Override
					public void checkStatus(NQueensBoard currentBoard) {
						System.out.println(currentBoard.toString() + "\n");
					}
				});
		for(int i=0; i<solutions.size(); i++) {
			System.out.println("Solution #" + (i + 1));
			System.out.println(solutions.get(i).toString() + "\n");
		}
		
	}
}
