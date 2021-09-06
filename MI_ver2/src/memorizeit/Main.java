package memorizeit;

public class Main {
	static boolean mainRun = true;
	ListsDAO ld = new ListsDAO();
	
	public static void main(String[] args) {
		while (mainRun) {
			Main mm = new Main();
			mm.mainMenu();
		}
	}
	
	public void mainMenu() {
		ld.initialize();
		System.out.println(
				"--------------------------\n 1. 목록 조회 | 2. 단어장 열기 | 3. 새 단어장 생성 | 4. 단어장 삭제 \n 0. 종료 \n\n입력 >>");
		try {
			int choice = Integer.parseInt(Scn.scn.nextLine());
			switch (choice) {
			case 1:
				ld.viewList();
				break;
			case 2:
				ld.selectList();
				break;
			case 3:
				ld.newList();
				break;
			case 4:
				ld.deleteList();
				break;
			case 0:
				System.out.println("종료합니다.");
				mainRun = false;
				break;
			default:
				System.out.println("잘못된 번호를 선택하셨습니다.");
			}
		} catch (NumberFormatException e) {
			System.out.println("\n\n\n\n\n\n\n\n\n\n입력 오류! \n메인메뉴로 돌아갑니다.");
		}
	}
}
