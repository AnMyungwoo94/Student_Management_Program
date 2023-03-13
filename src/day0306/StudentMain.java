package day0306;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StudentMain {
	public static Scanner sc = new Scanner(System.in);
	public static final int INPUT = 1, PRINT = 2, ANLYZE = 3, SEARCH = 4, UPDATE = 5, SORT = 6, DELETE = 7, EXIT = 8;

	public static void main(String[] args) {
		// 지역변수선언
		boolean run = true;
		int no = 0;
		ArrayList<Student> list = new ArrayList<>();
		DBConnection dbcon = new DBConnection();

		// 무한루트
		while (run) {
			System.out.println();
			System.out.println("============================= [성적 프로그램] =========================");
			System.out.println("          1.입력 2.출력 3.분석 4.검색 5.수정 6.성적순 7.삭제 8.종료          ");
			System.out.println("====================================================================");
			System.out.print("숫자를 입력하세요 : ");
			no = Integer.parseInt(sc.nextLine());
			switch (no) {
			case INPUT:
				// 이름, 나이, 국어점수, 수학점수, 영어점수 입력받기
				Student student = inputDataStudent();
				// 데이타베이스 입력
				int rValue = dbcon.insert(student);
				if (rValue == 1) {
					System.out.println("삽입에 성공하였습니다.");
				} else {
					System.out.println("삽입에 실패하였습니다.");
				}
				break;
			case PRINT:
				ArrayList<Student> list2 = dbcon.select();
				if (list2 == null) {
					System.out.println("선택에 실패하였습니다.");
				} else {
					printStudent(list2);
				}
				break;
			case ANLYZE:
				// 분석: 이름, 나이, 총점, 평균, 등급
				ArrayList<Student> list3 = dbcon.analizeSclect();
				if (list3 == null) {
					System.out.println("분석에 실패하였습니다.");
				} else {
					printAnalyze(list3);
				}

				break;
			case SEARCH:
				// 학생검색 이름받기
				String dataname = searchStudent();
				ArrayList<Student> list4 = dbcon.nameSearchSelect(dataname);
				if (list4.size() >= 1) {
					printStudent(list4);
				} else {
					System.out.println("이름을 검색하는데 오류가 발생하였습니다.");
				}
				break;
			case UPDATE:
				// 학생검색 점수를 수정해서 저장
				int updateReturnValue = 0;
				int id = inputId();
				Student stu = dbcon.SclectId(id);
				if (stu == null) {
					System.out.println("수정에 오류가 발생하였습니다.");
				} else {
					Student updataStudent = updateStudent(stu);
					updateReturnValue = dbcon.update(updataStudent);
				}

				if (updateReturnValue == 1) {
					System.out.println("업데이트에 성공하였습니다.");
				} else {
					System.out.println("업데이트에 실패하였습니다.");
				}
				break;
			case SORT:
				ArrayList<Student> list5 = dbcon.selectSort();
				if (list5 == null) {
					System.out.println("정렬에 오류가 발생했습니다.");
				} else {
					printScoreSort(list5);
				}
				break;
			case DELETE:
				// 학생검색
				int deleteid = inputId();
				int deleteReturnValue = dbcon.delete(deleteid);
				if (deleteReturnValue == 1) {
					System.out.println("삭제에 성공하였습니다.");
				} else {
					System.out.println("삭제에 실패하였습니다.");
				}
				break;
			case EXIT:
				run = false;
				break;
			default:
				System.out.println("1~8까지의 숫자를 입력하세요");
				break;
			}
		} // end of while

		System.out.println("프로그램을 종료합니다.");
	}// end of main

	// 아이디 입력
	private static int inputId() {
		boolean run = true;
		int id = 0;
		while (run) {
			try {
				System.out.print("ID 입력(number) : ");
				id = Integer.parseInt(sc.nextLine());
				if (id > 0 && id < Integer.MAX_VALUE) {
					run = false;
				}
			} catch (NumberFormatException e) {
				System.out.println(" ID 오류");
			}
		}
		return id;
	}

	// 성적순으로 정렬
	private static void printScoreSort(ArrayList<Student> list) {
		Collections.sort(list, Collections.reverseOrder());
		for (int i = 0; i < list.size(); i++) {
			System.out.println(i + 1 + "등 " + list.get(i).toString());
		}
	}

	// 점수 업데이트
	private static Student updateStudent(Student student) {
		int kor = inputScoreSubject(student.getName(), "국어", student.getKor());
		student.setKor(kor);
		int eng = inputScoreSubject(student.getName(), "영어", student.getEng());
		student.setEng(eng);
		int math = inputScoreSubject(student.getName(), "수학", student.getMath());
		student.setMath(math);
		student.calTotal();
		student.calAvg();
		student.calGrade();
		System.out.println(student);
		return student;
	}

	// 점수 입력받기
	private static int inputScoreSubject(String subject, String name, int score) {
		boolean run = true;
		int data = 0;
		while (run) {
			System.out.print(name + " " + subject + " " + score + ">>");
			try {
				data = Integer.parseInt(sc.nextLine());
				Pattern pattern = Pattern.compile("^[0-9]{1,3}$");
				Matcher matcher = pattern.matcher(String.valueOf(data));
				if (matcher.find() && data <= 100 && data >= 0) {
					run = false;
				} else {
					System.out.println("점수를 잘못입력하였습니다. 재입력요청");
				}
			} catch (NumberFormatException e) {
				System.out.println("점수입력에 오류 발생");
				data = 0;
			}
		}
		return data;
	}

	// 이름으로 학생검색
	private static String matchingNamePattern() {
		String name = null;
		while (true) {
			try {
				System.out.print("검색할 학생이름: ");
				name = sc.nextLine();
				Pattern pattern = Pattern.compile("^[가-힣]{2,4}$");
				Matcher matcher = pattern.matcher(name);
				if (!matcher.find()) {
					System.out.println("이름입력오류발생 다시재입력요청합니다.");
				} else {
					break;
				}
			} catch (Exception e) {
				System.out.println("입력에서 오류가 발생했습니다.");
				break;
			}
		}
		return name;
	}

	private static String searchStudent() {
		String name = null;
		name = matchingNamePattern();
		return name;
	}

	// 분석 출력하기
	private static void printAnalyze(ArrayList<Student> list) {
		System.out.println("id" + "\t" + "이름" + "\t" + "나이" + "\t" + "총점" + "\t" + "평균" + "\t" + "등급");
		for (Student data : list) {
			System.out.println(data.getId() + "\t" + data.getName() + "\t" + data.getAge() + "\t" + data.getTotal()
					+ "\t" + String.format("%.2f", data.getAvg()) + "\t" + data.getGrade());
		}
	}

	// 전체 출력하기
	private static void printStudent(ArrayList<Student> list) {
		System.out.println("id" + "\t" + "이름" + "\t" + "나이" + "\t" + "국어" + "\t" + "영어" + "\t" + "수학" + "\t" + "총점"
				+ "\t" + "평균" + "\t" + "등급");
		for (Student data : list) {
			System.out.println(data);
		}
	}

	// 기본정보 입력받기
	private static Student inputDataStudent() {
		String name = NamePattern();
		int age = AgePatten();
		int kor = inputScore("국어");
		int eng = inputScore("수학");
		int math = inputScore("영어");
		Student student = new Student(name, age, kor, eng, math);
		student.calTotal();
		student.calAvg();
		student.calGrade();
		return student;
	}

	// 점수 pattern, matcher
	private static int inputScore(String subject) {
		int score = 0;
		while (true) {
			try {
				System.out.print(subject + "점수 입력 : ");
				score = Integer.parseInt(sc.nextLine());
				Pattern pattern = Pattern.compile("^[0-9]{1,3}$");
				Matcher matcher = pattern.matcher(String.valueOf(score));
				if (matcher.find() && score >= 0 && score < 101) {
					break;
				} else {
					System.out.println("오류 재입력");
				}
			} catch (NumberFormatException e) {
				System.out.println("입력 오류");
				break;
			}
		}
		return score;
	}

	// 나이 pattern, matcher
	private static int AgePatten() {
		int age = 0;
		while (true) {
			try {
				System.out.print("나이입력 : ");
				age = Integer.parseInt(sc.nextLine());
				Pattern pattern = Pattern.compile("^[0-9]{1,3}$");
				Matcher matcher = pattern.matcher(String.valueOf(age));
				if (matcher.find() && age <= 100) {
					break;
				} else {
					System.out.println("나이를 잘못입력하였습니다. 재입력요청");
				}
			} catch (NumberFormatException e) {
				System.out.println("나이입력에 오류 발생했습니다.");
				age = 0;
				break;
			}
		}
		return age;
	}

	// 이름 pattern, matcher
	public static String NamePattern() {
		String name = null;
		System.out.print("이름을 입력하세요. : ");
		while (true) {
			try {
				name = sc.nextLine();
				Pattern pattern = Pattern.compile("^[가-힣]{2,4}$");
				Matcher matcher = pattern.matcher(name);
				if (matcher.find()) {
					break;
				} else {
					System.out.println("이름을 잘못입력하셨습니다.");
				}
			} catch (Exception e) {
				System.out.println("입력에서 오류가 발생했습니다.");
				break;
			}
		}
		return name;
	}
}
