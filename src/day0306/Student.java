package day0306;

import java.util.Objects;

public class Student implements Comparable<Student> {
	// 정적맴버변수
	public static final int COUNT = 3;
	// 맴버변수
	private int id;
	private String name;
	private int age;
	private int kor;
	private int eng;
	private int math;
	private int total;
	private double avg;
	private String grade;

	// 생성자오버로딩(), (이름), (이름,나이), (이름, 나이,국어, 영어, 수학)
	public Student() {

	}

	public Student(String name) {
		this(name, 0, 0, 0, 0);
	}

	public Student(String name, int age) {
		this(name, age, 0, 0, 0);
	}

	public Student(String name, int age, int kor, int eng, int math) {
		this(0, name, age, kor, eng, math, 0, 0.0, null);
	}

	public Student(int id, String name, int age, int kor, int eng, int math, int total, double avg, String grade) {
		super();
		this.id = id;
		this.name = name;
		this.age = age;
		this.kor = kor;
		this.eng = eng;
		this.math = math;
		this.total = total;
		this.avg = avg;
		this.grade = grade;
	}

	// 맴버함수
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getKor() {
		return kor;
	}

	public void setKor(int kor) {
		this.kor = kor;
	}

	public int getEng() {
		return eng;
	}

	public void setEng(int eng) {
		this.eng = eng;
	}

	public int getMath() {
		return math;
	}

	public void setMath(int math) {
		this.math = math;
	}

	public int getTotal() {
		return total;
	}

	public void calTotal() {
		this.total = this.kor + this.eng + this.math;
	}

	public double getAvg() {
		String data = String.format("%.2f", this.avg);
		return Double.parseDouble(data);
	}

	public void calAvg() {
		// String.format("%.2f,this.avg") 소수점 두자리 출력
		this.avg = this.total / (double) Student.COUNT;
	}

	public String getGrade() {
		return grade;
	}

	public void calGrade() {
		switch ((int) (this.avg / 10)) {
		case 10:
		case 9:
			this.grade = "A";
			break;
		case 8:
			this.grade = "B";
			break;
		case 7:
			this.grade = "C";
			break;
		case 6:
			this.grade = "D";
			break;
		default:
			this.grade = "F";
			break;
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.age, this.name);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Student) {
			Student stu = (Student) obj;
			return (this.age == stu.age) && (this.name.equals(stu.name));
		}
		return false;
	}

	// toString 오버라이딩
	@Override
	public String toString() {
		return id + "\t" + name + "\t" + age + "\t" + kor + "\t" + eng + "\t" + math + "\t" + total + "\t"
				+ String.format("%.2f", this.avg) + "\t" + grade;
	}

	@Override
	public int compareTo(Student o) {
		if ((this.total - o.total) == 0) {
			return 0;
		} else if ((this.total - o.total) > 0) {
			return 1;
		} else {
			return -1;
		}
	}
}
