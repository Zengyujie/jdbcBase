package daoPro;

/*
 * 
Student调用父类的sleep方法时，如果父类中调用了this的方法
并且该方法被子类重写过，那么仍然会调用子类的方法，而不是父类的方法
因为就算是继承，this只表示一个对象，那就是子类的对象，并不会
指向父类的对象


 * 
 * 
 */


public class TestSuperClass {
	public static void main(String[] args) {
		Student s = new Student();
		s.sleep();
	}
}


class Person{
	
	public void eat() {
		System.out.println("person Eat");
	}
	
	public void sleep() {
		this.eat();
		System.out.println("person sleep");
	}
	
}

class Student extends Person{
	@Override
	public void sleep() {
		System.out.println("student sleep");
		super.sleep();
	}
	
	@Override
	public void eat() {
		System.out.println("student eat");
	}
}