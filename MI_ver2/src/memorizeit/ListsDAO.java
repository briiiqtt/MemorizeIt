package memorizeit;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class ListsDAO {
	connJDBC cj = new connJDBC();
	String listName; // 선택한 단어장 이름
	int newListNum; // (기존 단어장 최댓값)+1
	int vocaNum; // 선택한 단어장 번호
	boolean keepGoing;
	String divider = "==========================";

	// 초기 설정
	public void initialize() {
		// wordlists테이블 존재 여부 확인
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String isInitialized = null;
		String query = "select count(*) from all_tables where table_name = 'WORDLISTS'";
		try {
			conn = cj.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				isInitialized = rs.getString(1);
			}
			if (isInitialized.equals("0")) {
				// wordlists 테이블 생성
				String query3 = "CREATE TABLE wordlists (list_num number(10) PRIMARY KEY, list_name varchar2(1000), added_date date default sysdate, m_priority number(1) NOT NULL)";
				stmt = conn.createStatement();
				stmt.execute(query3);
				// wordlists 테이블의 초기 행 추가 (기본 단어장)
				String query4 = "insert into wordlists values(1,'단어장 1',sysdate,'9')";
				stmt = conn.createStatement();
				stmt.execute(query4); // words 테이블 생성
				String query5 = "CREATE TABLE words (list_num number(10), m_word varchar2(1000) NOT NULL, meanings varchar2(1000) NOT NULL, is_memorized number(1), CONSTRAINT fk FOREIGN KEY (list_num) REFERENCES wordlists(list_num) ON DELETE CASCADE)";
				stmt = conn.createStatement();
				stmt.execute(query5); // 기본 단어 row 생성
				String query6 = "insert into words values(1, 'java', '자바', 0)";
				stmt = conn.createStatement();
				stmt.execute(query6);
				String query7 = "insert into words values(1, 'yedam', '예담', 0)";
				stmt = conn.createStatement();
				stmt.execute(query7);
				System.out.println("초기화 완료");
			} else {
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cj.closeJDBC(conn, stmt, rs);
		}

	}

	public void selectListByPriority() {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			String query = "SELECT * FROM wordlists order by m_priority desc";
			conn = cj.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			System.out.println("단어장 목록");
			System.out.println(divider);
			System.out.println("번호.단어장       -    날짜          -    중요도(0~9)");
			while (rs.next()) {
				int listNum = rs.getInt(1);
				String listName = rs.getString(2);
				Date addedDate = rs.getDate(3);
				int mPriority = rs.getInt(4);
				String result = " " + listNum + ". " + listName + " - " + addedDate + " 중요도: " + mPriority;
				System.out.println(result);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cj.closeJDBC(conn, stmt, rs);
		}
	}
	
	public void selectListByListNum() {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			String query = "SELECT * FROM wordlists order by list_num";
			conn = cj.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			System.out.println("단어장 목록");
			System.out.println(divider);
			System.out.println("번호.단어장       -    날짜          -    중요도(0~9)");
			while (rs.next()) {
				int listNum = rs.getInt(1);
				String listName = rs.getString(2);
				Date addedDate = rs.getDate(3);
				int mPriority = rs.getInt(4);
				String result = " " + listNum + ". " + listName + " - " + addedDate + " 중요도: " + mPriority;
				System.out.println(result);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cj.closeJDBC(conn, stmt, rs);
		}
	}

	public void newList() {
		System.out.println("\n새 단어장을 만듭니다. 취소 = \"!취소\"\n\n새 단어장 이름 >>");
		String newListName = Scn.scn.nextLine();
		if (newListName.equals("!취소")) {
			System.out.println("취소되었습니다.");
			return;
		}
		System.out.print("\n중요도 설정(0~9) >>");
		String listPriority = Scn.scn.nextLine();
		if (listPriority.equals("!취소")) {
			System.out.println("취소되었습니다.");
			return;
		}
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			String query = "select list_num from wordlists";
			conn = cj.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			int i = 0;
			while (rs.next()) {
				int listNum = rs.getInt(1);
				i = i + 1;
				if (i != listNum) {
					newListNum = i;
					break;
				} else {
					newListNum = listNum + 1;
				}
			}
			String query2 = "insert into wordlists values(" + newListNum + ",'" + newListName + "',sysdate,'"
					+ listPriority + "')";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query2);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cj.closeJDBC(conn, stmt, rs);
		}

	}

	public void deleteList() {
		System.out.println("단어장 삭제\n삭제할 단어장의 번호를 입력하십시오.\n\n입력 >>");
		int choice = Integer.parseInt(Scn.scn.nextLine());
		System.out.println(choice + "번 단어장을 정말로 삭제하시겠습니까?\n\n삭제하시려면 \"넹\"이라고 입력하십시오. >>");
		String sure = Scn.scn.nextLine();
		if (sure.equals("넹")) {
			String query = "delete from wordlists where list_num =" + choice;
			Connection conn = null;
			Statement stmt = null;
			try {
				conn = cj.getConnection();
				stmt = conn.createStatement();
				stmt.executeQuery(query);
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				cj.closeJDBC(conn, stmt);
			}
			System.out.println(choice + "번 단어장이 삭제되었습니다.");
		} else {
			System.out.println("삭제가 취소되었습니다.");
		}
	}

	public void selectList() {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			String query = "SELECT * FROM wordlists order by list_num";
			conn = cj.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			System.out.println("단어장 목록");
			System.out.println(divider);
			System.out.println("번호.단어장       -    날짜          -    중요도(0~9)");
			while (rs.next()) {
				int listNum = rs.getInt(1);
				String listName = rs.getString(2);
				Date addedDate = rs.getDate(3);
				int mPriority = rs.getInt(4);
				String result = " " + listNum + ". " + listName + " - " + addedDate + " 중요도: " + mPriority;
				System.out.println(result);
			}
			System.out.println("--------------------------\n\n 0. 메인메뉴\n열어볼 단어장의 번호를 입력하십시오.\n\n입력 >>");
			vocaNum = Integer.parseInt(Scn.scn.nextLine());
			if (vocaNum == 0) {
				return;
			}
			String query1 = "select list_name from wordlists where list_num=" + vocaNum;
			stmt = conn.createStatement();
			ResultSet vocaNum = stmt.executeQuery(query1);
			while (vocaNum.next()) {
				listName = vocaNum.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cj.closeJDBC(conn, stmt, rs);
		}
		WordsDAO wd = new WordsDAO();
		wd.openList(listName, vocaNum);
	}
}
