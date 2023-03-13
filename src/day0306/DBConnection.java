package day0306;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;


public class DBConnection {
	// 멤버변수
	private Connection connection = null;

	// 생성자 : 디폴트생성자
	// 멤버함수
	public void connect() {
		// 데이터삽입
		// 외부에서 데이타베이스를 접속할 수 있도록 설정
		Properties properties = new Properties();
		FileInputStream fis = null;
		// 2.db.properties 파일로드
		try {
			fis = new FileInputStream("C:/Users/flzm5/eclipse-workspace/day0306/src/day0306/db.properties");
			properties.load(fis);
		} catch (FileNotFoundException e) {
			System.out.println("FileInputStream error" + e.getStackTrace());
		} catch (IOException e) {
			System.out.println("propertices.load error" + e.getStackTrace());
			
		}

		
		// 2.내부적으로 JDBC 드라이버매니저를 통해서 DB와 연결을 가져온다.
		try {
			// 1. jdbc 클래스로드
			Class.forName(properties.getProperty("driverName"));
			// 2. mysql DB 연결
			connection = DriverManager.getConnection(properties.getProperty("url"), 
					properties.getProperty("user"),
					properties.getProperty("password"));
		} catch (ClassNotFoundException e) {
			System.out.println("[데이타베이스 로드오류]" + e.getStackTrace());
		} catch (SQLException e) {
			System.out.println("[데이타베이스 연결오류]" + e.getStackTrace());
		}
	}

	// 데이터삽입
	public int insert(Student s) {
		// 3.데이타를 삽입한다. insert into 테이블명(필드, ...) values(_, _, _);
		this.connect();
		PreparedStatement ps = null;
		int returnValue = -1;
//		Student s = new Student("kbj", 34, 100, 100, 100);
//		s.calTotal();
//		s.calAvg();
//		s.calGrade();
		String query = "insert into studentTBL values(null,?,?, ?, ?, ?, ?, ?,?)";
		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, s.getName());
			ps.setInt(2, s.getAge());
			ps.setInt(3, s.getKor());
			ps.setInt(4, s.getEng());
			ps.setInt(5, s.getMath());
			ps.setInt(6, s.getTotal());
			ps.setDouble(7, s.getAvg());
			ps.setString(8, s.getGrade());
			// 삽입 성공하면 1을 리턴한다.엔터
			returnValue = ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("insert 오류발생 " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("ps close 오류" + e.getMessage());
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				System.out.println("connection close 오류" + e.getMessage());
			}
		} // 파이널리
		return returnValue;
	}

	// 선택 statement
	public ArrayList<Student> select() {
		ArrayList<Student> list = new ArrayList<>();

		this.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = "select * from studentTBL;";

		try {
			ps = connection.prepareStatement(query);
			// Select 성공하면 ResultSet / 실패하면 null
			rs = ps.executeQuery();
			if (rs == null) {
				return null;
			}
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				int age = rs.getInt("age");
				int kor = rs.getInt("kor");
				int eng = rs.getInt("eng");
				int math = rs.getInt("math");
				int total = rs.getInt("total");
				Double avg = rs.getDouble("avg");
				String grade = rs.getString("grade");
				list.add(new Student(id, name, age, kor, eng, math, total, avg, grade));
			}
		} catch (Exception e) {
			System.out.println("select 오류발생 " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("ps close 오류" + e.getMessage());
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				System.out.println("connection close 오류" + e.getMessage());
			}
		} // 파이널리
		return list;
	}

	// 분석
	public ArrayList<Student> analizeSclect() {
		ArrayList<Student> list = new ArrayList<>();
		this.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = "select id, name, age, total, avg, grade from studenttbl";

		try {
			ps = connection.prepareStatement(query);
			// Select 성공하면 ResultSet / 실패하면 null
			rs = ps.executeQuery();
			if (rs == null) {
				return null;
			}
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				int age = rs.getInt("age");
				int total = rs.getInt("total");
				Double avg = rs.getDouble("avg");
				String grade = rs.getString("grade");
				list.add(new Student(id, name, age, 0, 0, 0, total, avg, grade));
			}
		} catch (Exception e) {
			System.out.println("select 오류발생 " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("ps close 오류" + e.getMessage());
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				System.out.println("connection close 오류" + e.getMessage());
			}
		} // 파이널리
		return list;
	}

	// 이름검색선탱
	public ArrayList<Student> nameSearchSelect(String dataname) {
		ArrayList<Student> list = new ArrayList<>();
		this.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;
		// String query = "select * from studenttbl where name like '%"+name+"%';";
		String query = "select * from studenttbl where name like ?";

		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, "%" + dataname + "%");
			// Select 성공하면 ResultSet / 실패하면 null
			rs = ps.executeQuery();
			if (rs == null) {
				return null;
			}
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				int age = rs.getInt("age");
				int kor = rs.getInt("kor");
				int eng = rs.getInt("eng");
				int math = rs.getInt("math");
				int total = rs.getInt("total");
				Double avg = rs.getDouble("avg");
				String grade = rs.getString("grade");
				list.add(new Student(id, name, age, kor, eng, math, total, avg, grade));
			}
		} catch (Exception e) {
			System.out.println("select 오류발생 " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("ps close 오류" + e.getMessage());
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				System.out.println("connection close 오류" + e.getMessage());
			}
		} // 파이널리
		return list;
	}

	// 셀렉트 아이디
	public Student SclectId(int dataid) {
		Student student = null;
		this.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;
		// String query = "select * from studenttbl where name like '%"+name+"%';";
		String query = "select * from studenttbl  where id = ?";

		try {
			ps = connection.prepareStatement(query);
			ps.setInt(1, dataid);
			// Select 성공하면 ResultSet / 실패하면 null
			rs = ps.executeQuery();
			if (rs == null) {
				return null;
			}
			if (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				int age = rs.getInt("age");
				int kor = rs.getInt("kor");
				int eng = rs.getInt("eng");
				int math = rs.getInt("math");
				int total = rs.getInt("total");
				Double avg = rs.getDouble("avg");
				String grade = rs.getString("grade");
				student = new Student(id, name, age, kor, eng, math, total, avg, grade);
			}
		} catch (Exception e) {
			System.out.println("select id 오류발생 " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("ps close 오류" + e.getMessage());
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				System.out.println("connection close 오류" + e.getMessage());
			}
		} // 파이널리
		return student;
	}

	// 업데이트
	public int update(Student s) {
		// 3.데이타를 삽입한다. insert into 테이블명(필드, ...) values(_, _, _);
		this.connect();
		PreparedStatement ps = null;
		int returnValue = -1;
		String query = "update studenttbl  set  kor = ?, eng = ?, math = ?, total = ?, avg = ?, grade = ? where id = ?";
		try {
			ps = connection.prepareStatement(query);
			ps.setInt(1, s.getKor());
			ps.setInt(2, s.getEng());
			ps.setInt(3, s.getMath());
			ps.setInt(4, s.getTotal());
			ps.setDouble(5, s.getAvg());
			ps.setString(6, s.getGrade());
			ps.setInt(7, s.getId());
			// 삽입 성공하면 1을 리턴한다.엔터
			returnValue = ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("update 오류발생 " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("ps close 오류" + e.getMessage());
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				System.out.println("connection close 오류" + e.getMessage());
			}
		} // 파이널리
		return returnValue;
	}

	public ArrayList<Student> selectSort() {
		ArrayList<Student> list = new ArrayList<>();

		this.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = "select * from studenttbl order by total desc;";

		try {
			ps = connection.prepareStatement(query);
			// Select 성공하면 ResultSet / 실패하면 null
			rs = ps.executeQuery();
			if (rs == null) {
				return null;
			}
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				int age = rs.getInt("age");
				int kor = rs.getInt("kor");
				int eng = rs.getInt("eng");
				int math = rs.getInt("math");
				int total = rs.getInt("total");
				Double avg = rs.getDouble("avg");
				String grade = rs.getString("grade");
				list.add(new Student(id, name, age, kor, eng, math, total, avg, grade));
			}
		} catch (Exception e) {
			System.out.println("select 오류발생 " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("ps close 오류" + e.getMessage());
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				System.out.println("connection close 오류" + e.getMessage());
			}
		} // 파이널리
		return list;
	}

	public int delete(int deleteid) {
		// 3.데이타를 삽입한다. insert into 테이블명(필드, ...) values(_, _, _);
		this.connect();
		PreparedStatement ps = null;
		int returnValue = -1;
//				Student s = new Student("kbj", 34, 100, 100, 100);
//				s.calTotal();
//				s.calAvg();
//				s.calGrade();
		String query = "delete  from studenttbl where id = ?";
		try {
			ps = connection.prepareStatement(query);
			ps.setInt(1, deleteid);
			// 삽입 성공하면 1을 리턴한다.엔터
			returnValue = ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("insert 오류발생 " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("ps close 오류" + e.getMessage());
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				System.out.println("connection close 오류" + e.getMessage());
			}
		} // 파이널리
		return returnValue;
	}
}