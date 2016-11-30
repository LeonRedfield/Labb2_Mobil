package app.com.example.teddy.labb2_android;

import android.util.Log;

import java.util.Arrays;

/**
 * @author Jonas W�hsl�n, jwi@kth.se. 
 * Revised by Anders Lindstr�m, anderslm@kth.se
 * Modified by: Carlos Galdo & Teddy Chavez
 */

public class NineMenMorrisRules {
	private int[] gameplan;
	private int whitemarker, blackmarker;
	private int turn; // player in turn

	public static final int WHITE_MOVES = 1;
	public static final int BLACK_MOVES = 2;

	public static final int EMPTY_SPACE = -1;
	public static final int WHITE_MARKER = 4;
	public static final int BLACK_MARKER = 5;

	public NineMenMorrisRules() {
		gameplan = new int[24]; // zeroes
		Arrays.fill(gameplan, -1);
		whitemarker = 9;
		blackmarker = 9;
		turn = BLACK_MOVES;
	}

	/**
	 * Returns true if a move is successful
	 */
	public boolean legalMove(int To, int From, int color) {
		Log.v("In legalMove", "to =" + To+ " From=" + From + " color="+color + " turn=" + turn);
		if (color == turn) {
			if (turn == BLACK_MOVES) {
				Log.v("Turns==BLACK_MOVEs", "YES");
				if (blackmarker > 0) {
					Log.v("blackmarker>0", "YES" + "gameplan[TO] = " + gameplan[To] + "empty_space==" + EMPTY_SPACE);
					if (gameplan[To] == EMPTY_SPACE) {
						Log.v("gamepla[To]==empty", "YES");
						gameplan[To] = BLACK_MARKER;
						blackmarker--;
						turn = WHITE_MOVES;
						return true;
					}
				}
				/*else*/
				if (gameplan[To] == EMPTY_SPACE) {
					boolean valid = isValidMove(To, From);
					if (valid == true) {
						gameplan[To] = BLACK_MARKER;
						turn = WHITE_MOVES;
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else {
				if (whitemarker > 0) {
					if (gameplan[To] == EMPTY_SPACE) {
						gameplan[To] = WHITE_MARKER;
						whitemarker--;
						turn = BLACK_MOVES;
						return true;
					}
				}
				if (gameplan[To] == EMPTY_SPACE) {
					boolean valid = isValidMove(To, From);
					if (valid == true) {
						gameplan[To] = WHITE_MARKER;
						turn = BLACK_MOVES;
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			}
		} else {
			return false;
		}
	}

	/**
	 * Returns true if position "to" is part of three in a row.
	 */
	public boolean remove(int to) {

		if ((to == 16 || to == 17 || to == 18) && gameplan[16] == gameplan[17]
				&& gameplan[17] == gameplan[18]) {
			return true;
		} //done
		else if ((to == 8 || to == 9 || to == 10)
				&& gameplan[8] == gameplan[9] && gameplan[9] == gameplan[10]) {
			return true;
		} else if ((to == 0 || to == 1 || to == 2)
				&& gameplan[0] == gameplan[1] && gameplan[1] == gameplan[2]) {
			return true;
		} //done
		else if ((to == 18 || to == 19 || to == 20)
				&& gameplan[18] == gameplan[19] && gameplan[19] == gameplan[20]) {
			return true;
		}
		else if ((to == 10 || to == 11 || to == 12)
				&& gameplan[10] == gameplan[11] && gameplan[11] == gameplan[12]) {
			return true;
		}
		else if ((to == 2 || to == 3 || to == 4)
				&& gameplan[2] == gameplan[3] && gameplan[3] == gameplan[4]) {
			return true;
		}
		else if ((to == 20 || to == 21 || to == 22)
				&& gameplan[20] == gameplan[21] && gameplan[21] == gameplan[22]) {
			return true;
		}
		else if ((to == 12 || to == 13 || to == 14)
				&& gameplan[12] == gameplan[13] && gameplan[13] == gameplan[14]) {
			return true;
		}
		else if ((to == 4 || to == 5 || to == 6)
				&& gameplan[4] == gameplan[5] && gameplan[5] == gameplan[6]) {
			return true;
		}
		else if ((to == 16 || to == 23 || to == 22)
				&& gameplan[16] == gameplan[23] && gameplan[23] == gameplan[22]) {
			return true;
		}
		else if ((to == 8 || to == 15 || to == 14)
				&& gameplan[8] == gameplan[15] && gameplan[15] == gameplan[14]) {
			return true;
		}
		else if ((to == 0 || to == 7 || to == 6)
				&& gameplan[0] == gameplan[7] && gameplan[7] == gameplan[6]) {
			return true;
		}
		else if ((to == 23 || to == 15 || to == 7)
				&& gameplan[23] == gameplan[15] && gameplan[15] == gameplan[7]) {
			return true;
		}
		else if ((to == 1 || to == 9 || to == 17)
				&& gameplan[1] == gameplan[9] && gameplan[9] == gameplan[17]) {
			return true;
		}
		else if ((to == 19 || to == 11 || to == 3)
				&& gameplan[19] == gameplan[11] && gameplan[11] == gameplan[3]) {
			return true;
		}
		else if ((to == 21 || to == 13 || to == 5)
				&& gameplan[21] == gameplan[13] && gameplan[13] == gameplan[5]) {
			return true;
		}
		return false;
	}

	/**
	 * Request to remove a marker for the selected player.
	 * Returns true if the marker where successfully removed
	 */
	public boolean remove(int From, int color) {
		if (gameplan[From] == color) {
			gameplan[From] = EMPTY_SPACE;
			return true;
		} else
			return false;
	}

	/**
	 *  Returns true if the selected player have less than three markerss left.
	 */
	public boolean win(int color) {
		int countMarker = 0;
		int count = 0;
		while (count <= 23) {
			if (gameplan[count] != EMPTY_SPACE && gameplan[count] != color)
				countMarker++;
			count++;
		}
		if (whitemarker <= 0 && blackmarker <= 0 && countMarker < 3)
			return true;
		else
			return false;
	}

	/**
	 * Returns EMPTY_SPACE = -1 WHITE_MARKER = 4 READ_MARKER = 5
	 */
	public int board(int From) {
		return gameplan[From];
	}

	public int[] getGameplan()
	{
		return gameplan;
	}
	
	/**
	 * Check whether this is a legal move.
	 */
	private boolean isValidMove(int to, int from) {
		
		if(this.gameplan[to] != EMPTY_SPACE) return false;
		
		switch (to) {
			case 0:
				return (from == 7 || from == 1);
		case 1:
			return (from == 0 || from == 2|| from == 9);
		case 2:
			return (from == 1 || from == 3);
		case 3:
			return (from == 2 || from == 4 || from == 11);
		case 4:
			return (from == 3 || from == 5);
		case 5:
			return (from == 4 || from == 6 || from == 13);
		case 6:
			return (from == 5 || from == 7);
		case 7:
			return (from == 9 || from == 15 || from == 0);
		case 8:
			return (from == 15 || from == 9);
		case 9:
			return (from == 1 || from == 8 || from == 17 || from == 10);
		case 10:
			return (from == 9 || from == 11);
		case 11:
			return (from == 10 || from == 3 || from == 12 || from == 19);
		case 12:
			return (from == 11 || from == 13);
		case 13:
			return (from == 5 || from == 12 || from == 14 || from == 21);
		case 14:
			return (from == 13 || from == 15);
		case 15:
			return (from == 7 || from == 8 || from == 14 || from == 23);
		case 16:
			return (from == 17 || from == 23);
		case 17:
			return (from == 9 || from == 16 || from == 18);
		case 18:
			return (from == 17  || from == 19);
		case 19:
			return (from == 11 || from == 18 || from == 20);
		case 20:
			return (from == 19 || from == 21);
		case 21:
			return (from == 13 || from == 20 || from == 22);
		case 22:
			return (from == 21 || from == 23);
		case 23:
			return (from == 22 || from == 16 || from == 15);
		}
		return false;
	}

	public int getAmountOfWhiteCheckers() {return this.whitemarker;}

	public int getAmountOfBlackCheckers(){return this.blackmarker;}


}