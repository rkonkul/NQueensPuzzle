package com.rkonkul.nqueensgui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.UIManager;

import base64.Base64;

import com.rkonkul.board.NQueensBoard;
import com.rkonkul.images.EncodedImages;

public class NQueensSwingGUI extends JFrame {

	private static final long serialVersionUID = 1147531706412206109L;

	int size = 8;
	JComboBox<Integer> boardSize;
	JButton clearButton = new JButton("Clear Queens");
	GridLayout boardLayout;
	JPanel boardPanel;
	NQueensBoard internalBoard;
	List<List<JButton>> buttonGrid;

	public NQueensSwingGUI(String name) {
		super(name);
		setResizable(false);

		try {
			queenIcon = loadScaledImage(Base64.decode(EncodedImages.queen));
			greenIcon = loadScaledImage(Base64.decode(EncodedImages.green));
			redIcon = loadScaledImage(Base64.decode(EncodedImages.red));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	ImageIcon queenIcon;
	ImageIcon greenIcon;
	ImageIcon redIcon;

	public ImageIcon loadScaledImage(byte[] bs) {
		ImageIcon Icon = new ImageIcon(bs);
		Image img = Icon.getImage();
		Image newimg = img.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
		Icon = new ImageIcon(newimg);
		return Icon;
	}

	public void addComponentsToPane(final Container pane) {
		boardSize = new JComboBox<Integer>(new Vector<Integer>(Arrays.asList(4, 5, 6, 7, 8, 9, 10, 11)));
		boardSize.setSelectedItem((Integer) size);

		JPanel controls = new JPanel();
		controls.setLayout(new GridLayout(6, 3));

		boardPanel = new JPanel();
		setupBoard();
		clearButton.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
				size = (Integer) boardSize.getSelectedItem();
				boardPanel.removeAll();
				setupBoard();
				frame.pack();
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}
		});
		controls.add(new Label("Board size:"));
		controls.add(boardSize);
		controls.add(clearButton);

		pane.add(boardPanel, BorderLayout.WEST);
		pane.add(new JSeparator(), BorderLayout.CENTER);
		pane.add(controls, BorderLayout.EAST);
	}

	private void setupBoard() {
		boardLayout = new GridLayout(size, size);
		boardPanel.setLayout(boardLayout);
		boardPanel.setPreferredSize(new Dimension(60 * size, 60 * size));

		buttonGrid = new ArrayList<List<JButton>>();
		internalBoard = new NQueensBoard(size);
		for (int i = 0; i < size; i++) {
			List<JButton> row = new ArrayList<JButton>();
			for (int j = 0; j < size; j++) {
				JButton jb = new JButton("");
				jb.setContentAreaFilled(false);
				jb.setName("" + i + " " + j + " _");
				jb.setIcon(greenIcon);
				jb.addMouseListener(cellListener());
				row.add(jb);
				boardPanel.add(jb);
			}
			buttonGrid.add(row);
		}
	}

	public MouseListener cellListener() {
		return new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				JButton source = (JButton) arg0.getSource();
				if (source.getName().endsWith("_")) { // no queen
					source.setName(source.getName().replace("_", "q"));
					source.setIcon(queenIcon);
					internalBoard.setQueen(Integer.parseInt(source.getName().split(" ")[0]),
							Integer.parseInt(source.getName().split(" ")[1]));

				} else { // button has queen
					source.setName(source.getName().replace("q", "_"));
					source.setIcon(new ImageIcon());
					internalBoard.removeQueen(Integer.parseInt(source.getName().split(" ")[0]),
							Integer.parseInt(source.getName().split(" ")[1]));
				}
				debug(internalBoard.toString());

				// check internal board and update all cells with
				// safe/not safe status
				for (int i = 0; i < size; i++) {
					for (int j = 0; j < size; j++) {
						if (!internalBoard.isQueenPresent(i, j) && internalBoard.isSafe(i, j)) {
							buttonGrid.get(i).get(j).setIcon(greenIcon);
						} else if (!internalBoard.isQueenPresent(i, j) && !internalBoard.isSafe(i, j)) {
							buttonGrid.get(i).get(j).setIcon(redIcon);
						}
					}
				}
				if (internalBoard.playerWon()) {
					playerWon();
				}
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}
		};
	}

	protected void playerWon() {
		JOptionPane.showMessageDialog(null, "You win!");
	}

	/**
	 * Create the GUI and show it. For thread safety, this method is invoked
	 * from the event dispatch thread.
	 */
	private static void createAndShowGUI() {
		frame = new NQueensSwingGUI("NQueens");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addComponentsToPane(frame.getContentPane());
		frame.pack();
		frame.setVisible(true);
	}

	static NQueensSwingGUI frame;

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	public void debug(String s) {
		System.out.println(s);
	}
}