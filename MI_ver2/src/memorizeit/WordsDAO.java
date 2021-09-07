package memorizeit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class WordsDAO extends ListsDAO {
	connJDBC cj = new connJDBC();
	boolean keepGoing;

	public void openList(String listName, int vocaNum) {
		System.out.println(divider);
		System.out.println("선택  : <" + listName
				+ ">\n--------------------------\n 1. 모든 단어 보기\n 2. 단어 추가 | 3. 단어 삭제 | 4. 단어 검색\n 5. 단어 시험 | 6. 맞힌 단어 초기화\n 0. 초기화면으로\n\n>>");
		int choice2 = Integer.parseInt(Scn.scn.nextLine());
		switch (choice2) {
		case 1:
			viewWord(listName, vocaNum);
			break;
		case 2:
			addWord(listName, vocaNum);
			break;
		case 3:
			deleteWord(listName, vocaNum);
			break;
		case 4:
			searchWord(listName, vocaNum);
			break;
		case 5:
			wordTest(listName, vocaNum);
			break;
		case 6:
			reset(vocaNum);
			break;
		case 0:
			System.out.println("초기 화면으로 돌아갑니다.");
			break;
		default:
			System.out.println("잘못된 번호를 선택하셨습니다.");
		}
	}

	public void viewWord(String listName, int vocaNum) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			String query = "select m_word, meanings from words where list_num=" + vocaNum;
			conn = cj.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				String word = rs.getString(1);
				String meaning = rs.getString(2);
				System.out.println(word + " : " + meaning);
			}
		} catch (SQLException e) {
			e.toString();
		} finally {
			cj.closeJDBC(conn, stmt, rs);
		}

	}

	public void addWord(String listName, int vocaNum) {
		Connection conn = null;
		Statement stmt = null;
		keepGoing = true;
		while (keepGoing) {
			System.out.print("\n단어 >>");
			String words = Scn.scn.nextLine();
			System.out.print("\n뜻 >>");
			String meanings = Scn.scn.nextLine();
			try {
				String query = "insert into words values(" + vocaNum + ", '" + words + "', '" + meanings + "', 0)";
				conn = cj.getConnection();
				stmt = conn.createStatement();
				stmt.execute(query);
				System.out.println("단어 추가됨.");
			} catch (SQLException e) {
				e.toString();
			} finally {
				cj.closeJDBC(conn, stmt);
			}
			keepGoing();
		}
	}

	public void searchWord(String listName, int vocaNum) {
		System.out.println(divider);
		System.out.println(
				"선택  : <" + listName + ">\n--------------------------\n 1. 단어로 검색\n 2. 뜻으로 검색\n 0. 초기화면으로\n\n>>");
		int choice = Integer.parseInt(Scn.scn.nextLine());
		switch (choice) {
		case 1:
			searchWordWithWord(listName, vocaNum);
			break;
		case 2:
			searchWordWithMeaning(listName, vocaNum);
			break;
		case 0:
			System.out.println("초기 화면으로 돌아갑니다.");
			return;
		default:
			System.out.println("잘못된 번호를 선택하셨습니다.");
		}
	}

	public void searchWordWithWord(String listName, int vocaNum) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		keepGoing = true;
		while (keepGoing) {
			System.out.println(divider);
			System.out.println("선택  : <" + listName + ">");
			System.out.println("--------------------------\n\n\n\n 단어 입력 \n>>");
			String searchWord = Scn.scn.nextLine();

			try {
				String query = "select m_word, meanings from words where list_num=" + vocaNum + "and m_word = '"
						+ searchWord + "'";
				conn = cj.getConnection();
				stmt = conn.createStatement();
				rs = stmt.executeQuery(query);
				System.out.println(divider);
				System.out.println("선택  : <" + listName + ">\n--------------------------\n");
				while (rs.next()) {
					String word = rs.getString(1);
					String meaning = rs.getString(2);
					System.out.println(word + " : " + meaning);
				}
				System.out.println();
				System.out.println("--------------------------");
				System.out.println();
			} catch (SQLException e) {
				e.toString();
			} finally {
				cj.closeJDBC(conn, stmt, rs);
			}
			keepGoing();
		}
		openList(listName, vocaNum);

	}

	public void searchWordWithMeaning(String listName, int vocaNum) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		keepGoing = true;
		while (keepGoing) {
			System.out.println(divider);
			System.out.print("선택  : <" + listName
					+ ">\n--------------------------\n\n\n\n--------------------------\n\n 뜻 입력 \n>>");
			String searchMeaning = Scn.scn.nextLine();
			try {
				String query = "select m_word, meanings from words where list_num=" + vocaNum + "and meanings = '"
						+ searchMeaning + "'";
				conn = cj.getConnection();
				stmt = conn.createStatement();
				rs = stmt.executeQuery(query);
				for (int i = 1; i < 15; i++) {
					System.out.println();
				}
				System.out.println("--------------------------\n");
				while (rs.next()) {
					String word = rs.getString(1);
					String meaning = rs.getString(2);
					System.out.println(word + " : " + meaning);
				}
				System.out.println("\n--------------------------\n");
			} catch (SQLException e) {
				e.toString();
			} finally {
				cj.closeJDBC(conn, stmt, rs);
			}
			keepGoing();
		}
		openList(listName, vocaNum);

	}

	public void deleteWord(String listName, int vocaNum) {
		Connection conn = null;
		Statement stmt = null;
		keepGoing = true;
		while (keepGoing) {
			System.out.println(divider);
			System.out.println("선택  : <" + listName
					+ ">\n--------------------------\n\n\n\n--------------------------\n\n 삭제할 단어 \n>>");
			String deleteWord = Scn.scn.nextLine();

			try {
				String query = "delete from words where list_num=" + vocaNum + "and m_word = '" + deleteWord + "'";
				conn = cj.getConnection();
				stmt = conn.createStatement();
				stmt.execute(query);
				System.out.println("삭제되었습니다.");
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				cj.closeJDBC(conn, stmt);
			}
			keepGoing();
		}
		openList(listName, vocaNum);

	}

	public void wordTest(String listName, int vocaNum) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String query = "select m_word, meanings from words where is_memorized = 0 and list_num =? order by dbms_random.random";
			conn = cj.getConnection();
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, vocaNum);
			rs = stmt.executeQuery();
			while (rs.next()) {
				String word = rs.getString("m_word");
				String meaning = rs.getString("meanings");
				System.out.println();
				System.out.print("  " + word + " : 뜻 입력>>");
				String answer = Scn.scn.nextLine();
				if (answer.equals(meaning)) {
					System.out.println("딩동댕~\n맞히셨습니다.");
					String query2 = "update words set is_memorized = 1 where m_word ='" + word + "'";
					conn.createStatement().execute(query2);
				} else {
					System.out.println("땡! 정답은 < " + meaning + " > 입니다.");
				}
			}
			String query2 = "select count(*) from words";
			stmt = conn.prepareStatement(query2);
			rs = stmt.executeQuery();
			rs.next();
			int allWord = rs.getInt(1);
			String query3 = "select count(*) from words where is_memorized = 1";
			stmt = conn.prepareStatement(query3);
			rs = stmt.executeQuery();
			rs.next();
			int correctWord = rs.getInt(1);
			if (correctWord == allWord) {
				System.out.println("모든 문제를 맞히셨습니다. \n시험을 다시 진행하려면 \"맞힌 문제 초기화\"를 해주세요.");
			} else {
				System.out.println("< " + vocaNum + " > 현재까지 " + allWord + "문제 중 " + correctWord + "문제 맞히셨습니다.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cj.closeJDBC(conn, stmt, rs);
		}
	}

	public void reset(int vocaNum) {
		Connection conn = null;
		Statement stmt = null;
		System.out.println("맞힌 문제를 리셋합니다\n\"ok\" : 리셋\n아무 키 : 돌아가기\n>>");
		String testFinished = Scn.scn.nextLine();
		if (testFinished.equals("ok")) {
			try {
				String query = "update words set is_memorized = 0 where list_num =" + vocaNum;
				conn = cj.getConnection();
				stmt = conn.createStatement();
				stmt.execute(query);
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				cj.closeJDBC(conn, stmt);
			}
			System.out.println("리셋 완료");
		}
	}

	public void keepGoing() {
		boolean run = true;
		while (run) {
			System.out.println("\n1: 계속 | 2: 그만\n>>");
			while (true) {
				int keepInput = Integer.parseInt(Scn.scn.nextLine());
				if (keepInput == 1) {
					keepGoing = true;
					run = false;
					break;
				} else if (keepInput == 2) {
					keepGoing = false;
					run = false;
					break;
				} else {
					break;
				}
			}
		}
	}

}
